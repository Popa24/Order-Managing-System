package org.example.databseconnection;

import org.example.order.model.OrderEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class TableUtils {

    public static <T> DefaultTableModel createTableModel(List<T> data, Class<T> clazz) {
        DefaultTableModel tableModel = new DefaultTableModel();
        Field[] fields = clazz.getDeclaredFields();

        // Add the column headers
        for (Field field : fields) {
            tableModel.addColumn(field.getName());
        }

        // Add the data
        for (T item : data) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    rowData[i] = fields[i].get(item);
                } catch (IllegalAccessException e) {
                    rowData[i] = null;
                }
            }
            tableModel.addRow(rowData);
        }

        return tableModel;
    }

    public static DefaultTableModel createOrderTableModel(List<OrderEntity> orders) {
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add the column headers
        tableModel.addColumn("Order ID");
        tableModel.addColumn("Client ID");
        tableModel.addColumn("Product IDs");
        tableModel.addColumn("Product Quantities");

        // Add the data
        for (OrderEntity order : orders) {
            Object[] rowData = new Object[4];

            rowData[0] = order.getId();
            rowData[1] = order.getClient().getId();

            // Create a String with Product IDs
            StringBuilder productIds = new StringBuilder();
            order.getProducts().forEach(product -> productIds.append(product.getId()).append(", "));
            if (productIds.length() > 0) {
                productIds.setLength(productIds.length() - 2); // Remove the last comma and space
            }
            rowData[2] = productIds.toString();

            // Create a String with Product Quantities
            StringBuilder productQuantities = new StringBuilder();
            order.getProductQuantities().values().forEach(quantity -> productQuantities.append(quantity).append(", "));
            if (productQuantities.length() > 0) {
                productQuantities.setLength(productQuantities.length() - 2); // Remove the last comma and space
            }
            rowData[3] = productQuantities.toString();

            tableModel.addRow(rowData);
        }

        return tableModel;
    }
    // Method to style the table
    public static void styleTable(JTable table) {
        // Set cell padding
        table.setRowHeight(25);

        // Set header color and font
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 0, 128));
        header.setForeground(Color.white);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));

        // Set row color and font
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);

        // Center align the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set column widths
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
    }
}
