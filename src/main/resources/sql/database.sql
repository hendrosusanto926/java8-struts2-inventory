IF DB_ID('java_be_inventory') IS NULL
BEGIN
    CREATE DATABASE java_be_inventory;
END
GO

USE java_be_inventory;
GO

IF OBJECT_ID('dbo.sales_order', 'U') IS NOT NULL DROP TABLE dbo.sales_order;
IF OBJECT_ID('dbo.inventory_transaction', 'U') IS NOT NULL DROP TABLE dbo.inventory_transaction;
IF OBJECT_ID('dbo.item', 'U') IS NOT NULL DROP TABLE dbo.item;
GO

CREATE TABLE dbo.item (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    current_stock INT NOT NULL CONSTRAINT df_item_current_stock DEFAULT 0,
    is_deleted BIT NOT NULL CONSTRAINT df_item_is_deleted DEFAULT 0,
    created_at DATETIME2 NOT NULL CONSTRAINT df_item_created_at DEFAULT SYSDATETIME(),
    updated_at DATETIME2 NOT NULL CONSTRAINT df_item_updated_at DEFAULT SYSDATETIME()
);

CREATE TABLE dbo.inventory_transaction (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    item_id BIGINT NOT NULL,
    qty INT NOT NULL,
    type CHAR(1) NOT NULL,
    note NVARCHAR(255) NULL,
    created_at DATETIME2 NOT NULL CONSTRAINT df_inventory_transaction_created_at DEFAULT SYSDATETIME(),
    updated_at DATETIME2 NOT NULL CONSTRAINT df_inventory_transaction_updated_at DEFAULT SYSDATETIME(),
    CONSTRAINT fk_inventory_transaction_item FOREIGN KEY (item_id) REFERENCES dbo.item(id),
    CONSTRAINT ck_inventory_transaction_type CHECK (type IN ('T', 'W')),
    CONSTRAINT ck_inventory_transaction_qty CHECK (qty > 0)
);

CREATE TABLE dbo.sales_order (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    order_no NVARCHAR(20) NOT NULL,
    item_id BIGINT NOT NULL,
    qty INT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    total_amount DECIMAL(18,2) NOT NULL,
    created_at DATETIME2 NOT NULL CONSTRAINT df_sales_order_created_at DEFAULT SYSDATETIME(),
    updated_at DATETIME2 NOT NULL CONSTRAINT df_sales_order_updated_at DEFAULT SYSDATETIME(),
    CONSTRAINT uq_sales_order_order_no UNIQUE (order_no),
    CONSTRAINT fk_sales_order_item FOREIGN KEY (item_id) REFERENCES dbo.item(id),
    CONSTRAINT ck_sales_order_qty CHECK (qty > 0),
    CONSTRAINT ck_sales_order_total CHECK (total_amount >= 0)
);
GO

SET IDENTITY_INSERT dbo.item ON;
INSERT INTO dbo.item (id, name, price, current_stock, is_deleted, created_at, updated_at) VALUES
(1, N'Pen', 5.00, 5, 0, SYSDATETIME(), SYSDATETIME()),
(2, N'Book', 10.00, 10, 0, SYSDATETIME(), SYSDATETIME()),
(3, N'Bag', 30.00, 30, 0, SYSDATETIME(), SYSDATETIME()),
(4, N'Pencil', 3.00, 3, 0, SYSDATETIME(), SYSDATETIME()),
(5, N'Shoe', 45.00, 35, 0, SYSDATETIME(), SYSDATETIME()),
(6, N'Box', 5.00, 5, 0, SYSDATETIME(), SYSDATETIME()),
(7, N'Cap', 25.00, 25, 0, SYSDATETIME(), SYSDATETIME()),
(8, N'Notebook', 12.00, 20, 0, SYSDATETIME(), SYSDATETIME()),
(9, N'Marker', 8.00, 18, 0, SYSDATETIME(), SYSDATETIME()),
(10, N'Eraser', 4.00, 40, 0, SYSDATETIME(), SYSDATETIME()),
(11, N'Ruler', 6.00, 22, 0, SYSDATETIME(), SYSDATETIME()),
(12, N'Glue', 7.00, 15, 0, SYSDATETIME(), SYSDATETIME()),
(13, N'Tape', 9.00, 16, 0, SYSDATETIME(), SYSDATETIME()),
(14, N'Folder', 11.00, 14, 0, SYSDATETIME(), SYSDATETIME()),
(15, N'Bottle', 15.00, 28, 0, SYSDATETIME(), SYSDATETIME()),
(16, N'Charger', 120.00, 9, 0, SYSDATETIME(), SYSDATETIME()),
(17, N'Mouse', 60.00, 11, 0, SYSDATETIME(), SYSDATETIME()),
(18, N'Keyboard', 150.00, 7, 0, SYSDATETIME(), SYSDATETIME()),
(19, N'Lamp', 85.00, 6, 0, SYSDATETIME(), SYSDATETIME()),
(20, N'Backpack', 70.00, 13, 0, SYSDATETIME(), SYSDATETIME()),
(21, N'Sticker', 2.00, 50, 0, SYSDATETIME(), SYSDATETIME()),
(22, N'Sharpener', 3.00, 26, 0, SYSDATETIME(), SYSDATETIME());
SET IDENTITY_INSERT dbo.item OFF;
GO

SET IDENTITY_INSERT dbo.inventory_transaction ON;
INSERT INTO dbo.inventory_transaction (id, item_id, qty, type, note, created_at, updated_at) VALUES
(1, 1, 5, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(2, 2, 10, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(3, 3, 30, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(4, 4, 3, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(5, 5, 45, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(6, 6, 5, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(7, 7, 25, 'T', N'Initial top up', SYSDATETIME(), SYSDATETIME()),
(8, 5, 10, 'W', N'Sample withdrawal', SYSDATETIME(), SYSDATETIME()),
(9, 8, 20, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(10, 9, 18, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(11, 10, 40, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(12, 11, 22, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(13, 12, 15, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(14, 13, 16, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(15, 14, 14, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(16, 15, 28, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(17, 16, 9, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(18, 17, 11, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(19, 18, 7, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(20, 19, 6, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(21, 20, 13, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(22, 21, 50, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME()),
(23, 22, 26, 'T', N'Pagination seed top up', SYSDATETIME(), SYSDATETIME());
SET IDENTITY_INSERT dbo.inventory_transaction OFF;
GO

SET IDENTITY_INSERT dbo.sales_order ON;
INSERT INTO dbo.sales_order (id, order_no, item_id, qty, unit_price, total_amount, created_at, updated_at) VALUES
(1, N'O1', 1, 2, 5.00, 10.00, SYSDATETIME(), SYSDATETIME()),
(2, N'O2', 2, 3, 10.00, 30.00, SYSDATETIME(), SYSDATETIME()),
(3, N'O3', 5, 4, 45.00, 180.00, SYSDATETIME(), SYSDATETIME()),
(4, N'O4', 4, 1, 3.00, 3.00, SYSDATETIME(), SYSDATETIME()),
(5, N'O5', 5, 2, 45.00, 90.00, SYSDATETIME(), SYSDATETIME()),
(6, N'O6', 6, 3, 5.00, 15.00, SYSDATETIME(), SYSDATETIME()),
(7, N'O7', 1, 5, 5.00, 25.00, SYSDATETIME(), SYSDATETIME()),
(8, N'O8', 2, 4, 10.00, 40.00, SYSDATETIME(), SYSDATETIME()),
(9, N'O9', 3, 2, 30.00, 60.00, SYSDATETIME(), SYSDATETIME()),
(10, N'O10', 4, 3, 3.00, 9.00, SYSDATETIME(), SYSDATETIME()),
(11, N'O11', 8, 2, 12.00, 24.00, SYSDATETIME(), SYSDATETIME()),
(12, N'O12', 9, 1, 8.00, 8.00, SYSDATETIME(), SYSDATETIME()),
(13, N'O13', 10, 4, 4.00, 16.00, SYSDATETIME(), SYSDATETIME()),
(14, N'O14', 11, 3, 6.00, 18.00, SYSDATETIME(), SYSDATETIME()),
(15, N'O15', 12, 2, 7.00, 14.00, SYSDATETIME(), SYSDATETIME()),
(16, N'O16', 13, 1, 9.00, 9.00, SYSDATETIME(), SYSDATETIME()),
(17, N'O17', 14, 5, 11.00, 55.00, SYSDATETIME(), SYSDATETIME()),
(18, N'O18', 15, 2, 15.00, 30.00, SYSDATETIME(), SYSDATETIME()),
(19, N'O19', 16, 1, 120.00, 120.00, SYSDATETIME(), SYSDATETIME()),
(20, N'O20', 17, 2, 60.00, 120.00, SYSDATETIME(), SYSDATETIME()),
(21, N'O21', 18, 1, 150.00, 150.00, SYSDATETIME(), SYSDATETIME()),
(22, N'O22', 19, 1, 85.00, 85.00, SYSDATETIME(), SYSDATETIME()),
(23, N'O23', 20, 1, 70.00, 70.00, SYSDATETIME(), SYSDATETIME()),
(24, N'O24', 21, 3, 2.00, 6.00, SYSDATETIME(), SYSDATETIME()),
(25, N'O25', 22, 4, 3.00, 12.00, SYSDATETIME(), SYSDATETIME());
SET IDENTITY_INSERT dbo.sales_order OFF;
GO

SELECT
    i.id AS item_id,
    i.name AS item_name,
    i.current_stock,
    COALESCE(SUM(o.qty), 0) AS total_order_qty,
    COALESCE(SUM(o.total_amount), 0) AS total_revenue
FROM dbo.item i
LEFT JOIN dbo.sales_order o ON o.item_id = i.id
WHERE i.is_deleted = 0
GROUP BY i.id, i.name, i.current_stock
ORDER BY i.id;

SELECT COUNT(*) AS total_order FROM dbo.sales_order;

SELECT COALESCE(SUM(current_stock), 0) AS total_inventory
FROM dbo.item
WHERE is_deleted = 0;
