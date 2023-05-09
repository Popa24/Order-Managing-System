package org.example.product.view;

import org.example.product.controller.ProductController;
import org.example.product.model.ProductEntity;

import javax.swing.*;
import java.awt.*;

public class UpdateProductDialog extends JDialog {
    private final JTextField productNameField;
    private final JTextField quantityField;
    private final JTextField priceField;
    private boolean saved;

    public UpdateProductDialog(final ProductController productController, final ProductEntity productEntity) {
        setTitle("Update Product");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel formPanel = new JPanel(new GridLayout(3, 3));
        contentPane.add(formPanel, BorderLayout.CENTER);

        formPanel.add(new JLabel("Product Name: "));
        productNameField = new JTextField(productEntity.getProductName());
        formPanel.add(productNameField);

        formPanel.add(new JLabel("Quantity: "));
        quantityField = new JTextField(productEntity.getQuantity());
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Price: "));
        priceField=new JTextField(String.valueOf(productEntity.getPrice()));
        formPanel.add(priceField);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            productEntity.setProductName(productNameField.getText());
            productEntity.setQuantity(Integer.parseInt(quantityField.getText()));
            productEntity.setPrice(Double.parseDouble(priceField.getText()));

            try {
                productController.update(productEntity);
                saved = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            saved = false;
            dispose();
        });
        buttonPanel.add(cancelButton);
    }

    public boolean isSaved() {
        return saved;
    }
}
