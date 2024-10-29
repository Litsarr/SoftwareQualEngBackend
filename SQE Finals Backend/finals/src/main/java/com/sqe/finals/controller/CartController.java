package com.sqe.finals.controller;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<Cart> getCartBySessionId(@PathVariable String sessionId) {
        return cartService.findBySessionId(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cart> createOrUpdateCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.save(cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

