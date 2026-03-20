package com.example.flash_sale.fee;

import java.math.BigDecimal;
import java.time.LocalDate;


public record SettlementResponse(
        Long orderId,          // 어떤 주문에 대한 정산인지
        BigDecimal originAmount, // 원래 거래 금액
        BigDecimal feeAmount,    // 떼어간 수수료
        BigDecimal finalAmount,  // 가맹점이 받을 최종 금액
        LocalDate settlementDate // 입금 예정일
) {

    public SettlementResponse {
        if(originAmount.subtract(feeAmount).compareTo(finalAmount)!= 0){
            throw new IllegalArgumentException("정산 금액 오류");
        }
    }
}
