package com.inventoryapp.util;

import com.inventoryapp.exception.BusinessException;

public final class StockCalculator {

    private StockCalculator() {
    }

    public static int applyInventoryTransaction(int currentStock, String type, int qty) {
        validateQuantity(qty);
        if ("T".equalsIgnoreCase(type)) {
            return currentStock + qty;
        }
        if ("W".equalsIgnoreCase(type)) {
            int newStock = currentStock - qty;
            if (newStock < 0) {
                throw new BusinessException("Insufficient stock for withdrawal");
            }
            return newStock;
        }
        throw new BusinessException("Inventory type must be T or W");
    }

    public static int reverseInventoryTransaction(int currentStock, String type, int qty) {
        validateQuantity(qty);
        if ("T".equalsIgnoreCase(type)) {
            int newStock = currentStock - qty;
            if (newStock < 0) {
                throw new BusinessException("Insufficient stock to reverse top up transaction");
            }
            return newStock;
        }
        if ("W".equalsIgnoreCase(type)) {
            return currentStock + qty;
        }
        throw new BusinessException("Inventory type must be T or W");
    }

    public static int reserveStock(int currentStock, int qty) {
        validateQuantity(qty);
        if (currentStock < qty) {
            throw new BusinessException("Insufficient stock");
        }
        return currentStock - qty;
    }

    public static int restoreStock(int currentStock, int qty) {
        validateQuantity(qty);
        return currentStock + qty;
    }

    public static void validateQuantity(int qty) {
        if (qty <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }
    }
}
