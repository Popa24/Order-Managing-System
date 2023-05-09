package org.example.client.repository;


import org.example.client.model.ClientEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private final Connection connection;

    public ClientRepository(Connection connection) {
        this.connection = connection;
    }

    public ClientEntity create(ClientEntity clientEntity) throws SQLException {
        String sql = "INSERT INTO client (name, surName,email, city, street, streetNo) VALUES (?, ?,?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, clientEntity.getName());
        statement.setString(2, clientEntity.getSurname());
        statement.setString(3,"alex24popa@gmail.com");
        statement.setString(4, clientEntity.getCity());
        statement.setString(5, clientEntity.getStreet());
        statement.setString(6, clientEntity.getStreetNo());
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            clientEntity.setId(resultSet.getLong(1));
        }
        return clientEntity;
    }


    public ClientEntity update(ClientEntity clientEntity) throws SQLException {
        String sql = "UPDATE client SET name = ?, surName = ?, city = ?, street = ?, streetNo = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, clientEntity.getName());
        statement.setString(2, clientEntity.getSurname());
        statement.setString(3, clientEntity.getCity());
        statement.setString(4, clientEntity.getStreet());
        statement.setString(5, clientEntity.getStreetNo());
        statement.setLong(6, clientEntity.getId());
        statement.executeUpdate();
        return clientEntity;
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    public List<ClientEntity> findAll() throws SQLException {
        List<ClientEntity> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setId(resultSet.getLong("id"));
            clientEntity.setName(resultSet.getString("name"));
            clientEntity.setSurname(resultSet.getString("surName"));
            clientEntity.setEmail(resultSet.getString("email"));
            clientEntity.setCity(resultSet.getString("city"));
            clientEntity.setStreet(resultSet.getString("street"));
            clientEntity.setStreetNo(resultSet.getString("streetNo"));
            clients.add(clientEntity);
        }
        return clients;
    }
    public ClientEntity findById(Long id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setId(resultSet.getLong("id"));
            clientEntity.setName(resultSet.getString("name"));
            clientEntity.setSurname(resultSet.getString("surName"));
            clientEntity.setEmail(resultSet.getString("email"));
            clientEntity.setCity(resultSet.getString("city"));
            clientEntity.setStreet(resultSet.getString("street"));
            clientEntity.setStreetNo(resultSet.getString("streetNo"));
            return clientEntity;
        } else {
            return null;
        }
    }


}
