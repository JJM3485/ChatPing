package com.example.chatping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequest {
    private Long productId; // 무슨 상품
    private int count;      // 몇 개 
}
