package com.inventoryapp.service;

import com.inventoryapp.dao.ItemDao;
import com.inventoryapp.dao.OrderDao;
import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.Item;
import com.inventoryapp.model.SalesOrder;
import com.inventoryapp.util.Db;
import com.inventoryapp.util.PageResult;
import com.inventoryapp.util.StockCalculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class OrderService {

    private final OrderDao orderDao = new OrderDao();
    private final ItemDao itemDao = new ItemDao();

    public PageResult<SalesOrder> list(int page, int size) {
        return orderDao.findPage(page, size);
    }

    public List<SalesOrder> findAll() {
        return orderDao.findAll();
    }

    public SalesOrder findById(long id) {
        SalesOrder order = orderDao.findById(id);
        if (order == null) {
            throw new BusinessException("Order not found");
        }
        return order;
    }

    public long save(SalesOrder order) {
        validate(order, false);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = itemDao.findById(connection, order.getItemId());
                if (item == null) {
                    throw new BusinessException("Item not found");
                }
                int newStock = StockCalculator.reserveStock(safeStock(item), order.getQty());
                itemDao.updateStock(connection, item.getId(), newStock);

                order.setUnitPrice(item.getPrice());
                order.setTotalAmount(item.getPrice().multiply(BigDecimal.valueOf(order.getQty())));
                order.setOrderNo(generateTemporaryOrderNo());
                normalize(order);

                long id = orderDao.insert(connection, order);
                String finalOrderNo = generateOrderNo(id);
                orderDao.updateOrderNo(connection, id, finalOrderNo);
                order.setOrderNo(finalOrderNo);
                connection.commit();
                return id;
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to save order", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to save order", ex);
        }
    }

    public void update(SalesOrder order) {
        validate(order, true);
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                SalesOrder existing = orderDao.findById(connection, order.getId());
                if (existing == null) {
                    throw new BusinessException("Order not found");
                }

                order.setOrderNo(existing.getOrderNo());
                Item oldItem = itemDao.findById(connection, existing.getItemId());
                if (oldItem == null) {
                    throw new BusinessException("Old item not found");
                }
                int restoredStock = StockCalculator.restoreStock(safeStock(oldItem), existing.getQty());
                itemDao.updateStock(connection, oldItem.getId(), restoredStock);

                Item newItem = itemDao.findById(connection, order.getItemId());
                if (newItem == null) {
                    throw new BusinessException("Item not found");
                }
                int newStock = StockCalculator.reserveStock(safeStock(newItem), order.getQty());
                itemDao.updateStock(connection, newItem.getId(), newStock);

                order.setUnitPrice(newItem.getPrice());
                order.setTotalAmount(newItem.getPrice().multiply(BigDecimal.valueOf(order.getQty())));
                normalize(order);

                orderDao.update(connection, order);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to update order", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to update order", ex);
        }
    }

    public void delete(long id) {
        try (Connection connection = Db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                SalesOrder existing = orderDao.findById(connection, id);
                if (existing == null) {
                    throw new BusinessException("Order not found");
                }
                Item item = itemDao.findById(connection, existing.getItemId());
                if (item == null) {
                    throw new BusinessException("Item not found");
                }
                int restoredStock = StockCalculator.restoreStock(safeStock(item), existing.getQty());
                itemDao.updateStock(connection, item.getId(), restoredStock);
                orderDao.delete(connection, id);
                connection.commit();
            } catch (java.sql.SQLException ex) {
                Db.rollbackQuietly(connection);
                throw new BusinessException("Failed to delete order", ex);
            } catch (RuntimeException ex) {
                Db.rollbackQuietly(connection);
                throw ex;
            }
        } catch (java.sql.SQLException ex) {
            throw new BusinessException("Failed to delete order", ex);
        }
    }

    private void validate(SalesOrder order, boolean update) {
        if (order == null) {
            throw new BusinessException("Order data is required");
        }
        if (update && order.getId() == null) {
            throw new BusinessException("Order id is required");
        }
        if (order.getItemId() == null) {
            throw new BusinessException("Item is required");
        }
        if (order.getQty() == null || order.getQty() <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }
    }

    private void normalize(SalesOrder order) {
        if (order.getOrderNo() != null) {
            order.setOrderNo(order.getOrderNo().trim());
        }
    }

    private String generateTemporaryOrderNo() {
        return "TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private String generateOrderNo(long id) {
        return "O" + id;
    }

    private int safeStock(Item item) {
        return item.getCurrentStock() == null ? 0 : item.getCurrentStock();
    }
}
