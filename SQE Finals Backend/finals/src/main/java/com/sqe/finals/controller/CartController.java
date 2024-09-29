package com.sqe.finals.controller;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping //Request to view items in the cart
    public Cart viewCart(HttpSession session) {
        String sessionId = session.getId();
        return cartService.viewCart(sessionId);
    }

    @PostMapping("/add")
    @Transactional//API request to add items to the cart
    public Cart addToCart(HttpSession session, @RequestParam Long productId, @RequestParam Integer quantity) {
        String sessionId = session.getId();
        return cartService.addToCart(sessionId, productId, quantity);
    }

    @DeleteMapping("/remove")
    @Transactional//API request to remove items to the cart
    public Cart removeFromCart(HttpSession session, @RequestParam Long productId) {
        String sessionId = session.getId();
        return cartService.removeFromCart(sessionId, productId);
    }
}

