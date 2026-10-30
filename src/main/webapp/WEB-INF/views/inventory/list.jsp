<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Stock Moves</span>
        <h1>Inventory</h1>
        <p>Top up and withdrawal history.</p>
    </div>

    <div class="card">
        <div class="page-header">
            <div>
                <h2 class="section-title">Inventory Transactions</h2>
                <p class="section-note">Stock changes are saved in one pass.</p>
            </div>
            <div class="actions">
                <a class="btn" href="inventory-form.action">New Move</a>
                <a class="btn secondary" href="item-list.action">Items</a>
            </div>
        </div>

        <div class="table-shell">
        <table class="grid">
            <thead>
            <tr>
                <th>ID</th>
                <th>Item</th>
                <th>Qty</th>
                <th>Type</th>
                <th>Note</th>
                <th>Updated</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <s:if test="transactions == null || transactions.isEmpty()">
                <tr>
                    <td colspan="7" class="muted">No stock moves.</td>
                </tr>
            </s:if>
            <s:iterator value="transactions" var="tx">
                <tr>
                    <td><s:property value="#tx.id"/></td>
                    <td><s:property value="#tx.itemName"/></td>
                    <td><s:property value="#tx.qty"/></td>
                    <td>
                        <s:if test="#tx.topUp">
                            <span class="pill success">Top Up</span>
                        </s:if>
                        <s:else>
                            <span class="pill warning">Withdrawal</span>
                        </s:else>
                    </td>
                    <td><s:property value="#tx.note"/></td>
                    <td><s:date name="#tx.updatedAt" format="dd/MM/yyyy HH:mm:ss"/></td>
                    <td>
                        <s:url var="editUrl" action="inventory-edit">
                            <s:param name="id" value="%{#tx.id}"/>
                        </s:url>
                        <s:url var="deleteUrl" action="inventory-delete">
                            <s:param name="id" value="%{#tx.id}"/>
                        </s:url>
                        <a class="btn secondary" href="${editUrl}">Edit</a>
                        <a class="btn danger" href="${deleteUrl}" onclick="return confirm('Delete this inventory transaction?')">Delete</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        </div>

        <div class="pagination">
            <s:if test="page > 1">
                <s:url var="prevUrl" action="inventory-list">
                    <s:param name="page" value="%{page - 1}"/>
                    <s:param name="size" value="%{size}"/>
                </s:url>
                <a href="${prevUrl}">Previous</a>
            </s:if>
            <span>Page <s:property value="page"/> of <s:property value="totalPages"/> | <s:property value="totalRecords"/> moves</span>
            <s:if test="page < totalPages">
                <s:url var="nextUrl" action="inventory-list">
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
