package com.example.flash_sale.payment;

import com.example.flash_sale.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentComponent paymentComponent;

    @PostMapping
    public ApiResponse<Void> pay(@RequestBody PaymentRequest paymentRequest) {
        paymentComponent.pay(paymentRequest);
        return ApiResponse.successNull();
    }
}
