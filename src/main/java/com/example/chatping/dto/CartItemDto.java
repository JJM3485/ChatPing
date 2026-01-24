package com.example.chatping.dto;

import com.example.chatping.entity.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDto {
    private Long itemId;
    private String productName;
    private int price;
    private int count;
    private String imageUrl;

    public CartItemDto(CartItem cartItem) {
        this.itemId = cartItem.getId();
        this.productName = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice();
        this.count = cartItem.getCount();
        this.imageUrl = cartItem.getProduct().getImageUrl();
    }
}
