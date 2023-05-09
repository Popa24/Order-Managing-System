package org.example.order.repository;

import org.example.client.model.ClientEntity;
import org.example.order.model.OrderEntity;
import org.example.product.model.ProductEntity;

import java.sql.*;
import java.util.*;

public class OrderRepository {
    private final Connection connection;

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    public OrderEntity create(OrderEntity orderEntity) throws SQLException {
        String sql = "INSERT INTO \"order\" (client_id) VALUES (?) RETURNING id";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, orderEntity.getClient().getId());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            orderEntity.setId(resultSet.getLong("id"));
        }

        for (ProductEntity product : orderEntity.getProducts()) {
            sql = "INSERT INTO order_product (order_id, product_id) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderEntity.getId());
            statement.setLong(2, product.getId());
            statement.executeUpdate();

            sql = "INSERT INTO order_product_quantity (order_id, product_id, opq_quantity) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderEntity.getId());
            statement.setLong(2, product.getId());
            statement.setInt(3, orderEntity.getProductQuantities().get(product));
            statement.executeUpdate();
        }

        return orderEntity;
    }

    public OrderEntity update(OrderEntity orderEntity) throws SQLException {
        // Update client_id
        String sql = "UPDATE \"order\" SET client_id = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, orderEntity.getClient().getId());
        statement.setLong(2, orderEntity.getId());
        statement.executeUpdate();

        // Delete previous order_product and order_product_quantity entries
        sql = "DELETE FROM order_product WHERE order_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setLong(1, orderEntity.getId());
        statement.executeUpdate();

        sql = "DELETE FROM order_product_quantity WHERE order_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setLong(1, orderEntity.getId());
        statement.executeUpdate();

        // Insert new order_product and order_product_quantity entries
        for (ProductEntity product : orderEntity.getProducts()) {
            sql = "INSERT INTO order_product (order_id, product_id) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderEntity.getId());
            statement.setLong(2, product.getId());
            statement.executeUpdate();

            sql = "INSERT INTO order_product_quantity (order_id, product_id, opq_quantity) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderEntity.getId());
            statement.setLong(2, product.getId());
            statement.setInt(3, orderEntity.getProductQuantities().get(product));
            statement.executeUpdate();
        }

        return orderEntity;
    }

    public void delete(Long id) throws SQLException {
        // Delete order_product and order_product_quantity entries first
        String sql = "DELETE FROM order_product WHERE order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();

        sql = "DELETE FROM order_product_quantity WHERE order_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();
        // Delete the order
        sql = "DELETE FROM \"order\" WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    public List<OrderEntity> findAll() throws SQLException {
        List<OrderEntity> orders = new ArrayList<>();
        String sql = "SELECT * FROM \"order\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(resultSet.getLong("id"));

            Long clientId = resultSet.getLong("client_id");
            orderEntity.setClient(findClientById(clientId));

            orderEntity.setProducts(findProductsByOrderId(orderEntity.getId()));
            orderEntity.setProductQuantities(findProductQuantitiesByOrderId(orderEntity.getId()));

            orders.add(orderEntity);
        }
        return orders;
    }

    public OrderEntity findById(Long id) throws SQLException {
        String sql = "SELECT * FROM \"order\" WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(resultSet.getLong("id"));

            Long clientId = resultSet.getLong("client_id");
            orderEntity.setClient(findClientById(clientId));

            orderEntity.setProducts(findProductsByOrderId(orderEntity.getId()));
            orderEntity.setProductQuantities(findProductQuantitiesByOrderId(orderEntity.getId()));
            return orderEntity;
        } else {
            return null;
        }
    }


                    // Helper methods to fetch related entities
    private ClientEntity findClientById(Long id) throws SQLException {
        String sql = "SELECT * FROM \"client\" WHERE id = ?";
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

    private Set<ProductEntity> findProductsByOrderId(Long orderId) throws SQLException {
        Set<ProductEntity> products = new HashSet<>();
        String sql = "SELECT p.* FROM \"product\" p " +
                "JOIN order_product op ON p.id = op.product_id " +
                "WHERE op.order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, orderId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(resultSet.getLong("id"));
            productEntity.setProductName(resultSet.getString("productName"));
            productEntity.setQuantity(resultSet.getInt("quantity"));
            productEntity.setPrice(resultSet.getDouble("price"));
            products.add(productEntity);
        }

        return products;
    }

    private Map<ProductEntity, Integer> findProductQuantitiesByOrderId(Long orderId) throws SQLException {
        Map<ProductEntity, Integer> productQuantities = new HashMap<>();
        String sql = "SELECT p.*, opq.opq_quantity FROM \"product\" p " +
                "JOIN order_product_quantity opq ON p.id = opq.product_id " +
                "WHERE opq.order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, orderId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(resultSet.getLong("id"));
            productEntity.setProductName(resultSet.getString("productName"));
            productEntity.setQuantity(resultSet.getInt("quantity"));
            productEntity.setPrice(resultSet.getDouble("price"));

            int quantity = resultSet.getInt("opq_quantity");
            productQuantities.put(productEntity, quantity);
        }

        return productQuantities;
    }
}