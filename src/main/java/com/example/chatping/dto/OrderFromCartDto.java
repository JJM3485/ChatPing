package com.example.chatping.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderFromCartDto {
    private Long couponId; // 사용할 쿠폰 ID
}
