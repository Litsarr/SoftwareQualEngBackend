package com.sqe.finals.service;

import com.sqe.finals.entity.*;
import com.sqe.finals.repository.CartRepository;
import com.sqe.finals.repository.OrderRepository;
import com.sqe.finals.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    public Orders checkout(UUID sessionId, OrderRequestDTO checkoutRequest) {
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> itemsToCheckout = cart.getItems().stream()
                .filter(item -> checkoutRequest.getCheckedCartItemIds().contains(item.getId()))
                .collect(Collectors.toList());

        Orders order = new Orders();
        order.setSessionId(sessionId);
        order.setContactInfo(checkoutRequest.getContactInfo());
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(checkoutRequest.getCustomerName());
        order.setAddress(checkoutRequest.getAddress());
        order.setPostalCode(checkoutRequest.getPostalCode());

        double totalAmount = 0.0;
        for (CartItem cartItem : itemsToCheckout) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();

            // Update product quantity based on size
            Product product = cartItem.getProduct();
            Map<String, Integer> sizes = product.getSizes();
            sizes.put(cartItem.getSize(), sizes.get(cartItem.getSize()) - cartItem.getQuantity());
            product.setSizes(sizes);  // Make sure the updated sizes map is set back to the product
            productRepository.save(product);

        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Remove checked-out items from cart
        for (CartItem cartItem : itemsToCheckout) {
            cartItem.setCart(null);  // Disconnect the CartItem from the Cart
        }

        cart.getItems().removeAll(itemsToCheckout);  // Remove items from the Cart list
        cartRepository.save(cart);  // Save the updated Cart

        return order;
    }

    public List<OrderResponseDTO> getRecentOrders() {
        List<Orders> recentOrders = orderRepository.findTop10ByOrderByOrderDateDesc();
        return recentOrders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setContactInfo(order.getContactInfo());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setOrderDate(order.getOrderDate());
            dto.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
                OrderResponseDTO.OrderItemDTO itemDTO = new OrderResponseDTO.OrderItemDTO();
                itemDTO.setProductName(orderItem.getProduct().getName());
                itemDTO.setSize(orderItem.getSize());
                itemDTO.setQuantity(orderItem.getQuantity());
                return itemDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}



