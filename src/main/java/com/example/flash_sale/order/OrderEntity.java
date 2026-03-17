package com.example.flash_sale.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long orderAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    public OrderEntity(Long userId, Long productId, Long orderAmount) {
        this.userId = userId;
        this.productId = productId;
        this.orderAmount = orderAmount;
        this.status = OrderStatus.COMPLETED;
        this.createdAt = LocalDateTime.now();
    }
}
