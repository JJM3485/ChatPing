package com.example.chatping.entity;

import com.example.chatping.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int totalPrice;    // 총 금액
    private int discountPrice; // 쿠폰 할인 금액
    private int finalPrice;    // 최종 결제 금액

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems, int discountPrice) {
        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        order.setDiscountPrice(discountPrice);

        int total = 0;
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
            total += item.getOrderPrice() * item.getCount();
        }

        order.setTotalPrice(total);
        order.setFinalPrice(total - discountPrice);

        return order;
    }
}
