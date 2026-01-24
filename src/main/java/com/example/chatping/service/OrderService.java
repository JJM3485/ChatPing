package com.example.chatping.service;

import com.example.chatping.dto.OrderRequest;
import com.example.chatping.entity.*;
import com.example.chatping.enums.OrderStatus;
import com.example.chatping.repository.MemberRepository;
import com.example.chatping.repository.OrderRepository;
import com.example.chatping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long order(String email, OrderRequest request) {
        // 주문자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 재고 확인 및 감소
        product.removeStock(request.getCount());

        // 주문 상세 정보 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrderPrice(product.getPrice()); // 구매 당시 가격 박제
        orderItem.setCount(request.getCount());

        // 주문 생성
        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        order.getOrderItems().add(orderItem);
        orderItem.setOrder(order);

        orderRepository.save(order);

        return order.getId();
    }
}
