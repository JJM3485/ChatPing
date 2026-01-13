package com.example.chatping.repository;

import com.example.chatping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberIdOrderByOrderDateDesc(Long memberId);
}
