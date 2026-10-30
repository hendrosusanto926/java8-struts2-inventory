<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory Studio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="container">
    <div class="topbar">
        <div class="brand">
            <div class="brand-title">Inventory Studio</div>
            <div class="brand-subtitle">Java 8 Struts 2</div>
        </div>
        <div class="nav-links">
            <a href="item-list.action">Item</a>
            <a href="inventory-list.action">Inventory</a>
            <a href="order-list.action">Order</a>
            <a href="report-list.action">Report</a>
        </div>
    </div>

    <div class="hero">
        <div class="hero-panel">
            <span class="eyebrow">Java 8 / Struts 2 / SQL Server</span>
            <h1>Inventory, orders, and reports.</h1>
            <p>Simple screens for the test task.</p>
            <div class="hero-actions">
                <a class="btn ghost" href="item-list.action">Items</a>
                <a class="btn ghost" href="report-list.action">Report</a>
            </div>
        </div>

        <div class="hero-rail">
            <div class="metric-card">
                <div class="label">Screens</div>
                <p class="value">4</p>
                <div class="hint">Item, inventory, order, report.</div>
            </div>
            <div class="metric-card">
                <div class="label">Stock</div>
                <p class="value">Safe</p>
                <div class="hint">Checks stock before save.</div>
            </div>
            <div class="metric-card">
                <div class="label">Stack</div>
                <p class="value">Java 8</p>
                <div class="hint">Struts 2, JSP, SQL Server.</div>
            </div>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <div class="label">Items</div>
            <div class="value">Master</div>
            <div class="caption">Item list and stock.</div>
        </div>
        <div class="summary-card alt">
            <div class="label">Inventory</div>
            <div class="value">T / W</div>
            <div class="caption">Top up and withdrawal.</div>
        </div>
        <div class="summary-card warn">
            <div class="label">Report</div>
            <div class="value">Totals</div>
            <div class="caption">Orders, inventory, revenue.</div>
        </div>
    </div>

    <div class="footer-note">Use the screens above for the submission screenshots.</div>
</div>
</body>
</html>
