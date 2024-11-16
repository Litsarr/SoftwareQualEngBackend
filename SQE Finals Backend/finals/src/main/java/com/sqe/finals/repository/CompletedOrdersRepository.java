package com.sqe.finals.repository;

import com.sqe.finals.entity.CompletedOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedOrdersRepository extends JpaRepository<CompletedOrders, Long> {
    List<CompletedOrders> findAllByOrderByCompletedAtDesc();
}

