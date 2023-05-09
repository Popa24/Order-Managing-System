package org.example.product.view;

import org.example.product.controller.ProductController;
import org.example.product.model.ProductEntity;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddProductDialog extends JDialog {
    private final JTextField productNameTextField;
    private final JTextField quantityTextField;
    private  final JTextField priceTextField;

    private final ProductController productController;
    private boolean isSaved = false;

    public AddProductDialog(ProductController productController) {
        this.productController = productController;

        setTitle("Add Product");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 300, 200);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 3));
        contentPane.add(fieldsPanel, BorderLayout.CENTER);

        fieldsPanel.add(new JLabel("Product Name:"));
        productNameTextField = new JTextField();
        fieldsPanel.add(productNameTextField);

        fieldsPanel.add(new JLabel("Quantity:"));
        quantityTextField = new JTextField();
        fieldsPanel.add(quantityTextField);

        fieldsPanel.add(new JLabel("Product Price:"));
        priceTextField=new JTextField();
        fieldsPanel.add(priceTextField);

        JPanel buttonsPanel = new JPanel();
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> createProduct());
        buttonsPanel.add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
    }

    private void createProduct() {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setProductName(productNameTextField.getText());
        productEntity.setQuantity(Integer.parseInt(quantityTextField.getText()));
        productEntity.setPrice((double) Integer.parseInt(priceTextField.getText()));

        try {
            productController.create(productEntity);
            isSaved = true;
            dispose();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Error creating product: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}