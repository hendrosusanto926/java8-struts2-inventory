<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Item</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Item</span>
        <h1><s:if test="item.id == null">Create Item</s:if><s:else>Edit Item</s:else></h1>
        <p>Name and price only.</p>
    </div>

    <div class="form-shell">
        <s:if test="item.id == null">
            <form action="item-save.action" method="post">
        </s:if>
        <s:else>
            <form action="item-update.action" method="post">
                <s:hidden name="item.id"/>
        </s:else>

            <div class="form-grid">
                <div class="field">
                    <label>Item Name</label>
                    <s:textfield name="item.name" theme="simple"/>
                </div>
                <div class="field">
                    <label>Price</label>
                    <s:textfield name="item.price" theme="simple"/>
                </div>
                <div class="field">
                    <label>Remaining Stock</label>
                    <div class="muted"><s:property value="item.currentStock"/></div>
                </div>
            </div>

            <div class="actions">
                <s:submit value="Save" cssClass="btn" theme="simple"/>
                <a class="btn secondary" href="item-list.action">Back</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
