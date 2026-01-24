package com.example.chatping.service;

import com.example.chatping.dto.CartResponseDto;
import com.example.chatping.entity.Cart;
import com.example.chatping.entity.CartItem;
import com.example.chatping.entity.Member;
import com.example.chatping.entity.Product;
import com.example.chatping.repository.CartItemRepository;
import com.example.chatping.repository.CartRepository;
import com.example.chatping.repository.MemberRepository;
import com.example.chatping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 장바구니 담기
    public void addToCart(String email, Long productId, int count) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // 내 장바구니 찾기
        Cart cart = cartRepository.findByMemberId(member.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setMember(member);
                    return cartRepository.save(newCart);
                });

        // 이미 담겨있는 물건인지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (savedCartItem != null) {
            // 이미 있으면 수량만 증가
            savedCartItem.addCount(count);
        } else {
            // 없으면 새로 담기
            CartItem newCartItem = CartItem.createCartItem(cart, product, count);
            cartItemRepository.save(newCartItem);
        }
    }

    // 내 장바구니 조회
    @Transactional(readOnly = true)
    public CartResponseDto getMyCart(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        Cart cart = cartRepository.findByMemberId(member.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setMember(member);
                    return cartRepository.save(newCart);
                });

        return new CartResponseDto(cart);
    }
}
