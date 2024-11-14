package com.sqe.finals.controller;

import com.sqe.finals.entity.Orders;
import com.sqe.finals.entity.OrderRequestDTO;
import com.sqe.finals.entity.OrderResponseDTO;
import com.sqe.finals.entity.Orders;
import com.sqe.finals.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    //Endpoint to check out items in the cart
    @PostMapping("/checkout")
    public ResponseEntity<Orders> checkout(@RequestBody OrderRequestDTO checkoutRequest, HttpSession session) {
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            throw new RuntimeException("Session ID not found.");
        }
        UUID sessionId = UUID.fromString(sessionIdStr);

        Orders order = orderService.checkout(sessionId, checkoutRequest);
        return ResponseEntity.ok(order);
    }

    //Endpoint to fetch recent orders ADMIN-only endpoint
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getRecentOrders() {
        List<OrderResponseDTO> recentOrders = orderService.getRecentOrders();
        return ResponseEntity.ok(recentOrders);
    }
}


