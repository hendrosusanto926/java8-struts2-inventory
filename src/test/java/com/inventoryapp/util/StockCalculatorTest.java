package com.inventoryapp.util;

import com.inventoryapp.exception.BusinessException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StockCalculatorTest {

    @Test
    public void applyInventoryTransactionShouldIncreaseStockForTopUp() {
        assertEquals(15, StockCalculator.applyInventoryTransaction(10, "T", 5));
    }

    @Test
    public void applyInventoryTransactionShouldDecreaseStockForWithdrawal() {
        assertEquals(7, StockCalculator.applyInventoryTransaction(10, "W", 3));
    }

    @Test(expected = BusinessException.class)
    public void applyInventoryTransactionShouldRejectInsufficientWithdrawal() {
        StockCalculator.applyInventoryTransaction(2, "W", 3);
    }

    @Test
    public void reserveStockShouldReduceStockForOrder() {
        assertEquals(8, StockCalculator.reserveStock(10, 2));
    }

    @Test(expected = BusinessException.class)
    public void reserveStockShouldRejectInsufficientStock() {
        StockCalculator.reserveStock(1, 2);
    }

    @Test
    public void reverseInventoryTransactionShouldRestoreTopUp() {
        assertEquals(5, StockCalculator.reverseInventoryTransaction(10, "T", 5));
    }

    @Test
    public void reverseInventoryTransactionShouldRestoreWithdrawal() {
        assertEquals(15, StockCalculator.reverseInventoryTransaction(10, "W", 5));
    }
}
