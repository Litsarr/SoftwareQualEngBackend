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
        return cartRepository.findBySessionId(sessionId);  // Correct repository method
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
            newItem.setSize(size);  // Set the selected size
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }


    public Cart save(Cart cart) {
        return cartRepository.save(cart);  // Save cart with UUID as sessionId
    }

    public void deleteById(UUID id) {
        cartRepository.deleteById(id);  // Delete cart by UUID sessionId
    }
}


