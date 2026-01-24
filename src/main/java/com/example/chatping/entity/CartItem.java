package com.example.chatping.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int count;

    public void addCount(int count) {
        this.count += count;
    }

    // 생성 메서드
    public static CartItem createCartItem(Cart cart, Product product, int count) {
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .count(count)
                .build();
    }
}
