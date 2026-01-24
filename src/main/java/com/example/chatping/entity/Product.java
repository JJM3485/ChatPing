package com.example.chatping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @Column(columnDefinition = "TEXT")
    private String description; // 벡터 검색용 설명

    private String imageUrl;
    private String category;

    public void removeStock(int count) {
        int restStock = this.stockQuantity - count;
        if (restStock < 0) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}
