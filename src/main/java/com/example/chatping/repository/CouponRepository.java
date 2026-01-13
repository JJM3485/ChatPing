package com.example.chatping.repository;

import com.example.chatping.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByMemberId(Long memberId);
}
