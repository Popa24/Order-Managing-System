package org.example.product.view;

import org.example.databseconnection.DatabaseConnection;
import org.example.databseconnection.TableUtils;
import org.example.product.controller.ProductController;
import org.example.product.model.ProductEntity;
import org.example.product.repository.ProductRepository;
import org.example.product.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductWindow extends JFrame {
    private final JTable productTable;
    private static ProductWindow instance;

    // Create a database connection
    Connection connection = DatabaseConnection.getConnection();

    // Initialize the ProductRepository
    ProductRepository productRepository = new ProductRepository(connection);

    // Initialize the ProductService
    ProductService productService = new ProductService(productRepository);

    // Initialize the ProductController
    ProductController productController = new ProductController(productService);

    public ProductWindow() {
        setTitle("Product Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        instance = this;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        productTable = new JTable();
        refreshProductTable();
        contentPane.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> {
            AddProductDialog dialog = new AddProductDialog(productController);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshProductTable();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Product");
        updateButton.addActionListener(e -> {
            int selectedIndex = productTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long productId = (Long) productTable.getValueAt(selectedIndex, 0);
                ProductEntity productEntity;
                try {
                    productEntity = productController.findById(productId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                UpdateProductDialog dialog = new UpdateProductDialog(productController, productEntity);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    refreshProductTable();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> {
            int selectedIndex = productTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long productId = (Long) productTable.getValueAt(selectedIndex, 0);
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected product?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        productController.delete(productId);
                        refreshProductTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(deleteButton);
    }

    public  void refreshProductTable() {
        try {
            List<ProductEntity> products = productController.findAll();
            productTable.setModel(TableUtils.createTableModel(products, ProductEntity.class));
            TableUtils.styleTable(productTable);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void triggerRefreshProductTable() {
        if (instance != null) {
            instance.refreshProductTable();
        }
    }
}
