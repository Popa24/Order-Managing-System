package org.example.log.repository;



import org.example.log.model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BillRepository {
    private final Connection connection;

    public BillRepository(Connection connection) {
        this.connection = connection;
    }

    public Bill save(Bill bill) throws SQLException {
        String sql = "INSERT INTO log (order_id, client_id, total_amount, timestamp) VALUES (?, ?, ?, ?) RETURNING id;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bill.orderId());
            statement.setLong(2, bill.clientId());
            statement.setDouble(3, bill.totalAmount());
            statement.setObject(4, bill.timestamp());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return new Bill( bill.orderId(), bill.clientId(), bill.totalAmount(), bill.timestamp());
            } else {
                throw new SQLException("Failed to insert the bill");
            }
        }
    }

    public Bill findByOrderId(long orderId) throws SQLException {
        String sql = "SELECT * FROM Log WHERE order_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                long clientId = resultSet.getLong("client_id");
                double totalAmount = resultSet.getDouble("total_amount");
                LocalDateTime timestamp = resultSet.getObject("timestamp", LocalDateTime.class);

                return new Bill( orderId, clientId, totalAmount, timestamp);
            } else {
                return null;
            }
        }
    }
}
