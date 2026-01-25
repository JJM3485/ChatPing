package com.example.chatping.controller;

import com.example.chatping.dto.OrderFromCartDto; // 추가
import com.example.chatping.dto.OrderRequestDto;
import com.example.chatping.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 단일 상품 바로 주문
    @PostMapping
    public ResponseEntity<String> order(@RequestBody OrderRequestDto request, Principal principal) {
        Long orderId = orderService.order(principal.getName(), request);
        return ResponseEntity.ok("주문 성공! 주문 번호: " + orderId);
    }

    // 장바구니에서 주문하기
    @PostMapping("/cart")
    public ResponseEntity<String> orderFromCart(@RequestBody OrderFromCartDto request, Principal principal) {
        Long orderId = orderService.orderFromCart(principal.getName(), request);
        return ResponseEntity.ok("장바구니 주문 완료! 주문 번호: " + orderId);
    }
}
