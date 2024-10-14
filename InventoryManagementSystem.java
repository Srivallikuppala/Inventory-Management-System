import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InventoryManagementSystem extends JFrame {
    // Database connection details
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/InventoryDB";  // Change for SQLite
    static final String JDBC_USER = "root";
    static final String JDBC_PASSWORD = "Pavani_55";  // Update your MySQL password

    // Swing components
    private JTextField productCodeField, productNameField, quantityField, priceField;
    private JTextArea displayArea;

    public InventoryManagementSystem() {
        // GUI Setup
        setTitle("Inventory Management System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Fields and Buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 4));

        inputPanel.add(new JLabel("Product Code:"));
        productCodeField = new JTextField();
        inputPanel.add(productCodeField);

        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        inputPanel.add(productNameField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        // Buttons for actions
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addProduct());
        inputPanel.add(addButton);

        JButton updateButton = new JButton("Update Product");
        updateButton.addActionListener(e -> updateProduct());
        inputPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> deleteProduct());
        inputPanel.add(deleteButton);

        JButton viewButton = new JButton("View Products");
        viewButton.addActionListener(e -> viewProducts());
        inputPanel.add(viewButton);

        add(inputPanel, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setVisible(true);
    }

    // Add a new product
    private void addProduct() {
        String productCode = productCodeField.getText();
        String productName = productNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO products (productCode, productName, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, productCode);
                pstmt.setString(2, productName);
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, price);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product.");
        }
    }

    // Update product details
    private void updateProduct() {
        String productCode = productCodeField.getText();
        String productName = productNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "UPDATE products SET productName = ?, quantity = ?, price = ? WHERE productCode = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, productName);
                pstmt.setInt(2, quantity);
                pstmt.setDouble(3, price);
                pstmt.setString(4, productCode);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product.");
        }
    }

    // Delete a product
    private void deleteProduct() {
        String productCode = productCodeField.getText();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "DELETE FROM products WHERE productCode = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, productCode);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product.");
        }
    }

    // View all products
    private void viewProducts() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM products";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                displayArea.setText("");
                while (rs.next()) {
                    String productDetails = String.format("Code: %s, Name: %s, Quantity: %d, Price: %.2f\n",
                            rs.getString("productCode"), rs.getString("productName"), rs.getInt("quantity"), rs.getDouble("price"));
                    displayArea.append(productDetails);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving products.");
        }
    }

    public static void main(String[] args) {
        // Ensure the JDBC driver is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // For MySQL
            // Class.forName("org.sqlite.JDBC");  // For SQLite
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Run the Swing GUI
        SwingUtilities.invokeLater(InventoryManagementSystem::new);
    }
}
