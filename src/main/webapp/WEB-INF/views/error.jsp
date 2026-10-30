<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="compact-page">
<div class="container">
    <div class="page-hero">
        <span class="eyebrow">Application Error</span>
        <h1>Something went wrong</h1>
        <p>The application hit a validation or business-rule error. Use the navigation below to go back safely.</p>
    </div>

    <div class="card">
        <div class="empty-state">
            <h3>We could not complete your request</h3>
            <div class="notice error">
                <s:actionerror/>
            </div>
            <div class="actions">
                <a class="btn" href="item-list.action">Back to Item</a>
                <a class="btn secondary" href="report-list.action">Back to Report</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
