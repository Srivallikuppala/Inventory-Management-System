-- Create a database
CREATE DATABASE InventoryDB;

-- Use the created database
USE InventoryDB;

-- Create products table
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    productCode VARCHAR(20) UNIQUE NOT NULL,
    productName VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Insert sample data into the products table
INSERT INTO products (productCode, productName, quantity, price) VALUES
('C01', 'Laptop', 10, 5600.50),
('C02', 'Smartphone', 25, 799.99);
