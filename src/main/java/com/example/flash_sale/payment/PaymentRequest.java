package com.example.flash_sale.payment;

 
public record PaymentRequest(
        Long orderId,
        Long amount,
        String paymentMethod
) {
}
