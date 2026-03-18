package com.example.flash_sale.payment;

 
public enum PaymentStatus {
    READY,       // 결제 요청 생성 (PG사 호출 전)
    IN_PROGRESS, // PG사 응답 대기 중 (멱등성 판단의 핵심!)
    DONE,        // 결제 완료
    FAIL,        // 결제 실패
    CANCELED     // 결제 취소
}
