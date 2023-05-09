package org.example.client.view;

import org.example.client.controller.ClientController;
import org.example.client.model.ClientEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateClientDialog extends JDialog {
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField cityField;
    private final JTextField streetField;
    private final JTextField streetNoField;
    private boolean saved;

    public UpdateClientDialog(ClientController clientController, ClientEntity clientEntity) {
        setTitle("Update Client");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        contentPane.add(formPanel, BorderLayout.CENTER);

        formPanel.add(new JLabel("Name: "));
        nameField = new JTextField(clientEntity.getName());
        formPanel.add(nameField);

        formPanel.add(new JLabel("Surname: "));
        surnameField = new JTextField(clientEntity.getSurname());
        formPanel.add(surnameField);

        formPanel.add(new JLabel("City: "));
        cityField = new JTextField(clientEntity.getCity());
        formPanel.add(cityField);

        formPanel.add(new JLabel("Street: "));
        streetField = new JTextField(clientEntity.getStreet());
        formPanel.add(streetField);

        formPanel.add(new JLabel("Street No: "));
        streetNoField = new JTextField(clientEntity.getStreetNo());
        formPanel.add(streetNoField);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientEntity.setName(nameField.getText());
                clientEntity.setSurname(surnameField.getText());
                clientEntity.setCity(cityField.getText());
                clientEntity.setStreet(streetField.getText());
                clientEntity.setStreetNo(streetNoField.getText());

                try {
                    clientController.update(clientEntity);
                    saved = true;
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saved = false;
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
    }

    public boolean isSaved() {
        return saved;
    }
}
