package com.example.flash_sale.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long pointBalance;


    public void usePoint(Long amount){
        if(this.pointBalance < amount){
            throw  new RuntimeException("INSUFFICIENT_POINT");
        }
        this.pointBalance -= amount;
    }


    public void charge(Long amount){
        this.pointBalance += amount;
    }

    public UserEntity(String name, Long pointBalance) {
        this.name = name;
        this.pointBalance = pointBalance;
    }
}
