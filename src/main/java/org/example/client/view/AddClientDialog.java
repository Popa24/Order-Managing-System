package org.example.client.view;

import org.example.client.controller.ClientController;
import org.example.client.model.ClientEntity;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddClientDialog extends JDialog {
    private final JTextField nameTextField;
    private final JTextField surnameTextField;
    private final JTextField cityTextField;
    private final JTextField streetTextField;
    private final JTextField streetNoTextField;

    private final ClientController clientController;
    private boolean isSaved = false;

    public AddClientDialog(ClientController clientController) {
        this.clientController = clientController;

        setTitle("Add Client");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 300, 250);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2));
        contentPane.add(fieldsPanel, BorderLayout.CENTER);

        fieldsPanel.add(new JLabel("Name:"));
        nameTextField = new JTextField();
        fieldsPanel.add(nameTextField);

        fieldsPanel.add(new JLabel("Surname:"));
        surnameTextField = new JTextField();
        fieldsPanel.add(surnameTextField);

        fieldsPanel.add(new JLabel("City:"));
        cityTextField = new JTextField();
        fieldsPanel.add(cityTextField);

        fieldsPanel.add(new JLabel("Street:"));
        streetTextField = new JTextField();
        fieldsPanel.add(streetTextField);

        fieldsPanel.add(new JLabel("Street No:"));
        streetNoTextField = new JTextField();
        fieldsPanel.add(streetNoTextField);

        JPanel buttonsPanel = new JPanel();
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> createClient());
        buttonsPanel.add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
    }

    private void createClient() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(nameTextField.getText());
        clientEntity.setSurname(surnameTextField.getText());
        clientEntity.setCity(cityTextField.getText());
        clientEntity.setStreet(streetTextField.getText());
        clientEntity.setStreetNo(streetNoTextField.getText());

        try {
            clientController.create(clientEntity);
            isSaved = true;
            dispose();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Error creating client: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
