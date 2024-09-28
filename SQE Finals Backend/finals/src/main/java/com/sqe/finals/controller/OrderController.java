package com.sqe.finals.controller;


import com.sqe.finals.entity.OrderStatus;
import com.sqe.finals.entity.Orders;
import com.sqe.finals.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Orders> getAllOrders() {
        return orderService.findAllOrders();
    }

    @PatchMapping("/{id}/status")
    public void updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
    }
}

