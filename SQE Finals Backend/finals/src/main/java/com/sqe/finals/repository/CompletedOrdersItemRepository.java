package com.sqe.finals.repository;

import com.sqe.finals.entity.CompletedOrderItem;
import com.sqe.finals.entity.CompletedOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedOrdersItemRepository extends JpaRepository<CompletedOrderItem, Long> {
}
