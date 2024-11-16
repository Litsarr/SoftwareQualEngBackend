package com.sqe.finals.controller;

import com.sqe.finals.entity.*;
import com.sqe.finals.entity.Orders;
import com.sqe.finals.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Endpoint to mark an order as completed (ADMIN-only)
    @PostMapping("/complete/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        try {
            orderService.completeOrder(orderId);
            return ResponseEntity.ok("Order completed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint to delete an order (ADMIN-only)
    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint to fetch completed orders ADMIN-only endpoint
    @GetMapping("/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompletedOrderResponseDTO>> getCompletedOrders() {
        List<CompletedOrderResponseDTO> completedOrders = orderService.getCompletedOrders();
        return ResponseEntity.ok(completedOrders);
    }


}


