package com.sqe.finals.repository;

import com.sqe.finals.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findBySessionId(UUID sessionId);  // custom method for finding by sessionId
}


