package com.inventoryapp.service;

import com.inventoryapp.dao.ItemDao;
import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.Item;
import com.inventoryapp.util.PageResult;
import com.inventoryapp.util.Db;

import java.sql.Connection;

public class ItemService {

    private final ItemDao itemDao = new ItemDao();

    public PageResult<Item> list(int page, int size) {
        return itemDao.findPage(page, size);
    }

    public java.util.List<Item> findAll() {
        return itemDao.findAll();
    }

    public Item findById(long id) {
        Item item = itemDao.findById(id);
        if (item == null) {
            throw new BusinessException("Item not found");
        }
        return item;
    }

    public long save(Item item) {
        validate(item, false);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (item.getCurrentStock() == null) {
                    item.setCurrentStock(0);
                }
                long id = itemDao.insert(connection, item);
                connection.commit();
                return id;
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to save item", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to save item", ex);
        }
    }

    public void update(Item item) {
        validate(item, true);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item existing = itemDao.findById(connection, item.getId());
                if (existing == null) {
                    throw new BusinessException("Item not found");
                }
                item.setCurrentStock(existing.getCurrentStock());
                itemDao.update(connection, item);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to update item", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to update item", ex);
        }
    }

    public void delete(long id) {
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item existing = itemDao.findById(connection, id);
                if (existing == null) {
                    throw new BusinessException("Item not found");
                }
                itemDao.delete(connection, id);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to delete item", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to delete item", ex);
        }
    }

    private void validate(Item item, boolean update) {
        if (item == null) {
            throw new BusinessException("Item data is required");
        }
        if (update && item.getId() == null) {
            throw new BusinessException("Item id is required");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new BusinessException("Item name is required");
        }
        if (item.getPrice() == null || item.getPrice().signum() <= 0) {
            throw new BusinessException("Item price must be greater than 0");
        }
    }
}
