package com.example.flash_sale.fee;

import lombok.Getter;


@Getter
public enum MerchantCategory {
    A("전자제품"),
    B("식음료"),
    C("서비스");

    private final String description;

    MerchantCategory(String description) {
        this.description = description;
    }
}
