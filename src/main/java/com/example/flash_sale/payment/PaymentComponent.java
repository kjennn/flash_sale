package com.example.flash_sale.payment;

import com.example.flash_sale.pgClient.ExternalPgClient;
import com.example.flash_sale.pgClient.PgResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

 
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentComponent {

    private final PaymentService paymentService;
    private final ExternalPgClient pgClient;
    private final PaymentRepository paymentRepository;

    public void pay(PaymentRequest paymentRequest) {

        Optional<PaymentEntity> existing = paymentRepository.findByOrderId(paymentRequest.orderId());
        if (existing.isPresent() && existing.get().getStatus() == PaymentStatus.DONE) {
            log.info("이미 완료된 결제입니다. orderId: {}", paymentRequest.orderId());
            return;
        }

        Long id;

        try {
            // READY 상태 저장-->  여기서 1등만 통과, 나머지는 예외 발생
            id = paymentService.ready(paymentRequest);
        } catch (DataIntegrityViolationException e) {
            // 중복 방어 2등부터는 여기서 잡힘.
            // 이미 진행 중이거나 완료된 것
            log.warn("중복 요청 차단 (DB Unique Constraint): {}", paymentRequest.orderId());
            throw e;
        }

        try {
            // [외부 API] 트랜잭션 밖에서 호출
            PgResponse response = pgClient.authorize(paymentRequest);

            // [완료]
            paymentService.completePayment(id, response.pgReceiptId());
        } catch (Exception e) {
            // [실패] 타임아웃 등 에러 발생 시
            paymentService.failPayment(id);
            log.error("결제 처리 중 진짜 에러 발생");
            throw e;
        }
    }

}
