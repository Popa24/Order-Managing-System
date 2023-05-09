package org.example.product.repository;

import org.example.product.model.ProductEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public ProductEntity create(ProductEntity productEntity) throws SQLException {
        String sql = "INSERT INTO product (productName, quantity, price) VALUES (?, ?,?) RETURNING id";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, productEntity.getProductName());
        statement.setLong(2, productEntity.getQuantity());
        statement.setDouble(3,productEntity.getPrice());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            productEntity.setId(resultSet.getLong("id"));
        }
        return productEntity;
    }

    public ProductEntity update(ProductEntity productEntity) throws SQLException {
        String sql = "UPDATE product SET productName = ?, quantity = ?, price=? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, productEntity.getProductName());
        statement.setLong(2, productEntity.getQuantity());
        statement.setDouble(3,productEntity.getPrice());
        statement.setLong(4, productEntity.getId());

        statement.executeUpdate();
        return productEntity;
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    public List<ProductEntity> findAll() throws SQLException {
        List<ProductEntity> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(resultSet.getLong("id"));
            productEntity.setProductName(resultSet.getString("productName"));
            productEntity.setQuantity((int) resultSet.getLong("quantity"));
            productEntity.setPrice(resultSet.getDouble("price"));
            products.add(productEntity);
        }
        return products;
    }
    public ProductEntity findById(Long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
           ProductEntity productEntity=new ProductEntity();
           productEntity.setId(resultSet.getLong("id"));
           productEntity.setProductName(resultSet.getString("productName"));
           productEntity.setQuantity((int) resultSet.getLong("quantity"));
           productEntity.setPrice(resultSet.getDouble("price"));
           return productEntity;
        } else {
            return null;
        }
    }
    public List<ProductEntity> findByIds(List<Long> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = "SELECT * FROM product WHERE id IN (" + ids.stream().map(id -> "?").collect(Collectors.joining(", ")) + ")";
        PreparedStatement statement = connection.prepareStatement(sql);

        int index = 1;
        for (Long id : ids) {
            statement.setLong(index++, id);
        }

        ResultSet resultSet = statement.executeQuery();
        List<ProductEntity> products = new ArrayList<>();

        while (resultSet.next()) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(resultSet.getLong("id"));
            productEntity.setProductName(resultSet.getString("productName"));
            productEntity.setQuantity((int) resultSet.getLong("quantity"));
            productEntity.setPrice(resultSet.getDouble("price"));
            products.add(productEntity);
        }

        return products;
    }

}
