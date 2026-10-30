package com.inventoryapp.model;

import java.sql.Timestamp;

public class InventoryTransaction {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer qty;
    private String type;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTopUp() {
        return "T".equalsIgnoreCase(type);
    }

    public boolean isWithdrawal() {
        return "W".equalsIgnoreCase(type);
    }

    public String getTypeLabel() {
        if (isTopUp()) {
            return "Top Up";
        }
        if (isWithdrawal()) {
            return "Withdrawal";
        }
        return type == null ? "" : type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
