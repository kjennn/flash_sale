package com.example.flash_sale.fee;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;



@Component
public class CategoryAFeeStrategy implements FeeStrategy {

    private static final BigDecimal THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal LOW_RATE = new BigDecimal("0.01");
    private static final BigDecimal HIGH_RATE = new BigDecimal("0.02");

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        BigDecimal rate = amount.compareTo(THRESHOLD) <= 0 ? LOW_RATE : HIGH_RATE;
        return amount.multiply(rate).setScale(0, RoundingMode.FLOOR);
    }

    @Override
    public MerchantCategory getCategory() {
        return MerchantCategory.A;
    }
}
