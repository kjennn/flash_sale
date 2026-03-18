package com.example.flash_sale.pgClient;

 
public record PgRequest(
        Long orderId,
        Long amount,
        String key
) {
}
