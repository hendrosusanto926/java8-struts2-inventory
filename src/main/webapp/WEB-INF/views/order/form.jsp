<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <%@ include file="/WEB-INF/views/common/pageHeader.jspf" %>
    <div class="page-hero">
        <span class="eyebrow">Order</span>
        <h1><s:if test="order.id == null">Create Order</s:if><s:else>Edit Order</s:else></h1>
        <p>Pick item, qty, save.</p>
    </div>

    <div class="form-shell">
        <s:if test="order.id == null">
            <form action="order-save.action" method="post">
        </s:if>
        <s:else>
            <form action="order-update.action" method="post">
                <s:hidden name="order.id"/>
        </s:else>

            <div class="form-grid">
                <div class="field">
                    <label>Order No</label>
                    <s:if test="order.id != null">
                        <div class="muted"><s:property value="order.orderNo"/></div>
                    </s:if>
                    <s:else>
                        <div class="muted">Generated automatically when saved.</div>
                    </s:else>
                </div>
                <div class="field">
                    <label>Item</label>
                    <s:select name="order.itemId" list="itemOptions" listKey="id" listValue="name" headerKey="" headerValue="-- Select Item --" theme="simple"/>
                </div>
                <div class="field">
                    <label>Quantity</label>
                    <s:textfield name="order.qty" theme="simple"/>
                </div>
            </div>

            <div class="actions">
                <s:submit value="Save" cssClass="btn" theme="simple"/>
                <a class="btn secondary" href="order-list.action">Back</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
