package com.inventoryapp.dao;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.SalesOrder;
import com.inventoryapp.util.Db;
import com.inventoryapp.util.PageResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public PageResult<SalesOrder> findPage(int page, int size) {
        int safePage = page < 1 ? 1 : page;
        int safeSize = size < 1 ? 10 : size;
        try (Connection connection = Db.getConnection()) {
            long totalRecords = countAll(connection);
            List<SalesOrder> orders = findPage(connection, safePage, safeSize);
            return new PageResult<SalesOrder>(orders, safePage, safeSize, totalRecords);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load orders", ex);
        }
    }

    public List<SalesOrder> findAll() {
        try (Connection connection = Db.getConnection()) {
            return findAll(connection);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load orders", ex);
        }
    }

    public SalesOrder findById(long id) {
        try (Connection connection = Db.getConnection()) {
            return findById(connection, id);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load order", ex);
        }
    }

    public SalesOrder findById(Connection connection, long id) throws SQLException {
        String sql = "SELECT o.id, o.order_no, o.item_id, i.name AS item_name, o.qty, o.unit_price, o.total_amount, o.created_at, o.updated_at " +
                "FROM dbo.sales_order o " +
                "INNER JOIN dbo.item i ON i.id = o.item_id " +
                "WHERE o.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
                return null;
            }
        }
    }

    public long insert(Connection connection, SalesOrder order) throws SQLException {
        String sql = "INSERT INTO dbo.sales_order (order_no, item_id, qty, unit_price, total_amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, order.getOrderNo());
            statement.setLong(2, order.getItemId());
            statement.setInt(3, order.getQty());
            statement.setBigDecimal(4, order.getUnitPrice());
            statement.setBigDecimal(5, order.getTotalAmount());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    order.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to insert order");
    }

    public void update(Connection connection, SalesOrder order) throws SQLException {
        String sql = "UPDATE dbo.sales_order SET item_id = ?, qty = ?, unit_price = ?, total_amount = ?, updated_at = SYSDATETIME() WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getItemId());
            statement.setInt(2, order.getQty());
            statement.setBigDecimal(3, order.getUnitPrice());
            statement.setBigDecimal(4, order.getTotalAmount());
            statement.setLong(5, order.getId());
            statement.executeUpdate();
        }
    }

    public void updateOrderNo(Connection connection, long id, String orderNo) throws SQLException {
        String sql = "UPDATE dbo.sales_order SET order_no = ?, updated_at = SYSDATETIME() WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, orderNo);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, long id) throws SQLException {
        String sql = "DELETE FROM dbo.sales_order WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private long countAll(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM dbo.sales_order";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        }
    }

    private List<SalesOrder> findPage(Connection connection, int page, int size) throws SQLException {
        String sql = "SELECT o.id, o.order_no, o.item_id, i.name AS item_name, o.qty, o.unit_price, o.total_amount, o.created_at, o.updated_at " +
                "FROM dbo.sales_order o " +
                "INNER JOIN dbo.item i ON i.id = o.item_id " +
                "ORDER BY o.created_at DESC, o.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * size;
        List<SalesOrder> orders = new ArrayList<SalesOrder>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offset);
            statement.setInt(2, size);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }
        return orders;
    }

    private List<SalesOrder> findAll(Connection connection) throws SQLException {
        String sql = "SELECT o.id, o.order_no, o.item_id, i.name AS item_name, o.qty, o.unit_price, o.total_amount, o.created_at, o.updated_at " +
                "FROM dbo.sales_order o " +
                "INNER JOIN dbo.item i ON i.id = o.item_id " +
                "ORDER BY o.created_at DESC, o.id DESC";
        List<SalesOrder> orders = new ArrayList<SalesOrder>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(mapOrder(resultSet));
            }
        }
        return orders;
    }

    private SalesOrder mapOrder(ResultSet resultSet) throws SQLException {
        SalesOrder order = new SalesOrder();
        order.setId(resultSet.getLong("id"));
        order.setOrderNo(resultSet.getString("order_no"));
        order.setItemId(resultSet.getLong("item_id"));
        order.setItemName(resultSet.getString("item_name"));
        order.setQty(resultSet.getInt("qty"));
        order.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        order.setCreatedAt(resultSet.getTimestamp("created_at"));
        order.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return order;
    }
}
