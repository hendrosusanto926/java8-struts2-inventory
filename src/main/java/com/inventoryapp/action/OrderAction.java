package com.inventoryapp.action;

import com.inventoryapp.exception.BusinessException;
import com.inventoryapp.model.Item;
import com.inventoryapp.model.SalesOrder;
import com.inventoryapp.service.ItemService;
import com.inventoryapp.service.OrderService;
import com.inventoryapp.util.PageResult;

import java.util.Collections;
import java.util.List;

public class OrderAction extends BaseAction {

    private final OrderService orderService = new OrderService();
    private final ItemService itemService = new ItemService();
    private List<SalesOrder> orders = Collections.emptyList();
    private List<Item> itemOptions;
    private SalesOrder order = new SalesOrder();
    private Long id;

    public String list() {
        try {
            loadOrders();
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public String form() {
        try {
            if (id != null) {
                order = orderService.findById(id);
            } else {
                order = new SalesOrder();
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
            orderService.save(order);
            loadOrders();
            setMessage("Order saved successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while saving order");
            return ERROR;
        }
    }

    public String update() {
        if (hasFieldErrors()) {
            return INPUT;
        }
        try {
            orderService.update(order);
            loadOrders();
            setMessage("Order updated successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            return INPUT;
        } catch (Exception ex) {
            addActionError("Unexpected error while updating order");
            return ERROR;
        }
    }

    public String delete() {
        try {
            if (id == null) {
                addActionError("Order id is required");
                return ERROR;
            }
            orderService.delete(id);
            loadOrders();
            setMessage("Order deleted successfully");
            return SUCCESS;
        } catch (BusinessException ex) {
            addActionError(ex.getMessage());
            try {
                loadOrders();
            } catch (BusinessException ignored) {
                // Keep the original error message.
            }
            return SUCCESS;
        } catch (Exception ex) {
            addActionError("Unexpected error while deleting order");
            return ERROR;
        }
    }

    public void validateSave() {
        validateOrder(false);
    }

    public void validateUpdate() {
        validateOrder(true);
    }

    private void validateOrder(boolean update) {
        if (order == null) {
            order = new SalesOrder();
        }
        if (update && order.getId() == null) {
            addFieldError("order.id", "Order id is required");
        }
        if (order.getItemId() == null) {
            addFieldError("order.itemId", "Item is required");
        }
        if (order.getQty() == null) {
            addFieldError("order.qty", "Quantity is required");
        } else if (order.getQty() <= 0) {
            addFieldError("order.qty", "Quantity must be greater than 0");
        }
    }

    private void loadOrders() {
        PageResult<SalesOrder> pageResult = orderService.list(normalizedPage(), normalizedSize());
        orders = pageResult.getItems();
        applyPageResult(pageResult);
    }

    private void ensureItemOptions() {
        if (itemOptions == null) {
            itemOptions = itemService.findAll();
        }
    }

    public List<SalesOrder> getOrders() {
        return orders;
    }

    public List<Item> getItemOptions() {
        ensureItemOptions();
        return itemOptions;
    }

    public SalesOrder getOrder() {
        return order;
    }

    public void setOrder(SalesOrder order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
