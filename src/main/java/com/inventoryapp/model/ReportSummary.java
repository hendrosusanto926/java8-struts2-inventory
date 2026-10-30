package com.inventoryapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportSummary {

    private long totalOrder;
    private int totalInventory;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private List<ReportRow> rows = new ArrayList<ReportRow>();

    public long getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(long totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(int totalInventory) {
        this.totalInventory = totalInventory;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<ReportRow> getRows() {
        return rows;
    }

    public void setRows(List<ReportRow> rows) {
        this.rows = rows;
    }
}
