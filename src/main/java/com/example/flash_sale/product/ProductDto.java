package com.example.flash_sale.product;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDto {

    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;
}
