package org.example.databseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection connection = null;

        // Replace these values with your actual database credentials
        String url = "jdbc:postgresql://localhost:5432/tp_tema3";
        String username = "postgres";
        String password = "1234";

        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish a connection to the database
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to the database failed.");
            e.printStackTrace();
        }

        return connection;
    }
}
