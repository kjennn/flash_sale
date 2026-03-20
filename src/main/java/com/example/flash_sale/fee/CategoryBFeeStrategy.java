package com.example.flash_sale.fee;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Component
public class CategoryBFeeStrategy implements FeeStrategy {
    private static final BigDecimal RATE = new BigDecimal("0.03");

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(RATE).setScale(0, RoundingMode.FLOOR);
    }

    @Override
    public MerchantCategory getCategory() {
        return MerchantCategory.B;
    }
}