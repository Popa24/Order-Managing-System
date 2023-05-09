package org.example.product.service;


import lombok.NonNull;
import org.example.product.model.ProductEntity;
import org.example.product.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(@NonNull final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @NonNull
    public ProductEntity create(ProductEntity productEntity) throws SQLException{
        return productRepository.create(productEntity);
    }
    @NonNull
    public ProductEntity update(ProductEntity productEntity) throws SQLException{
        return productRepository.update(productEntity);
    }

    public void delete(Long id) throws SQLException{
        productRepository.delete(id);
    }
    @NonNull
    public List<ProductEntity> findAll() throws SQLException{
        return productRepository.findAll();
    }
    public ProductEntity findById(Long id) throws SQLException{
        return productRepository.findById(id);
    }
    public List<ProductEntity> findByIds(List<Long> ids) throws SQLException{
        return productRepository.findByIds(ids);
    }
}
