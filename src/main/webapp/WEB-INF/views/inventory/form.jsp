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
        <span class="eyebrow">Inventory</span>
        <h1><s:if test="inventory.id == null">Create Inventory Transaction</s:if><s:else>Edit Inventory Transaction</s:else></h1>
        <p>Save a stock move.</p>
    </div>

    <div class="form-shell">
        <s:if test="inventory.id == null">
            <form action="inventory-save.action" method="post">
        </s:if>
        <s:else>
            <form action="inventory-update.action" method="post">
                <s:hidden name="inventory.id"/>
        </s:else>

            <div class="form-grid">
                <div class="field">
                    <label>Item</label>
                    <s:select name="inventory.itemId" list="itemOptions" listKey="id" listValue="name" headerKey="" headerValue="-- Select Item --" theme="simple"/>
                </div>
                <div class="field">
                    <label>Quantity</label>
                    <s:textfield name="inventory.qty" theme="simple"/>
                </div>
                <div class="field">
                    <label>Type</label>
                    <select name="inventory.type">
                        <option value="">-- Select Type --</option>
                        <option value="T" <s:if test="inventory.topUp">selected="selected"</s:if>>Top Up</option>
                        <option value="W" <s:if test="inventory.withdrawal">selected="selected"</s:if>>Withdrawal</option>
                    </select>
                </div>
                <div class="field">
                    <label>Note</label>
                    <s:textarea name="inventory.note" theme="simple" rows="4"/>
                </div>
            </div>

            <div class="actions">
                <s:submit value="Save" cssClass="btn" theme="simple"/>
                <a class="btn secondary" href="inventory-list.action">Back</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
