package org.example.order.view;

import org.example.client.controller.ClientController;
import org.example.client.model.ClientEntity;
import org.example.client.repository.ClientRepository;
import org.example.client.service.ClientService;
import org.example.databseconnection.DatabaseConnection;
import org.example.order.controller.OrderController;
import org.example.order.model.OrderEntity;
import org.example.product.controller.ProductController;
import org.example.product.model.ProductEntity;
import org.example.product.repository.ProductRepository;
import org.example.product.service.ProductService;
import org.example.product.view.ProductWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpdateOrderDialog extends JDialog {
    private final JTextField clientId;
    private final JTextField productId;
    private final JTextField productQuantities;
    private boolean saved;

    public UpdateOrderDialog(final OrderController orderController, final OrderEntity orderEntity) {
        setTitle("Update Order");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        contentPane.add(formPanel, BorderLayout.CENTER);

        formPanel.add(new JLabel("Client Id: "));
        clientId = new JTextField(Long.toString(orderEntity.getClient().getId()));
        formPanel.add(clientId);

        formPanel.add(new JLabel("Product Ids:"));
        productId = new JTextField(joinProductIds(orderEntity.getProducts()));
        formPanel.add(productId);

        formPanel.add(new JLabel("Product Quantities:"));
        productQuantities = new JTextField(joinProductQuantities(orderEntity.getProductQuantities()));

        formPanel.add(productQuantities);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            // Create a database connection
            Connection connection = DatabaseConnection.getConnection();

            // Initialize the ClientRepository
            ClientRepository clientRepository = new ClientRepository(connection);

            // Initialize the ClientService
            ClientService clientService = new ClientService(clientRepository);

            // Initialize the ClientController
            ClientController clientController = new ClientController(clientService);
            ClientEntity clientEntity;
            try {
                clientEntity = clientController.findById(Long.valueOf(clientId.getText()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
          //  orderEntity.setClientId(Long.valueOf(clientId.getText()));
            orderEntity.setClient(clientEntity);
            List<ProductEntity> products = parseProducts(productId.getText());

            List<Integer> newQuantities = parseQuantities(productQuantities.getText());
            List<Integer> oldQuantities = new ArrayList<>(orderEntity.getProductQuantities().values());

            // Add back the original quantities to the products
            try {
                updateProductQuantities(products, oldQuantities, true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate quantity differences
            List<Integer> quantityDifferences = calculateQuantityDifferences(oldQuantities, newQuantities);

            // Validate the new quantities
            if (!validateQuantities(products, quantityDifferences)) {
                // Revert the product quantities back to their updated state if validation fails
                try {
                    updateProductQuantities(products, oldQuantities, false);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }

            orderEntity.setProducts(new HashSet<>(products));
            orderEntity.setProductQuantities(products.stream().collect(Collectors.toMap(Function.identity(), product -> newQuantities.get(products.indexOf(product)))));


            try {
                orderController.update(orderEntity);
                updateProductQuantities(products, newQuantities, false);
                ProductWindow.triggerRefreshProductTable();

                saved = true;
                dispose();
            } catch (Exception ex) {
                try {
                    updateProductQuantities(products, newQuantities, true);
                } catch (SQLException exc) {
                    throw new RuntimeException(exc);
                }
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(saveButton);
        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(e -> {
            try {
                List<ProductEntity> products = parseProducts(productId.getText());
                List<Integer> quantities = parseQuantities(productQuantities.getText());
                updateProductQuantities(products, quantities, true);
                saved = false;
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(cancelButton);
    }

    private String joinProductIds(Set<ProductEntity> products) {
        return products.stream()
                .map(product -> Long.toString(product.getId()))
                .collect(Collectors.joining(","));
    }

    private String joinProductQuantities(Map<ProductEntity, Integer> quantities) {
        return quantities.values().stream()
                .map(Integer::toUnsignedString)
                .collect(Collectors.joining(","));
    }


    private List<ProductEntity> parseProducts(String productsStr) {
        return getProductEntities(productsStr);
    }

    static List<ProductEntity> getProductEntities(String productsStr) {
        List<Long> productIds = Arrays.stream(productsStr.split(","))
                .map(Long::valueOf)
                .toList();
        List<ProductEntity> products = new ArrayList<>();
        for (Long id : productIds) {
            ProductEntity product = new ProductEntity();
            product.setId(id);
            products.add(product);
        }
        return products;
    }

    private List<Integer> parseQuantities(String quantitiesStr) {
        return Arrays.stream(quantitiesStr.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
    void updateProductQuantities(List<ProductEntity> products, List<Integer> quantities, boolean revert) throws SQLException {
        // Create a database connection
        Connection connection = DatabaseConnection.getConnection();

        // Initialize the ProductRepository
        ProductRepository productRepository = new ProductRepository(connection);

        // Initialize the ProductService
        ProductService productService = new ProductService(productRepository);

        // Initialize the ProductController
        ProductController productController = new ProductController(productService);
        for (int i = 0; i < products.size(); i++) {
            ProductEntity product = products.get(i);

            int newQuantity = revert ? product.getQuantity() + quantities.get(i) : product.getQuantity() - quantities.get(i);
            product.setQuantity(newQuantity);
            productController.update(product);
        }

    }
    private boolean validateQuantities(List<ProductEntity> products, List<Integer> quantities) {
        for (int i = 0; i < products.size(); i++) {
            if (quantities.get(i) > products.get(i).getQuantity()) {
                JOptionPane.showMessageDialog(this,
                        "Error: The requested quantity for product with ID " + products.get(i).getId() + " is greater than the available quantity.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
    private List<Integer> calculateQuantityDifferences(List<Integer> oldQuantities, List<Integer> newQuantities) {
        List<Integer> differences = new ArrayList<>();
        for (int i = 0; i < oldQuantities.size(); i++) {
            differences.add(newQuantities.get(i) - oldQuantities.get(i));
        }
        return differences;
    }

    public boolean isSaved() {
        return saved;
    }
}
