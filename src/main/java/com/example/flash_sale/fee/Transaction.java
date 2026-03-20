package com.example.flash_sale.fee;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@NoArgsConstructor
public class Transaction {
    private Long orderId;        // 주문 번호
    private MerchantCategory category; // 가맹점 카테고리 (A, B, C)
    private BigDecimal amount;     // 결제 금액
    private LocalDateTime transactedAt; // 결제 일시

    // 생성자, Getter 등...
    public Transaction(Long orderId, MerchantCategory category, BigDecimal amount, LocalDateTime transactedAt) {
        this.orderId = orderId;
        this.category = category;
        this.amount = amount;
        this.transactedAt = transactedAt;
    }
}
