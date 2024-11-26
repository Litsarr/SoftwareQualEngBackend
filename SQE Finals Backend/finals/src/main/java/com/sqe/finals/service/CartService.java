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
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;  // To fetch products

    public Optional<Cart> findBySessionId(UUID sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    public Cart addProductToCart(UUID sessionId, Long productId, String size, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findBySessionId(sessionId).orElse(new Cart());
        cart.setSessionId(sessionId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) && item.getSize().equals(size))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem itemToUpdate = existingItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setSize(size);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateProductQuantity(UUID sessionId, Long productId, String size, int quantity) {
        // Fetch the cart associated with the session ID
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Find the corresponding cart item
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) && item.getSize().equals(size))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Update or remove the cart item based on the quantity
        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem); // Remove from the database if applicable
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem); // Persist changes
        }

        // Save the updated cart
        return cartRepository.save(cart);
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteById(UUID id) {
        cartRepository.deleteById(id);
    }
}



