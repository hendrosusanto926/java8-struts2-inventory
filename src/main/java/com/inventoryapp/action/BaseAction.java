package com.inventoryapp.action;

import com.inventoryapp.util.PageResult;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport {

    private int page = 1;
    private int size = 6;
    private long totalRecords;
    private int totalPages;
    private String message;

    protected int normalizedPage() {
        return page < 1 ? 1 : page;
    }

    protected int normalizedSize() {
        if (size < 1) {
            return 6;
        }
        return Math.min(size, 100);
    }

    protected void applyPageResult(PageResult<?> pageResult) {
        if (pageResult == null) {
            totalRecords = 0L;
            totalPages = 0;
            return;
        }
        this.page = pageResult.getPage();
        this.size = pageResult.getSize();
        this.totalRecords = pageResult.getTotalRecords();
        this.totalPages = pageResult.getTotalPages();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
