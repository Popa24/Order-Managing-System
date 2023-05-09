package org.example.product.controller;

import lombok.NonNull;
import org.example.product.model.ProductEntity;
import org.example.product.service.ProductService;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private final ProductService productService;

    public ProductController(@NonNull final ProductService productService) {
        this.productService = productService;
    }
    @NonNull
    public ProductEntity create(ProductEntity productEntity) throws SQLException {
        return productService.create(productEntity);
    }
    @NonNull
    public ProductEntity update(ProductEntity productEntity) throws SQLException{
        return productService.update(productEntity);
    }

    public void delete(Long id) throws SQLException{
        productService.delete(id);
    }
    @NonNull
    public List<ProductEntity> findAll() throws SQLException{
        return productService.findAll();
    }
    public ProductEntity findById(Long id) throws SQLException{
        return productService.findById(id);
    }
    public List<ProductEntity> findByIds(List<Long> ids) throws SQLException{
        return productService.findByIds(ids);
    }
}
