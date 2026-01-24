package com.example.chatping.controller;

import com.example.chatping.dto.CartResponseDto;
import com.example.chatping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/add")
    public String addToCart(@RequestBody Map<String, Integer> request, Principal principal) {
        Long productId = Long.valueOf(request.get("productId"));
        int count = request.get("count");

        cartService.addToCart(principal.getName(), productId, count);
        return "장바구니에 담기 성공!";
    }

    // 내 장바구니 조회
    @GetMapping
    public CartResponseDto getMyCart(Principal principal) {
        return cartService.getMyCart(principal.getName());
    }
}
