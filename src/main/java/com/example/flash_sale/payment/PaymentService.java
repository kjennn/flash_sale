package com.example.flash_sale.payment;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Long ready(PaymentRequest request) {
        return paymentRepository.save(new PaymentEntity(request.orderId(), request.amount())).getId();
    }

    @Transactional
    public void completePayment(Long id, String pgReceiptId) {
        PaymentEntity paymentEntity = paymentRepository.findById(id).get();
        paymentEntity.complete(pgReceiptId);
    }

    @Transactional
    public void failPayment(Long id) {
        PaymentEntity paymentEntity = paymentRepository.findById(id).get();
        paymentEntity.fail();
    }
}
