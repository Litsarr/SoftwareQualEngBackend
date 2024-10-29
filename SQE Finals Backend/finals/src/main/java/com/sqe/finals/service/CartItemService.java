package com.sqe.finals.service;

import com.sqe.finals.entity.CartItem;
import com.sqe.finals.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
