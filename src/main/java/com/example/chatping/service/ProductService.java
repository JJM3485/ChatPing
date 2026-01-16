package com.example.chatping.service;

import com.example.chatping.entity.Product;
import com.example.chatping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 모든 상품 가져오기
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 상세 조회 (나중에 주문할 때 씀)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 없습니다."));
    }
}
