package com.example.flash_sale.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class ProductEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;


    public void decreaseStock(int quantity) {
        if(this.stockQuantity < quantity) {
            throw  new RuntimeException("SOLD_OUT");
        }
        this.stockQuantity -= quantity;
    }

    public ProductEntity(String name, Long price, Long stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
