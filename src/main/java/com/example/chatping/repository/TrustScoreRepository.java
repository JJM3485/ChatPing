package com.example.chatping.repository;

import com.example.chatping.entity.TrustScore;
import com.example.chatping.enums.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrustScoreRepository extends JpaRepository<TrustScore, Long> {
    Optional<TrustScore> findByMemberIdAndCharacterType(Long memberId, CharacterType characterType);
}
