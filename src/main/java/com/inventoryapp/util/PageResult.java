package com.inventoryapp.util;

import java.util.Collections;
import java.util.List;

public class PageResult<T> {

    private final List<T> items;
    private final int page;
    private final int size;
    private final long totalRecords;
    private final int totalPages;

    public PageResult(List<T> items, int page, int size, long totalRecords) {
        this.items = items == null ? Collections.<T>emptyList() : items;
        this.page = page;
        this.size = size;
        this.totalRecords = totalRecords;
        this.totalPages = size <= 0 ? 0 : (int) Math.ceil((double) totalRecords / size);
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
