package org.example.order.controller;

import lombok.NonNull;
import org.example.order.model.OrderEntity;
import org.example.order.service.OrderService;

import java.sql.SQLException;
import java.util.List;

public class OrderController {
    private final OrderService orderService;

    public OrderController(@NonNull final OrderService orderService) {
        this.orderService = orderService;
    }
    @NonNull
    public OrderEntity create(OrderEntity orderEntity) throws SQLException {
        return orderService.create(orderEntity);
    }
    @NonNull
    public OrderEntity update(OrderEntity orderEntity) throws SQLException {
        return orderService.update(orderEntity);
    }

    public void delete(Long id) throws SQLException{
        orderService.delete(id);
    }
    @NonNull
    public List<OrderEntity> findAll() throws SQLException{
        return orderService.findAll();
    }
    public OrderEntity findById(Long id) throws SQLException{
        return orderService.findById(id);
    }
}
