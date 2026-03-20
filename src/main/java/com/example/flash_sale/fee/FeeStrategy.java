package com.example.flash_sale.fee;

import java.math.BigDecimal;


public interface FeeStrategy {

    BigDecimal calculate(BigDecimal amount);
    MerchantCategory getCategory();
}
