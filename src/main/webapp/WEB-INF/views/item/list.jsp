<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Items</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Items</span>
        <h1>Item Screen</h1>
        <p>Stock is shown beside each item.</p>
    </div>

    <div class="card">
        <div class="page-header">
            <div>
                <h2 class="section-title">Item Catalog</h2>
                <p class="section-note">List, edit, or remove items.</p>
            </div>
            <div class="actions">
                <a class="btn" href="item-form.action">New Item</a>
                <a class="btn secondary" href="report-list.action">Report</a>
            </div>
        </div>

        <div class="table-shell">
        <table class="grid">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Updated</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <s:if test="items == null || items.isEmpty()">
                <tr>
                    <td colspan="6" class="muted">No items.</td>
                </tr>
            </s:if>
            <s:iterator value="items" var="it">
                <tr>
                    <td><s:property value="#it.id"/></td>
                    <td><s:property value="#it.name"/></td>
                    <td><s:property value="#it.price"/></td>
                    <td>
                        <s:if test="#it.currentStock == 0">
                            <span class="pill danger">Out</span>
                        </s:if>
                        <s:elseif test="#it.currentStock &lt; 10">
                            <span class="pill warning"><s:property value="#it.currentStock"/></span>
                        </s:elseif>
                        <s:else>
                            <span class="pill success"><s:property value="#it.currentStock"/></span>
                        </s:else>
                    </td>
                    <td><s:date name="#it.updatedAt" format="dd/MM/yyyy HH:mm:ss"/></td>
                    <td>
                        <s:url var="editUrl" action="item-edit">
                            <s:param name="id" value="%{#it.id}"/>
                        </s:url>
                        <s:url var="deleteUrl" action="item-delete">
                            <s:param name="id" value="%{#it.id}"/>
                        </s:url>
                        <a class="btn secondary" href="${editUrl}">Edit</a>
                        <a class="btn danger" href="${deleteUrl}" onclick="return confirm('Delete this item?')">Delete</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        </div>

        <div class="pagination">
            <s:if test="page > 1">
                <s:url var="prevUrl" action="item-list">
                    <s:param name="page" value="%{page - 1}"/>
                    <s:param name="size" value="%{size}"/>
                </s:url>
                <a href="${prevUrl}">Previous</a>
            </s:if>
            <span>Page <s:property value="page"/> of <s:property value="totalPages"/> | <s:property value="totalRecords"/> items</span>
            <s:if test="page < totalPages">
                <s:url var="nextUrl" action="item-list">
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
