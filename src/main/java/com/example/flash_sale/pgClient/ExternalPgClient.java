package com.example.flash_sale.pgClient;

import com.example.flash_sale.payment.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

 

@Component
@Slf4j
public class ExternalPgClient {

    public PgResponse authorize(PaymentRequest request) {

        log.info("결재 요청: 주문 번호 : {}- 금액 : {}", request.orderId(), request.amount());

        // 네트,워크 지연
        simulateDelay(800);

        if("timeout-order-99".equals(request.orderId())) {
            log.error("타임아웃 발생");
            throw new RuntimeException("응답지연");
        }

        if(request.amount() > 1000000) {
            log.warn("결제 거절 한도초과");
            return new PgResponse(null, "FAIL");
        }

        String receiptId = "PR-RECP-" + UUID.randomUUID().toString().substring(0, 8);
        log.info("결제 승인 완료 - 영수증 번호 : {}", receiptId);

        return new PgResponse(receiptId, "SUCCESS");
    }

    private  void simulateDelay(int ms){
        try{
            Thread.sleep(ms);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
