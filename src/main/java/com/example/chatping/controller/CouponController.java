package com.example.chatping.controller;

import com.example.chatping.dto.CouponResponseDto;
import com.example.chatping.entity.Coupon;
import com.example.chatping.entity.Member;
import com.example.chatping.repository.CouponRepository;
import com.example.chatping.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    // 내 쿠폰 조회 API
    @GetMapping
    public List<CouponResponseDto> getMyCoupons(Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        List<Coupon> coupons = couponRepository.findByMemberId(member.getId());

        return coupons.stream()
                .map(CouponResponseDto::new)
                .collect(Collectors.toList());
    }
}
