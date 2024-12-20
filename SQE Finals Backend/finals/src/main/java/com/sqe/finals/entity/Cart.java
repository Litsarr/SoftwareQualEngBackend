package com.sqe.finals.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Cart {

    @Id
    private UUID sessionId;  // Primary key as UUID

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Manage the forward side of the relationship (Cart -> CartItem)
    private List<CartItem> items = new ArrayList<>();


    // Getter and Setter for sessionId
    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    // Getter and Setter for cartItems
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}

