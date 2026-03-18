package com.example.flash_sale.payment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(name = "uk_order_id", columnNames = {"orderId"})
})
public class PaymentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long orderId;
    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String pgReceiptId; // PG사에서 준 영수증 번호

    public PaymentEntity(Long orderId, Long amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public void complete(String pgReceiptId) {
        if(this.status != PaymentStatus.READY) {
            throw new IllegalStateException("결제 완료가 가능한 상태가 아닙니다.");
        }
        this.status = PaymentStatus.DONE;
        this.pgReceiptId = pgReceiptId;
    }

    public void fail(){
        this.status = PaymentStatus.FAIL;
    }
}
