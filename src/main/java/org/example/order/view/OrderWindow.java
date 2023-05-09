package org.example.order.view;

import org.example.client.controller.ClientController;
import org.example.client.repository.ClientRepository;
import org.example.client.service.ClientService;
import org.example.databseconnection.DatabaseConnection;
import org.example.databseconnection.TableUtils;
import org.example.log.repository.BillRepository;
import org.example.order.controller.OrderController;
import org.example.order.model.OrderEntity;
import org.example.order.repository.OrderRepository;
import org.example.order.service.OrderService;
import org.example.product.model.ProductEntity;
import org.example.product.view.ProductWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderWindow extends JFrame {
    private final JTable orderTable;
    Connection connection = DatabaseConnection.getConnection();
    BillRepository billRepository = new BillRepository(connection);

    OrderRepository orderRepository = new OrderRepository(connection);

    OrderService orderService = new OrderService(orderRepository);

    OrderController orderController = new OrderController(orderService);
    // Create a database connection

    // Initialize the ClientRepository
    ClientRepository clientRepository = new ClientRepository(connection);

    // Initialize the ClientService
    ClientService clientService = new ClientService(clientRepository);

    // Initialize the ClientController
    ClientController clientController = new ClientController(clientService);

    public OrderWindow() {
        setTitle("Order Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        orderTable = new JTable();
        refreshOrderTable();
        contentPane.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Order");
        addButton.addActionListener(e -> {

            AddOrderDialog dialog = new AddOrderDialog(orderController, clientController, billRepository);

            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshOrderTable();
            }
        });

        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Order");
        updateButton.addActionListener(e -> {

            int selectedIndex = orderTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long orderId = (Long) orderTable.getValueAt(selectedIndex, 0);
                try {
                    OrderEntity orderEntity = orderController.findById(orderId);
                    UpdateOrderDialog dialog = new UpdateOrderDialog(orderController, orderEntity);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        refreshOrderTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an order to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Order");
        deleteButton.addActionListener(e -> {
            int selectedIndex = orderTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long orderId = (Long) orderTable.getValueAt(selectedIndex, 0);
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected order?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        OrderEntity orderEntity = orderController.findById(orderId);
                        List<ProductEntity> products = new ArrayList<>(orderEntity.getProducts());
                        List<Integer> quantities = new ArrayList<>(orderEntity.getProductQuantities().values());

                        UpdateOrderDialog updateOrderDialog = new UpdateOrderDialog(orderController, orderEntity);
                        updateOrderDialog.updateProductQuantities(products, quantities, true);

                        orderController.delete(orderId);
                        refreshOrderTable();
                        ProductWindow.triggerRefreshProductTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an order to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });


        buttonPanel.add(deleteButton);
    }

    private void refreshOrderTable() {
        try {
            List<OrderEntity> orders = orderController.findAll();
            orderTable.setModel(TableUtils.createOrderTableModel(orders));
            TableUtils.styleTable(orderTable);  // Add this line
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}