package org.example.order.service;


import lombok.NonNull;
import org.example.order.model.OrderEntity;
import org.example.order.repository.OrderRepository;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(@NonNull final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @NonNull
    public OrderEntity create(OrderEntity orderEntity) throws SQLException {
        return orderRepository.create(orderEntity);
    }
    @NonNull
    public OrderEntity update(OrderEntity orderEntity) throws SQLException {
        return orderRepository.update(orderEntity);
    }

    public void delete(Long id) throws SQLException{
        orderRepository.delete(id);
    }
    @NonNull
    public List<OrderEntity> findAll() throws SQLException{
        return orderRepository.findAll();
    }
    public OrderEntity findById(Long id) throws SQLException{
        return orderRepository.findById(id);
    }

}
