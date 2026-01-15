package com.example.chatping.controller;

import com.example.chatping.dto.LoginRequest;
import com.example.chatping.dto.SignUpRequest;
import com.example.chatping.entity.Member;
import com.example.chatping.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입 API
    // 주소: POST http://localhost:8080/members/signup
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        Long memberId = memberService.signUp(request);
        return ResponseEntity.ok("회원가입 성공! 회원 ID: " + memberId);
    }

    // 2. 로그인 API
    // 주소: POST http://localhost:8080/members/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = memberService.login(request); // 토큰을 받음
        return ResponseEntity.ok(token);
    }
}
