package com.inventoryapp.dao;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.InventoryTransaction;
import com.inventoryapp.util.Db;
import com.inventoryapp.util.PageResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventoryDao {

    public PageResult<InventoryTransaction> findPage(int page, int size) {
        int safePage = page < 1 ? 1 : page;
        int safeSize = size < 1 ? 10 : size;
        try (Connection connection = Db.getConnection()) {
            long totalRecords = countAll(connection);
            List<InventoryTransaction> items = findPage(connection, safePage, safeSize);
            return new PageResult<InventoryTransaction>(items, safePage, safeSize, totalRecords);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load inventory transactions", ex);
        }
    }

    public List<InventoryTransaction> findAll() {
        try (Connection connection = Db.getConnection()) {
            return findAll(connection);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load inventory transactions", ex);
        }
    }

    public InventoryTransaction findById(long id) {
        try (Connection connection = Db.getConnection()) {
            return findById(connection, id);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load inventory transaction", ex);
        }
    }

    public InventoryTransaction findById(Connection connection, long id) throws SQLException {
        String sql = "SELECT t.id, t.item_id, i.name AS item_name, t.qty, t.type, t.note, t.created_at, t.updated_at " +
                "FROM dbo.inventory_transaction t " +
                "INNER JOIN dbo.item i ON i.id = t.item_id " +
                "WHERE t.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapInventory(resultSet);
                }
                return null;
            }
        }
    }

    public long insert(Connection connection, InventoryTransaction transaction) throws SQLException {
        String sql = "INSERT INTO dbo.inventory_transaction (item_id, qty, type, note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, transaction.getItemId());
            statement.setInt(2, transaction.getQty());
            statement.setString(3, transaction.getType());
            statement.setString(4, transaction.getNote());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    transaction.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to insert inventory transaction");
    }

    public void update(Connection connection, InventoryTransaction transaction) throws SQLException {
        String sql = "UPDATE dbo.inventory_transaction SET item_id = ?, qty = ?, type = ?, note = ?, updated_at = SYSDATETIME() WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transaction.getItemId());
            statement.setInt(2, transaction.getQty());
            statement.setString(3, transaction.getType());
            statement.setString(4, transaction.getNote());
            statement.setLong(5, transaction.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, long id) throws SQLException {
        String sql = "DELETE FROM dbo.inventory_transaction WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private long countAll(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM dbo.inventory_transaction";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        }
    }

    private List<InventoryTransaction> findPage(Connection connection, int page, int size) throws SQLException {
        String sql = "SELECT t.id, t.item_id, i.name AS item_name, t.qty, t.type, t.note, t.created_at, t.updated_at " +
                "FROM dbo.inventory_transaction t " +
                "INNER JOIN dbo.item i ON i.id = t.item_id " +
                "ORDER BY t.created_at DESC, t.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * size;
        List<InventoryTransaction> items = new ArrayList<InventoryTransaction>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offset);
            statement.setInt(2, size);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapInventory(resultSet));
                }
            }
        }
        return items;
    }

    private List<InventoryTransaction> findAll(Connection connection) throws SQLException {
        String sql = "SELECT t.id, t.item_id, i.name AS item_name, t.qty, t.type, t.note, t.created_at, t.updated_at " +
                "FROM dbo.inventory_transaction t " +
                "INNER JOIN dbo.item i ON i.id = t.item_id " +
                "ORDER BY t.created_at DESC, t.id DESC";
        List<InventoryTransaction> items = new ArrayList<InventoryTransaction>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                items.add(mapInventory(resultSet));
            }
        }
        return items;
    }

    private InventoryTransaction mapInventory(ResultSet resultSet) throws SQLException {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setItemId(resultSet.getLong("item_id"));
        transaction.setItemName(resultSet.getString("item_name"));
        transaction.setQty(resultSet.getInt("qty"));
        transaction.setType(resultSet.getString("type"));
        transaction.setNote(resultSet.getString("note"));
        transaction.setCreatedAt(resultSet.getTimestamp("created_at"));
        transaction.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return transaction;
    }
}
