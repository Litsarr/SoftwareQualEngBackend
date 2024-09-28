package com.sqe.finals.service;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.entity.CartItem;
import com.sqe.finals.entity.Product;
import com.sqe.finals.repository.CartItemRepository;
import com.sqe.finals.repository.CartRepository;
import com.sqe.finals.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getOrCreateCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setSessionId(sessionId);
                    return cartRepository.save(cart);
                });
    }

    public Cart addToCart(String sessionId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(sessionId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String sessionId, Long productId) {
        Cart cart = getOrCreateCart(sessionId);
        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    public Cart viewCart(String sessionId) {
        return getOrCreateCart(sessionId);
    }
}

