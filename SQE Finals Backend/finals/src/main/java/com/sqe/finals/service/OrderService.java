package com.sqe.finals.service;


import com.sqe.finals.entity.OrderStatus;
import com.sqe.finals.entity.Orders;
import com.sqe.finals.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Orders> findAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long id, OrderStatus status) {
        Orders order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }
}

