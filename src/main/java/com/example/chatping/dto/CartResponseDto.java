package com.example.chatping.dto;

import com.example.chatping.entity.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private Long cartId;
    private Long memberId;
    private List<CartItemDto> items; // 상품 목록

    public CartResponseDto(Cart cart) {
        this.cartId = cart.getId();
        this.memberId = cart.getMember().getId();
        this.items = cart.getCartItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());
    }
}
