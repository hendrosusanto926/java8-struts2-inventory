# Deployment Guide

## Prerequisites
- Java 8 JDK
- Apache Maven
- SQL Server
- Tomcat 9 or compatible servlet container

## Database Setup
1. Create a database named `java_be_inventory`.
2. Run `src/main/resources/sql/database.sql`.
3. Update `src/main/resources/db.properties` with your SQL Server username and password.

The script creates the schema, loads sample data, adds pagination demo rows, and includes the report query at the end.

## Build
Run:

```bash
mvn package
```

The WAR will be created at `target/java8-struts2-inventory.war`.

## Deploy
1. Copy the WAR to Tomcat `webapps`.
2. Start Tomcat.
3. Open the app in browser and verify these screens:
   - `index.jsp`
   - Item Screen
   - Inventory Screen
   - Order Screen
   - Report Screen

## Screenshot Checklist
- Landing page / home screen
- Item list screen
- Item form screen
- Inventory list screen
- Inventory form screen
- Order list screen
- Order form screen
- Report screen

## Notes
- If order stock is insufficient, the app will show an error message on the form.
- Report data is generated from SQL query against SQL Server.
