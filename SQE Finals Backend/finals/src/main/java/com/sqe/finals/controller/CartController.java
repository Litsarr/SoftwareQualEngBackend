package com.sqe.finals.controller;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.entity.CartItem;
import com.sqe.finals.repository.CartItemRepository;
import com.sqe.finals.repository.CartRepository;
import com.sqe.finals.requests.CartItemRequest;
import com.sqe.finals.service.CartItemService;
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

    @Autowired
    private CartRepository cartRepository;

    // Endpoint to fetch the cart of the user based on their UUID/Session ID
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            sessionIdStr = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionIdStr);
        }
        UUID sessionId = UUID.fromString(sessionIdStr);
        return cartService.findBySessionId(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create or update the cart of the user
    @PostMapping
    public ResponseEntity<Cart> createOrUpdateCart(@RequestBody(required = false) Cart cart, HttpSession session) {
        if (cart == null) {
            cart = new Cart();
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
        Cart updatedCart = cartService.addProductToCart(sessionId, cartItemRequest.getProductId(),
                cartItemRequest.getSize(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    // Endpoint to remove an item from the cart
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartItemId, HttpSession session) {
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            sessionIdStr = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionIdStr);
        }
        UUID sessionId = UUID.fromString(sessionIdStr);

        // Fetch the cart
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Remove the item from the cart
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.getItems().remove(cartItem);

        // Save the updated cart
        Cart updatedCart = cartRepository.save(cart);

        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<Cart> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            HttpSession session) {
        // Ensure the quantity is valid
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        // Fetch session ID from the HttpSession
        String sessionIdStr = (String) session.getAttribute("sessionId");
        if (sessionIdStr == null) {
            return ResponseEntity.badRequest().build(); // Session must exist to update cart items
        }
        UUID sessionId = UUID.fromString(sessionIdStr);

        // Fetch the cart
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Locate the cart item and update its quantity
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);

        // Save the updated cart
        Cart updatedCart = cartRepository.save(cart);

        return ResponseEntity.ok(updatedCart);
    }

}





