<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sales Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Report</span>
        <h1>Sales Report</h1>
        <p>Totals and revenue by item.</p>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <div class="label">Orders</div>
            <div class="value"><s:property value="reportSummary.totalOrder"/></div>
            <div class="caption">Rows in sales_order.</div>
        </div>
        <div class="summary-card alt">
            <div class="label">Inventory</div>
            <div class="value"><s:property value="reportSummary.totalInventory"/></div>
            <div class="caption">Active stock only.</div>
        </div>
        <div class="summary-card warn">
            <div class="label">Revenue</div>
            <div class="value"><s:property value="reportSummary.totalRevenue"/></div>
            <div class="caption">All order lines.</div>
        </div>
    </div>

    <div class="card">
        <div class="page-header">
            <div>
                <h2 class="section-title">By Item</h2>
                <p class="section-note">Per-item totals.</p>
            </div>
            <div class="actions">
                <a class="btn secondary" href="item-list.action">Items</a>
                <a class="btn secondary" href="inventory-list.action">Inventory</a>
                <a class="btn secondary" href="order-list.action">Orders</a>
            </div>
        </div>

        <div class="table-shell">
        <table class="grid">
            <thead>
            <tr>
                <th>Item ID</th>
                <th>Item</th>
                <th>Stock</th>
                <th>Orders</th>
                <th>Revenue</th>
            </tr>
            </thead>
            <tbody>
            <s:if test="rows == null || rows.isEmpty()">
                <tr>
                    <td colspan="5" class="muted">No report rows.</td>
                </tr>
            </s:if>
            <s:iterator value="rows" var="row">
                <tr>
                    <td><s:property value="#row.itemId"/></td>
                    <td><s:property value="#row.itemName"/></td>
                    <td><s:property value="#row.currentStock"/></td>
                    <td><s:property value="#row.totalOrderQty"/></td>
                    <td><s:property value="#row.totalRevenue"/></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        </div>

        <div class="pagination">
            <s:if test="page > 1">
                <s:url var="prevUrl" action="report-list">
                    <s:param name="page" value="%{page - 1}"/>
                    <s:param name="size" value="%{size}"/>
                </s:url>
                <a href="${prevUrl}">Previous</a>
            </s:if>
            <span>Page <s:property value="page"/> of <s:property value="totalPages"/> | <s:property value="totalRecords"/> items</span>
            <s:if test="page < totalPages">
                <s:url var="nextUrl" action="report-list">
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
