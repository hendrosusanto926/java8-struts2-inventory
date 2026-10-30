package com.inventoryapp.service;

import com.inventoryapp.dao.ReportDao;
import com.inventoryapp.model.ReportRow;
import com.inventoryapp.model.ReportSummary;
import com.inventoryapp.util.PageResult;

public class ReportService {

    private final ReportDao reportDao = new ReportDao();

    public ReportSummary loadSummary() {
        return reportDao.loadSummary();
    }

    public PageResult<ReportRow> listRows(int page, int size) {
        return reportDao.findRevenueRowsPage(page, size);
    }
}
