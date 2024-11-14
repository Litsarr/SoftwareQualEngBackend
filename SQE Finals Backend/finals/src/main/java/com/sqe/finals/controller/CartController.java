package com.sqe.finals.controller;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.requests.CartItemRequest;
import com.sqe.finals.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpSession httpSession;

    // Endpoint to fetch the cart of the user based on their UUID/Session ID
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            sessionIdStr = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionIdStr);
        }
        UUID sessionId = UUID.fromString(sessionIdStr);  // Convert to UUID
        return cartService.findBySessionId(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Endpoint to create the cart of the user based on their UUID/Session ID
    @PostMapping
    public ResponseEntity<Cart> createOrUpdateCart(@RequestBody(required = false) Cart cart, HttpSession session) {
        if (cart == null) {
            cart = new Cart();  // Create a new cart if body is missing
        }

        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            sessionIdStr = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionIdStr);
        }
        UUID sessionId = UUID.fromString(sessionIdStr);
        cart.setSessionId(sessionId);

        return ResponseEntity.ok(cartService.save(cart));
    }
    // Endpoint to add items to the cart
    @PostMapping("/addItem")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemRequest cartItemRequest, HttpSession session) {
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            sessionIdStr = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionIdStr);
        }
        UUID sessionId = UUID.fromString(sessionIdStr);
        Cart updatedCart = cartService.addProductToCart(sessionId, cartItemRequest.getProductId(), cartItemRequest.getSize(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }
    // Endpoint to add items to the cart
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID id) {
        cartService.deleteById(id);  // Use UUID for delete
        return ResponseEntity.noContent().build();
    }
}




