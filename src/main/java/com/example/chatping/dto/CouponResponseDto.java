package com.example.chatping.dto;

import com.example.chatping.entity.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponResponseDto {
    private Long id;
    private String name;
    private int discountAmount;
    private boolean isUsed;
    private LocalDateTime expiredAt;

    public CouponResponseDto(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.discountAmount = coupon.getDiscountAmount();
        this.isUsed = coupon.isUsed();
        this.expiredAt = coupon.getExpiredAt();
    }
}
