<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Orders</span>
        <h1>Order Register</h1>
        <p>Stock is reserved when an order is saved.</p>
    </div>

    <div class="card">
        <div class="page-header">
            <div>
                <h2 class="section-title">Order Register</h2>
                <p class="section-note">Add, edit, or delete orders.</p>
            </div>
            <div class="actions">
                <a class="btn" href="order-form.action">New Order</a>
                <a class="btn secondary" href="inventory-list.action">Inventory</a>
            </div>
        </div>

        <div class="table-shell">
        <table class="grid">
            <thead>
            <tr>
                <th>ID</th>
                <th>Order No</th>
                <th>Item</th>
                <th>Qty</th>
                <th>Unit Price</th>
                <th>Total</th>
                <th>Updated</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <s:if test="orders == null || orders.isEmpty()">
                <tr>
                    <td colspan="8" class="muted">No orders.</td>
                </tr>
            </s:if>
            <s:iterator value="orders" var="ord">
                <tr>
                    <td><s:property value="#ord.id"/></td>
                    <td><s:property value="#ord.orderNo"/></td>
                    <td><s:property value="#ord.itemName"/></td>
                    <td><s:property value="#ord.qty"/></td>
                    <td><s:property value="#ord.unitPrice"/></td>
                    <td><span class="pill info"><s:property value="#ord.totalAmount"/></span></td>
                    <td><s:date name="#ord.updatedAt" format="dd/MM/yyyy HH:mm:ss"/></td>
                    <td>
                        <s:url var="editUrl" action="order-edit">
                            <s:param name="id" value="%{#ord.id}"/>
                        </s:url>
                        <s:url var="deleteUrl" action="order-delete">
                            <s:param name="id" value="%{#ord.id}"/>
                        </s:url>
                        <a class="btn secondary" href="${editUrl}">Edit</a>
                        <a class="btn danger" href="${deleteUrl}" onclick="return confirm('Delete this order?')">Delete</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        </div>

        <div class="pagination">
            <s:if test="page > 1">
                <s:url var="prevUrl" action="order-list">
                    <s:param name="page" value="%{page - 1}"/>
                    <s:param name="size" value="%{size}"/>
                </s:url>
                <a href="${prevUrl}">Previous</a>
            </s:if>
            <span>Page <s:property value="page"/> of <s:property value="totalPages"/> | <s:property value="totalRecords"/> orders</span>
            <s:if test="page < totalPages">
                <s:url var="nextUrl" action="order-list">
                    <s:param name="page" value="%{page + 1}"/>
                    <s:param name="size" value="%{size}"/>
                </s:url>
                <a href="${nextUrl}">Next</a>
            </s:if>
        </div>
    </div>
</div>
</body>
</html>
