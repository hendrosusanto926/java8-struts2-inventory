package com.inventoryapp.action;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.InventoryTransaction;
import com.inventoryapp.model.Item;
import com.inventoryapp.service.InventoryService;
import com.inventoryapp.service.ItemService;
import com.inventoryapp.util.PageResult;

import java.util.Collections;
import java.util.List;

public class InventoryAction extends BaseAction {

    private final InventoryService inventoryService = new InventoryService();
    private final ItemService itemService = new ItemService();
    private List<InventoryTransaction> transactions = Collections.emptyList();
    private List<Item> itemOptions;
    private InventoryTransaction inventory = new InventoryTransaction();
    private Long id;

    public String list() {
        try {
            loadTransactions();
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public String form() {
        try {
            if (id != null) {
                inventory = inventoryService.findById(id);
            } else {
                inventory = new InventoryTransaction();
            }
            ensureItemOptions();
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public String save() {
        if (hasFieldErrors()) {
            return INPUT;
        }
        try {
            inventoryService.save(inventory);
            loadTransactions();
            setMessage("Inventory transaction saved successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while saving inventory transaction");
            return ERROR;
        }
    }

    public String update() {
        if (hasFieldErrors()) {
            return INPUT;
        }
        try {
            inventoryService.update(inventory);
            loadTransactions();
            setMessage("Inventory transaction updated successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while updating inventory transaction");
            return ERROR;
        }
    }

    public String delete() {
        try {
            if (id == null) {
                addActionError("Inventory transaction id is required");
                return ERROR;
            }
            inventoryService.delete(id);
            loadTransactions();
            setMessage("Inventory transaction deleted successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            try {
                loadTransactions();
            } catch (BusinessException ignored) {
                // Keep the original error message.
            }
            return SUCCESS;
        } catch (Exception ex) {
            addActionError("Unexpected error while deleting inventory transaction");
            return ERROR;
        }
    }

    public void validateSave() {
        validateInventory(false);
    }

    public void validateUpdate() {
        validateInventory(true);
    }

    private void validateInventory(boolean update) {
        if (inventory == null) {
            inventory = new InventoryTransaction();
        }
        if (update && inventory.getId() == null) {
            addFieldError("inventory.id", "Inventory transaction id is required");
        }
        if (inventory.getItemId() == null) {
            addFieldError("inventory.itemId", "Item is required");
        }
        if (inventory.getQty() == null) {
            addFieldError("inventory.qty", "Quantity is required");
        } else if (inventory.getQty() <= 0) {
            addFieldError("inventory.qty", "Quantity must be greater than 0");
        }
        if (inventory.getType() == null || inventory.getType().trim().isEmpty()) {
            addFieldError("inventory.type", "Type is required");
        } else if (!"T".equalsIgnoreCase(inventory.getType()) && !"W".equalsIgnoreCase(inventory.getType())) {
            addFieldError("inventory.type", "Type must be T or W");
        }
    }

    private void loadTransactions() {
        PageResult<InventoryTransaction> pageResult = inventoryService.list(normalizedPage(), normalizedSize());
        transactions = pageResult.getItems();
        applyPageResult(pageResult);
    }

    private void ensureItemOptions() {
        if (itemOptions == null) {
            itemOptions = itemService.findAll();
        }
    }

    public List<InventoryTransaction> getTransactions() {
        return transactions;
    }

    public List<Item> getItemOptions() {
        ensureItemOptions();
        return itemOptions;
    }

    public InventoryTransaction getInventory() {
        return inventory;
    }

    public void setInventory(InventoryTransaction inventory) {
        this.inventory = inventory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
