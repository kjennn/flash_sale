package com.example.flash_sale.order;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class OrderDto {
    private Long id;

    private Long userId;
    private Long productId;
    private Long orderAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;
}
