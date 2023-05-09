package org.example.order.view;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import org.example.client.controller.ClientController;
import org.example.client.model.ClientEntity;
import org.example.databseconnection.DatabaseConnection;
import org.example.log.model.Bill;
import org.example.log.repository.BillRepository;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AddOrderDialog extends JDialog {
    private final JTextField clientId;
    private final JTextField productId;
    private final JTextField productQuantities;

    private final OrderController orderController;
    private final ClientController clientController;
    private final BillRepository billRepository;
    private boolean isSaved = false;

    public AddOrderDialog(OrderController orderController, ClientController clientController, BillRepository billRepository) {
        this.orderController = orderController;
        this.clientController=clientController;
        this.billRepository = billRepository;

        setTitle("Add Order");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 300, 250);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2));
        contentPane.add(fieldsPanel, BorderLayout.CENTER);

        fieldsPanel.add(new JLabel("Client Id: "));
        clientId = new JTextField();
        fieldsPanel.add(clientId);

        fieldsPanel.add(new JLabel("Product Ids: "));
        productId = new JTextField();
        fieldsPanel.add(productId);

        fieldsPanel.add(new JLabel("Product Quantities: "));
        productQuantities = new JTextField();
        fieldsPanel.add(productQuantities);

        JPanel buttonsPanel = new JPanel();
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                createOrder();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonsPanel.add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
    }

    private void createOrder() throws SQLException {
        // Create a database connection
        Connection connection = DatabaseConnection.getConnection();

        // Initialize the ProductRepository
        ProductRepository productRepository = new ProductRepository(connection);

        // Initialize the ProductService
        ProductService productService = new ProductService(productRepository);

        // Initialize the ProductController
        ProductController productController = new ProductController(productService);
        OrderEntity orderEntity = new OrderEntity();
        ClientEntity clientEntity = clientController.findById(Long.valueOf(clientId.getText()));
        orderEntity.setClient(clientEntity);

        List<ProductEntity> products = parseProducts(productId.getText());
        List<Integer> quantities = parseQuantities(productQuantities.getText());
        if (!validateQuantities(products, quantities)) {
            return;
        }
        // Convert the products and quantities lists to a Map<ProductEntity, Integer>
        Map<ProductEntity, Integer> productQuantitiesMap = new HashMap<>();
        for (int i = 0; i < products.size(); i++) {
            productQuantitiesMap.put(products.get(i), quantities.get(i));
        }
        double totalCost = calculateTotalCost(products, quantities);
        orderEntity.setProducts(new HashSet<>(products));
        orderEntity.setProductQuantities(productQuantitiesMap);

        try {
            OrderEntity createdOrder = orderController.create(orderEntity);
            Bill bill = new Bill(createdOrder.getId(), createdOrder.getClient().getId(), totalCost, LocalDateTime.now());
            billRepository.save(bill);
            updateProductQuantities(products, quantities, false);
            ProductWindow.triggerRefreshProductTable();

            isSaved = true;

            dispose();
            sendOrderEmail(clientEntity, orderEntity, bill, products, quantities);

        } catch (SQLException exception) {
            updateProductQuantities(products, quantities, true);
            JOptionPane.showMessageDialog(this, "Error creating order: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<ProductEntity> parseProducts(String productsStr) throws SQLException {
        // Create a database connection
        Connection connection = DatabaseConnection.getConnection();

        // Initialize the ProductRepository
        ProductRepository productRepository = new ProductRepository(connection);

        // Initialize the ProductService
        ProductService productService = new ProductService(productRepository);

        // Initialize the ProductController
        ProductController productController = new ProductController(productService);
        // Assuming the product IDs are comma-separated, you can split the string and map each ID to a ProductEntity
        List<Long> productIds = Arrays.stream(productsStr.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // Replace this with the actual method to fetch products from your data source
        productController.findByIds(productIds);

        return productController.findByIds(productIds);
    }
    private double calculateTotalCost(List<ProductEntity> products, List<Integer> quantities) {
        double totalCost = 0.0;
        for (int i = 0; i < products.size(); i++) {
            totalCost += products.get(i).getPrice() * quantities.get(i);
        }
        return totalCost;
    }
    private List<Integer> parseQuantities(String quantitiesStr) {
        return Arrays.stream(quantitiesStr.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
    private void updateProductQuantities(List<ProductEntity> products, List<Integer> quantities, boolean revert) throws SQLException {
        // Create a database connection
        Connection connection = DatabaseConnection.getConnection();

        // Initialize the ProductRepository
        ProductRepository productRepository = new ProductRepository(connection);

        // Initialize the ProductService
        ProductService productService = new ProductService(productRepository);

        // Initialize the ProductController
        ProductController productController = new ProductController(productService);
        for (int i = 0; i < products.size(); i++) {
            ProductEntity product;

                product = products.get(i);

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
    private void sendOrderEmail(ClientEntity client, OrderEntity order, Bill bill, List<ProductEntity> products, List<Integer> quantities) throws SQLException {
        StringBuilder productInfo = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            productInfo.append(products.get(i).getProductName())
                    .append(" x ")
                    .append(quantities.get(i))
                    .append(" with the price: ")
                    .append(products.get(i).getPrice())
                    .append("\n");
        }

        String subject = "Your order receipt";
        String body = "Hi " + client.getName() + ",\n" +
                "The order: " + order.getId() + " containing:\n" +
                productInfo +
                "Amounting to a total of: " +billRepository.findByOrderId(order.getId()).totalAmount()  + ".\n" +
                "Will be delivered in 3-5 working days from the moment you receive this email.\n" +
                "With sincerity, PopaIndustries.";

        Message message = Message.builder()
                .from(client.getEmail()) // Replace with your email address
                .to(client.getEmail())
                .subject(subject)
                .text(body) // Use text instead of html for the plain text email content
                .build();

        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config("c4802ebd871393ebe6575f0a31523620-181449aa-5212a2d7") // Replace with your Mailgun API key
                .createApi(MailgunMessagesApi.class);

        MessageResponse response = mailgunMessagesApi.sendMessage("sandboxe65fdf3087464f3ca217c1783cadc4be.mailgun.org", message); // Replace with your Mailgun domain
    }

    public boolean isSaved() {
        return isSaved;
    }
}
