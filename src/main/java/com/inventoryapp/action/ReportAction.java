package com.inventoryapp.action;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.ReportRow;
import com.inventoryapp.model.ReportSummary;
import com.inventoryapp.service.ReportService;
import com.inventoryapp.util.PageResult;

import java.util.Collections;
import java.util.List;

public class ReportAction extends BaseAction {

    private final ReportService reportService = new ReportService();
    private ReportSummary reportSummary = new ReportSummary();
    private List<ReportRow> rows = Collections.emptyList();

    public String list() {
        try {
            reportSummary = reportService.loadSummary();
            PageResult<ReportRow> pageResult = reportService.listRows(normalizedPage(), normalizedSize());
            rows = pageResult.getItems();
            reportSummary.setRows(rows);
            applyPageResult(pageResult);
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public ReportSummary getReportSummary() {
        return reportSummary;
    }

    public List<ReportRow> getRows() {
        return rows;
    }
}
