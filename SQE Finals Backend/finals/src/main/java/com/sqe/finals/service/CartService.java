package com.sqe.finals.service;

import com.sqe.finals.entity.Cart;
import com.sqe.finals.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Optional<Cart> findBySessionId(String sessionId) {
        return cartRepository.findById(Long.parseLong(sessionId));
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}

