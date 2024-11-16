package com.sqe.finals.service;

import com.sqe.finals.entity.*;
import com.sqe.finals.repository.*;
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

    @Autowired
    private CompletedOrdersRepository completedOrderRepository;

    @Autowired
    private CompletedOrdersItemRepository completedOrderItemRepository;

    public Orders checkout(UUID sessionId, OrderRequestDTO checkoutRequest) {
        // Fetch the cart for the given sessionId
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Filter the cart items to include only those in the checkedCartItemIds
        List<CartItem> itemsToCheckout = cart.getItems().stream()
                .filter(item -> checkoutRequest.getCheckedCartItemIds().contains(item.getId()))
                .collect(Collectors.toList());

        // Create a new order object
        Orders order = new Orders();
        order.setSessionId(sessionId);
        order.setContactInfo(checkoutRequest.getContactInfo());
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(checkoutRequest.getCustomerName());
        order.setAddress(checkoutRequest.getAddress());
        order.setPostalCode(checkoutRequest.getPostalCode());

        double totalAmount = 0.0;

        // Process each item in the checkout request
        for (CartItem cartItem : itemsToCheckout) {
            // Create and populate the order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order); // Link the order item to the order
            order.getOrderItems().add(orderItem); // Add the order item to the order's item list

            // Calculate the total amount based on the product price and quantity
            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();

            // Update the product's stock (for specific sizes)
            Product product = cartItem.getProduct();
            Map<String, Integer> sizes = product.getSizes();
            Integer availableQuantity = sizes.get(cartItem.getSize());

            // Check if there is enough stock
            if (availableQuantity == null || availableQuantity < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + cartItem.getProduct().getName() + " (Size: " + cartItem.getSize() + ")");
            }

            // Deduct the quantity from the stock (for the specific size)
            sizes.put(cartItem.getSize(), availableQuantity - cartItem.getQuantity());
            product.setSizes(sizes); // Make sure to save the updated product with the adjusted sizes
            productRepository.save(product);
        }

        // Set the total amount for the order
        order.setTotalAmount(totalAmount);

        // Save the order to the order repository
        orderRepository.save(order);

        // Remove checked-out items from the cart
        for (CartItem cartItem : itemsToCheckout) {
            cartItem.setCart(null); // Disconnect the CartItem from the Cart
        }

        cart.getItems().removeAll(itemsToCheckout); // Remove the items from the Cart list
        cartRepository.save(cart); // Save the updated Cart

        return order;
    }

    // Method to mark order as completed and copy to the completed orders table
    public void completeOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create a new CompletedOrders object to copy the data
        CompletedOrders completedOrder = new CompletedOrders();
        completedOrder.setCustomerName(order.getCustomerName());
        completedOrder.setAddress(order.getAddress());
        completedOrder.setPostalCode(order.getPostalCode());
        completedOrder.setContactInfo(order.getContactInfo());
        completedOrder.setSessionId(order.getSessionId());
        completedOrder.setTotalAmount(order.getTotalAmount());
        completedOrder.setOrderDate(order.getOrderDate());
        completedOrder.setCompletedAt(LocalDateTime.now()); // Mark the completion time

        // Copy the order items to the completed order items table
        List<CompletedOrderItem> completedOrderItems = order.getOrderItems().stream()
                .map(orderItem -> {
                    CompletedOrderItem completedOrderItem = new CompletedOrderItem();
                    completedOrderItem.setProductName(orderItem.getProduct().getName());
                    completedOrderItem.setSize(orderItem.getSize());
                    completedOrderItem.setQuantity(orderItem.getQuantity());
                    completedOrderItem.setPrice(orderItem.getProduct().getPrice());
                    completedOrderItem.setCompletedOrder(completedOrder); // Link to the completed order
                    return completedOrderItem;
                })
                .collect(Collectors.toList());

        // Set the completed items in the completed order
        completedOrder.setCompletedOrderItems(completedOrderItems);

        // Save the completed order to the repository
        completedOrderRepository.save(completedOrder);

        // Optionally, remove the order from the orders table
        orderRepository.delete(order);
    }

    public List<OrderResponseDTO> getRecentOrders() {
        List<Orders> recentOrders = orderRepository.findTop10ByOrderByOrderDateDesc();
        return recentOrders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setContactInfo(order.getContactInfo());
            dto.setAddress(order.getAddress());
            dto.setCustomerName(order.getCustomerName());
            dto.setPostalCode(order.getPostalCode());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setOrderDate(order.getOrderDate());

            // Mapping order items and fetching price from the product related to the order item
            dto.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
                OrderResponseDTO.OrderItemDTO itemDTO = new OrderResponseDTO.OrderItemDTO();
                itemDTO.setProductName(orderItem.getProduct().getName()); // Product name
                itemDTO.setSize(orderItem.getSize()); // Item size
                itemDTO.setQuantity(orderItem.getQuantity()); // Quantity of the product in the order
                itemDTO.setPrice(orderItem.getProduct().getPrice()); // Fetching the price from the associated Product
                return itemDTO;
            }).collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteOrder(Long orderId) {
        // Fetch the order from the repository
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Delete the order from the orders table
        orderRepository.delete(order);
    }

    public List<CompletedOrderResponseDTO> getCompletedOrders() {
        List<CompletedOrders> completedOrders = completedOrderRepository.findAllByOrderByCompletedAtDesc();
        return completedOrders.stream().map(order -> {
            CompletedOrderResponseDTO dto = new CompletedOrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setContactInfo(order.getContactInfo());
            dto.setAddress(order.getAddress());
            dto.setCustomerName(order.getCustomerName());
            dto.setPostalCode(order.getPostalCode());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setOrderDate(order.getOrderDate());
            dto.setCompletedAt(order.getCompletedAt()); // Include completion date

            // Mapping completed order items to DTO
            dto.setOrderItems(order.getCompletedOrderItems().stream().map(orderItem -> {
                CompletedOrderResponseDTO.OrderItemDTO itemDTO = new CompletedOrderResponseDTO.OrderItemDTO();
                itemDTO.setProductName(orderItem.getProductName()); // Product name
                itemDTO.setSize(orderItem.getSize()); // Item size
                itemDTO.setQuantity(orderItem.getQuantity()); // Quantity of the product in the completed order
                itemDTO.setPrice(orderItem.getPrice()); // Price from the completed order item
                return itemDTO;
            }).collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

}




