package com.inventoryapp.dao;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.Item;
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

public class ItemDao {

    public PageResult<Item> findPage(int page, int size) {
        int safePage = page < 1 ? 1 : page;
        int safeSize = size < 1 ? 10 : size;
        try (Connection connection = Db.getConnection()) {
            long totalRecords = countAll(connection);
            List<Item> items = findPage(connection, safePage, safeSize);
            return new PageResult<Item>(items, safePage, safeSize, totalRecords);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load items", ex);
        }
    }

    public List<Item> findAll() {
        try (Connection connection = Db.getConnection()) {
            return findAll(connection);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load items", ex);
        }
    }

    public Item findById(long id) {
        try (Connection connection = Db.getConnection()) {
            return findById(connection, id);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load item", ex);
        }
    }

    public Item findById(Connection connection, long id) throws SQLException {
        String sql = "SELECT id, name, price, current_stock, is_deleted, created_at, updated_at FROM dbo.item WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapItem(resultSet);
                }
                return null;
            }
        }
    }

    public long insert(Connection connection, Item item) throws SQLException {
        String sql = "INSERT INTO dbo.item (name, price, current_stock, is_deleted) VALUES (?, ?, ?, 0)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setBigDecimal(2, item.getPrice());
            statement.setInt(3, safeStock(item.getCurrentStock()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    item.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to insert item");
    }

    public void update(Connection connection, Item item) throws SQLException {
        String sql = "UPDATE dbo.item SET name = ?, price = ?, current_stock = ?, updated_at = SYSDATETIME() WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getName());
            statement.setBigDecimal(2, item.getPrice());
            statement.setInt(3, safeStock(item.getCurrentStock()));
            statement.setLong(4, item.getId());
            statement.executeUpdate();
        }
    }

    public void updateStock(Connection connection, long id, int currentStock) throws SQLException {
        String sql = "UPDATE dbo.item SET current_stock = ?, updated_at = SYSDATETIME() WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, currentStock);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, long id) throws SQLException {
        String sql = "UPDATE dbo.item SET is_deleted = 1, updated_at = SYSDATETIME() WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private long countAll(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM dbo.item WHERE is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        }
    }

    private List<Item> findPage(Connection connection, int page, int size) throws SQLException {
        String sql = "SELECT id, name, price, current_stock, created_at, updated_at " +
                "FROM dbo.item WHERE is_deleted = 0 ORDER BY id ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * size;
        List<Item> items = new ArrayList<Item>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offset);
            statement.setInt(2, size);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapItem(resultSet));
                }
            }
        }
        return items;
    }

    private List<Item> findAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name, price, current_stock, created_at, updated_at FROM dbo.item WHERE is_deleted = 0 ORDER BY id ASC";
        List<Item> items = new ArrayList<Item>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                items.add(mapItem(resultSet));
            }
        }
        return items;
    }

    private Item mapItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setPrice(resultSet.getBigDecimal("price"));
        item.setCurrentStock(resultSet.getInt("current_stock"));
        item.setCreatedAt(resultSet.getTimestamp("created_at"));
        item.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return item;
    }

    private int safeStock(Integer currentStock) {
        return currentStock == null ? 0 : currentStock.intValue();
    }
}
