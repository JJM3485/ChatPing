package com.example.chatping.repository;

import com.example.chatping.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); // 로그인용 (이메일로 찾기)
    boolean existsByEmail(String email); // 회원가입용 (중복 검사)
}
