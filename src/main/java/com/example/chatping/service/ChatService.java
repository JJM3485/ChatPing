package com.example.chatping.service;

import com.example.chatping.entity.Coupon;             // [추가]
import com.example.chatping.entity.Member;
import com.example.chatping.entity.TrustScore;
import com.example.chatping.enums.CharacterType;
import com.example.chatping.repository.CouponRepository; // [추가]
import com.example.chatping.repository.MemberRepository;
import com.example.chatping.repository.TrustScoreRepository;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // [추가]
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatLanguageModel chatLanguageModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final MemberRepository memberRepository;
    private final TrustScoreRepository trustScoreRepository;

    private final CouponRepository couponRepository;

    @Transactional
    public String chat(String email, String userMessage, int typeIndex) {

        // 회원 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        // 캐릭터 설정
        CharacterType personaType = CharacterType.values()[typeIndex];

        // 점수표 가져오기
        TrustScore trustScore = trustScoreRepository.findByMemberIdAndCharacterType(member.getId(), personaType)
                .orElseThrow(() -> new RuntimeException("신뢰도 정보 없음"));

        trustScore.addScore(10);
        System.out.println("친밀도 상승! 현재 점수: " + trustScore.getScore());

        boolean couponIssued = false; // 이번 턴에 쿠폰 받았는지 체크

        // 100점 넘으면 쿠폰 발급
        if (trustScore.getScore() >= 100) {
            Coupon newCoupon = Coupon.builder()
                    .name(personaType.name() + "의 감사 쿠폰") // 예: CAT의 감사 쿠폰
                    .discountAmount(5000) // 5000원 할인
                    .isUsed(false)
                    .expiredAt(LocalDateTime.now().plusDays(30)) // 유효기간 30일
                    .member(member)
                    .build();

            couponRepository.save(newCoupon);

            trustScore.setScore(0); // 점수 초기화
            couponIssued = true;
            System.out.println("쿠폰이 발급되었습니다!");
        }

        // RAG 검색
        Embedding queryEmbedding = embeddingModel.embed(userMessage).content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 3);
        String context = relevant.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n---\n\n"));

        // 프롬프트 생성
        String personaPrompt = getPersonaPrompt(personaType);

        if (couponIssued) {
            personaPrompt += "\n[SYSTEM]: 방금 고객의 신뢰도가 100이 되어 5000원 쿠폰을 선물했습니다. 축하해주고 쿠폰을 줬다고 생색내세요.";
        }

        String prompt = """
                %s
                
                아래 [상품 정보]를 참고해서 답변해라냥. 정보가 없으면 모른다고 해라냥.
                
                [상품 정보]
                %s
                
                [고객 질문]
                %s
                """.formatted(personaPrompt, context, userMessage);

        return chatLanguageModel.generate(prompt);
    }

    private String getPersonaPrompt(CharacterType type) {
        switch (type.ordinal()) {
            case 0: return "당신은 20대 남성 직원입니다. 부드럽고 상냥하게 존댓말을 씁니다.";
            case 1: return "당신은 20대 여성 직원입니다. 활발하고 귀엽게! 이모지(✨)를 섞어서 말해요!";
            case 2: return "당신은 츤데레 고양이 점원입니다. 말끝마다 '냥'을 붙이세요. 귀찮은 척하지만 챙겨줍니다.";
            default: return "친절한 AI 점원입니다.";
        }
    }
}
