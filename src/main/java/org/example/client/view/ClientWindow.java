package org.example.client.view;

import org.example.client.controller.ClientController;
import org.example.client.model.ClientEntity;
import org.example.client.repository.ClientRepository;
import org.example.client.service.ClientService;
import org.example.databseconnection.DatabaseConnection;
import org.example.databseconnection.TableUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientWindow extends JFrame {
    private final JTable clientTable;

    // Create a database connection
    Connection connection = DatabaseConnection.getConnection();

    // Initialize the ClientRepository
    ClientRepository clientRepository = new ClientRepository(connection);

    // Initialize the ClientService
    ClientService clientService = new ClientService(clientRepository);

    // Initialize the ClientController
    ClientController clientController = new ClientController(clientService);

    public ClientWindow() {
        setTitle("Client Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        clientTable = new JTable();
        refreshClientTable();
        contentPane.add(new JScrollPane(clientTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Client");
        addButton.addActionListener(e -> {
            AddClientDialog dialog = new AddClientDialog(clientController);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshClientTable();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Client");
        updateButton.addActionListener(e -> {
            int selectedIndex = clientTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long clientId = (Long) clientTable.getValueAt(selectedIndex, 0);
                try {
                    ClientEntity clientEntity = clientController.findById(clientId);
                    UpdateClientDialog dialog = new UpdateClientDialog(clientController, clientEntity);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        refreshClientTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a client to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Client");
        deleteButton.addActionListener(e -> {
            int selectedIndex = clientTable.getSelectedRow();
            if (selectedIndex >= 0) {
                Long clientId = (Long) clientTable.getValueAt(selectedIndex, 0);
                try {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected client?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        clientController.delete(clientId);
                        refreshClientTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a client to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(deleteButton);
    }

    private void refreshClientTable() {
        try {
            List<ClientEntity> clients = clientController.findAll();
            clientTable.setModel(TableUtils.createTableModel(clients, ClientEntity.class));
            TableUtils.styleTable(clientTable);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
