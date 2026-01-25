package com.example.chatping.service;

import com.example.chatping.dto.OrderFromCartDto;
import com.example.chatping.dto.OrderRequestDto;
import com.example.chatping.entity.*;
import com.example.chatping.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Long order(String email, OrderRequestDto request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // 재고 감소
        product.removeStock(request.getCount());

        // 주문 상품 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrderPrice(product.getPrice());
        orderItem.setCount(request.getCount());

        // 주문 생성
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.createOrder(member, orderItems, 0);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public Long orderFromCart(String email, OrderFromCartDto request) {
        //회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        //장바구니 조회
        Cart cart = cartRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("장바구니가 비었습니다."));

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니에 담긴 상품이 없습니다.");
        }

        //주문 상품 리스트 만들고 재고 깎기
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // 재고 감소
            product.removeStock(cartItem.getCount());

            // 주문 상품 객체로 변환
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrderPrice(product.getPrice());
            orderItem.setCount(cartItem.getCount());

            orderItems.add(orderItem);
        }

        //쿠폰 적용
        int discountAmount = 0;
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new RuntimeException("쿠폰이 유효하지 않습니다."));

            if (coupon.getMember().getId().equals(member.getId()) && !coupon.isUsed()) {
                discountAmount = coupon.getDiscountAmount();
                coupon.use();
            } else {
                throw new RuntimeException("이미 사용했거나 내 쿠폰이 아닙니다.");
            }
        }

        //실제 주문 생성
        Order order = Order.createOrder(member, orderItems, discountAmount);
        orderRepository.save(order);

        //장바구니 비우기
        cartItemRepository.deleteAll(cartItems);
        cart.getCartItems().clear();

        return order.getId();
    }
}
