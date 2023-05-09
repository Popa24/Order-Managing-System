package org.example;

import org.example.client.view.ClientWindow;
import org.example.order.view.OrderWindow;
import org.example.product.view.ProductWindow;

import javax.swing.*;
import java.awt.*;

public class MainApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Create the main frame to hold all the child windows
                JFrame mainFrame = new JFrame("Main Application");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setBounds(100, 100, 1200, 800);

                // Set the main frame's layout
                mainFrame.setLayout(new GridLayout(3, 1));

                // Create and add the ClientWindow
                ClientWindow clientWindow = new ClientWindow();
                mainFrame.getContentPane().add(clientWindow.getContentPane());

                // Create and add the OrderWindow
                OrderWindow orderWindow = new OrderWindow();
                mainFrame.getContentPane().add(orderWindow.getContentPane());

                // Create and add the ProductWindow
                ProductWindow productWindow = new ProductWindow();
                mainFrame.getContentPane().add(productWindow.getContentPane());

                // Set the main frame visible
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
