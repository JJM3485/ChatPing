package com.example.chatping.controller;

import com.example.chatping.dto.OrderRequestDto;
import com.example.chatping.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // http://localhost:8080/orders
    @PostMapping
    public ResponseEntity<String> order(@RequestBody OrderRequestDto request, Principal principal) {
        String email = principal.getName();

        Long orderId = orderService.order(email, request);
        return ResponseEntity.ok("주문 성공! 주문 번호: " + orderId);
    }
}
