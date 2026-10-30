package com.inventoryapp.action;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.Item;
import com.inventoryapp.service.ItemService;
import com.inventoryapp.util.PageResult;

import java.util.Collections;
import java.util.List;

public class ItemAction extends BaseAction {

    private final ItemService itemService = new ItemService();
    private List<Item> items = Collections.emptyList();
    private Item item = new Item();
    private Long id;

    public String list() {
        try {
            loadItems();
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public String form() {
        try {
            if (id != null) {
                item = itemService.findById(id);
            } else {
                item = new Item();
            }
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
            itemService.save(item);
            loadItems();
            setMessage("Item saved successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while saving item");
            return ERROR;
        }
    }

    public String update() {
        if (hasFieldErrors()) {
            return INPUT;
        }
        try {
            itemService.update(item);
            loadItems();
            setMessage("Item updated successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while updating item");
            return ERROR;
        }
    }

    public String delete() {
        try {
            if (id == null) {
                addActionError("Item id is required");
                return ERROR;
            }
            itemService.delete(id);
            loadItems();
            setMessage("Item deleted successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            try {
                loadItems();
            } catch (BusinessException ignored) {
                // Keep the original error message.
            }
            return SUCCESS;
        } catch (Exception ex) {
            addActionError("Unexpected error while deleting item");
            return ERROR;
        }
    }

    public void validateSave() {
        validateItem(false);
    }

    public void validateUpdate() {
        validateItem(true);
    }

    private void validateItem(boolean update) {
        if (item == null) {
            item = new Item();
        }
        if (update && item.getId() == null) {
            addFieldError("item.id", "Item id is required");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            addFieldError("item.name", "Item name is required");
        }
        if (item.getPrice() == null) {
            addFieldError("item.price", "Item price is required");
        } else if (item.getPrice().signum() <= 0) {
            addFieldError("item.price", "Item price must be greater than 0");
        }
    }

    private void loadItems() {
        PageResult<Item> pageResult = itemService.list(normalizedPage(), normalizedSize());
        items = pageResult.getItems();
        applyPageResult(pageResult);
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
