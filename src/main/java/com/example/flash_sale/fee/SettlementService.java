package com.example.flash_sale.fee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;



@Service
@RequiredArgsConstructor
public class SettlementService {

    private final List<FeeStrategy> strategyList;

    public List<SettlementResponse> calculateAll(List<Transaction> txs){
        return txs.stream()
                .map(this::calcuateSettlement)
                .toList();
    }

    public SettlementResponse calcuateSettlement(Transaction tx){
        FeeStrategy strategy = strategyList.stream()
                .filter(s -> s.getCategory() == tx.getCategory())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("지원하지 않는 카테고리입니다. " + tx.getCategory()));

        BigDecimal fee= strategy.calculate(tx.getAmount());

        BigDecimal subtract = tx.getAmount().subtract(fee);

        return new SettlementResponse(
                tx.getOrderId(),
                tx.getAmount(),
                fee,
                subtract,
                tx.getTransactedAt().toLocalDate().plusDays(1)
        );
    }
}
