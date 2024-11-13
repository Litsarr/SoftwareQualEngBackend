package com.sqe.finals.repository;

import com.sqe.finals.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findTop10ByOrderByOrderDateDesc();
}



