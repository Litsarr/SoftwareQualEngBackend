package com.sqe.finals.controller;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // Request to view items in the cart
    @GetMapping
    public Cart viewCart(HttpSession session) {
        String sessionId = session.getId();
        return cartService.viewCart(sessionId);
    }

    // API request to add items to the cart
    @PostMapping("/add")
    @Transactional
    public Cart addToCart(HttpSession session, @RequestParam Long productId, @RequestParam Integer quantity) {
        String sessionId = session.getId();
        return cartService.addToCart(sessionId, productId, quantity);
    }

    // API request to remove items from the cart
    @DeleteMapping("/remove")
    @Transactional
    public Cart removeFromCart(HttpSession session, @RequestParam Long productId) {
        String sessionId = session.getId();
        return cartService.removeFromCart(sessionId, productId);
    }
}

