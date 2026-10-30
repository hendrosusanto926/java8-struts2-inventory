package com.inventoryapp.dao;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.ReportRow;
import com.inventoryapp.model.ReportSummary;
import com.inventoryapp.util.Db;
import com.inventoryapp.util.PageResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    public ReportSummary loadSummary() {
        try (Connection connection = Db.getConnection()) {
            ReportSummary summary = new ReportSummary();
            summary.setTotalOrder(countOrders(connection));
            summary.setTotalInventory(totalInventory(connection));
            summary.setTotalRevenue(totalRevenue(connection));
            return summary;
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load report", ex);
        }
    }

    public PageResult<ReportRow> findRevenueRowsPage(int page, int size) {
        int safePage = page < 1 ? 1 : page;
        int safeSize = size < 1 ? 10 : size;
        try (Connection connection = Db.getConnection()) {
            long totalRecords = countActiveItems(connection);
            List<ReportRow> rows = findRevenueRowsPage(connection, safePage, safeSize);
            return new PageResult<ReportRow>(rows, safePage, safeSize, totalRecords);
        } catch (SQLException ex) {
            throw new BusinessException("Failed to load report rows", ex);
        }
    }

    private long countOrders(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM dbo.sales_order";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        }
    }

    private int totalInventory(Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(SUM(current_stock), 0) FROM dbo.item WHERE is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private BigDecimal totalRevenue(Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(SUM(o.total_amount), 0) AS total_revenue " +
                "FROM dbo.sales_order o " +
                "INNER JOIN dbo.item i ON i.id = o.item_id " +
                "WHERE i.is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getBigDecimal("total_revenue") : BigDecimal.ZERO;
        }
    }

    private long countActiveItems(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM dbo.item WHERE is_deleted = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        }
    }

    private List<ReportRow> findRevenueRowsPage(Connection connection, int page, int size) throws SQLException {
        String sql = "SELECT i.id AS item_id, i.name AS item_name, i.current_stock, " +
                "COALESCE(SUM(o.qty), 0) AS total_order_qty, " +
                "COALESCE(SUM(o.total_amount), 0) AS total_revenue " +
                "FROM dbo.item i " +
                "LEFT JOIN dbo.sales_order o ON o.item_id = i.id " +
                "WHERE i.is_deleted = 0 " +
                "GROUP BY i.id, i.name, i.current_stock " +
                "ORDER BY i.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * size;
        List<ReportRow> rows = new ArrayList<ReportRow>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offset);
            statement.setInt(2, size);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ReportRow row = new ReportRow();
                    row.setItemId(resultSet.getLong("item_id"));
                    row.setItemName(resultSet.getString("item_name"));
                    row.setCurrentStock(resultSet.getInt("current_stock"));
                    row.setTotalOrderQty(resultSet.getInt("total_order_qty"));
                    row.setTotalRevenue(resultSet.getBigDecimal("total_revenue"));
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    private List<ReportRow> findRevenueRows(Connection connection) throws SQLException {
        String sql = "SELECT i.id AS item_id, i.name AS item_name, i.current_stock, " +
                "COALESCE(SUM(o.qty), 0) AS total_order_qty, " +
                "COALESCE(SUM(o.total_amount), 0) AS total_revenue " +
                "FROM dbo.item i " +
                "LEFT JOIN dbo.sales_order o ON o.item_id = i.id " +
                "WHERE i.is_deleted = 0 " +
                "GROUP BY i.id, i.name, i.current_stock " +
                "ORDER BY i.id";
        List<ReportRow> rows = new ArrayList<ReportRow>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ReportRow row = new ReportRow();
                row.setItemId(resultSet.getLong("item_id"));
                row.setItemName(resultSet.getString("item_name"));
                row.setCurrentStock(resultSet.getInt("current_stock"));
                row.setTotalOrderQty(resultSet.getInt("total_order_qty"));
                row.setTotalRevenue(resultSet.getBigDecimal("total_revenue"));
                rows.add(row);
            }
        }
        return rows;
    }
}
