package com.example.chatping.service;

import com.example.chatping.dto.LoginRequest;
import com.example.chatping.dto.SignUpRequest;
import com.example.chatping.entity.Member;
import com.example.chatping.entity.TrustScore;
import com.example.chatping.enums.CharacterType;
import com.example.chatping.repository.MemberRepository;
import com.example.chatping.repository.TrustScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // 보안 설정 필요
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.chatping.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final TrustScoreRepository trustScoreRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화용
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 회원가입
    @Transactional
    public Long signUp(SignUpRequest request) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 회원 생성 (비밀번호 암호화)
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setNickname(request.getNickname());

        // 회원 저장
        Member savedMember = memberRepository.save(member);

        // 캐릭터 3명과 친밀도 초기화
        initTrustScore(savedMember, CharacterType.MALE);
        initTrustScore(savedMember, CharacterType.FEMALE);
        initTrustScore(savedMember, CharacterType.CAT);

        return savedMember.getId();
    }

    // 2. 로그인
    public String login(LoginRequest request) { // 리턴 타입: Member -> String
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(member.getEmail());
    }

    // 캐릭터 친밀도 초기화
    private void initTrustScore(Member member, CharacterType type) {
        TrustScore trustScore = new TrustScore();
        trustScore.setMember(member);
        trustScore.setCharacterType(type);
        trustScore.setScore(0);
        trustScoreRepository.save(trustScore);
    }
}

