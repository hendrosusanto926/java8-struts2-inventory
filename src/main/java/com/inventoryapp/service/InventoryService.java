package com.inventoryapp.service;

import com.inventoryapp.dao.InventoryDao;
import com.inventoryapp.dao.ItemDao;
import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.InventoryTransaction;
import com.inventoryapp.model.Item;
import com.inventoryapp.util.Db;
import com.inventoryapp.util.PageResult;
import com.inventoryapp.util.StockCalculator;

import java.sql.Connection;

public class InventoryService {

    private final InventoryDao inventoryDao = new InventoryDao();
    private final ItemDao itemDao = new ItemDao();

    public PageResult<InventoryTransaction> list(int page, int size) {
        return inventoryDao.findPage(page, size);
    }

    public java.util.List<InventoryTransaction> findAll() {
        return inventoryDao.findAll();
    }

    public InventoryTransaction findById(long id) {
        InventoryTransaction transaction = inventoryDao.findById(id);
        if (transaction == null) {
            throw new BusinessException("Inventory transaction not found");
        }
        return transaction;
    }

    public long save(InventoryTransaction transaction) {
        validate(transaction, false);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = itemDao.findById(connection, transaction.getItemId());
                if (item == null) {
                    throw new BusinessException("Item not found");
                }
                int newStock = StockCalculator.applyInventoryTransaction(safeStock(item), transaction.getType(), transaction.getQty());
                itemDao.updateStock(connection, item.getId(), newStock);
                normalize(transaction);
                long id = inventoryDao.insert(connection, transaction);
                connection.commit();
                return id;
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to save inventory transaction", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to save inventory transaction", ex);
        }
    }

    public void update(InventoryTransaction transaction) {
        validate(transaction, true);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                InventoryTransaction existing = inventoryDao.findById(connection, transaction.getId());
                if (existing == null) {
                    throw new BusinessException("Inventory transaction not found");
                }

                Item oldItem = itemDao.findById(connection, existing.getItemId());
                if (oldItem == null) {
                    throw new BusinessException("Old item not found");
                }
                int revertedOldStock = StockCalculator.reverseInventoryTransaction(safeStock(oldItem), existing.getType(), existing.getQty());
                itemDao.updateStock(connection, oldItem.getId(), revertedOldStock);

                Item targetItem = itemDao.findById(connection, transaction.getItemId());
                if (targetItem == null) {
                    throw new BusinessException("Item not found");
                }
                int newStock = StockCalculator.applyInventoryTransaction(safeStock(targetItem), transaction.getType(), transaction.getQty());
                itemDao.updateStock(connection, targetItem.getId(), newStock);

                normalize(transaction);
                inventoryDao.update(connection, transaction);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to update inventory transaction", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to update inventory transaction", ex);
        }
    }

    public void delete(long id) {
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                InventoryTransaction existing = inventoryDao.findById(connection, id);
                if (existing == null) {
                    throw new BusinessException("Inventory transaction not found");
                }
                Item item = itemDao.findById(connection, existing.getItemId());
                if (item == null) {
                    throw new BusinessException("Item not found");
                }
                int revertedStock = StockCalculator.reverseInventoryTransaction(safeStock(item), existing.getType(), existing.getQty());
                itemDao.updateStock(connection, item.getId(), revertedStock);
                inventoryDao.delete(connection, id);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to delete inventory transaction", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to delete inventory transaction", ex);
        }
    }

    private void validate(InventoryTransaction transaction, boolean update) {
        if (transaction == null) {
            throw new BusinessException("Inventory transaction data is required");
        }
        if (update && transaction.getId() == null) {
            throw new BusinessException("Inventory transaction id is required");
        }
        if (transaction.getItemId() == null) {
            throw new BusinessException("Item is required");
        }
        if (transaction.getQty() == null || transaction.getQty() <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }
        if (transaction.getType() == null || (!"T".equalsIgnoreCase(transaction.getType()) && !"W".equalsIgnoreCase(transaction.getType()))) {
            throw new BusinessException("Inventory type must be T or W");
        }
    }

    private void normalize(InventoryTransaction transaction) {
        if (transaction.getType() != null) {
            transaction.setType(transaction.getType().toUpperCase());
        }
        if (transaction.getNote() != null) {
            transaction.setNote(transaction.getNote().trim());
        }
    }

    private int safeStock(Item item) {
        return item.getCurrentStock() == null ? 0 : item.getCurrentStock();
    }
}
