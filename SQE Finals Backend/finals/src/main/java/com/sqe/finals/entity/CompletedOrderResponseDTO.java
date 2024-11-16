package com.sqe.finals.entity;

import java.time.LocalDateTime;
import java.util.List;

public class CompletedOrderResponseDTO {
    private Long orderId;
    private String contactInfo;
    private Double totalAmount;
    private String customerName;
    private String address;
    private String postalCode;
    private LocalDateTime orderDate;
    private LocalDateTime completedAt; // Added the completedAt field to track when the order was completed
    private List<OrderItemDTO> orderItems;

    // Getters and Setters for CompletedOrderResponseDTO

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getCompletedAt() { // Getter for completedAt
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) { // Setter for completedAt
        this.completedAt = completedAt;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    // Nested DTO for order items
    public static class OrderItemDTO {
        private String productName;
        private String size;
        private int quantity;
        private Double price;

        // Getters and Setters for OrderItemDTO

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }
}

