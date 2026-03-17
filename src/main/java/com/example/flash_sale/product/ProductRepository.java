package com.example.flash_sale.product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;




public interface ProductRepository  extends JpaRepository<ProductEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) //for_update
    @Query("select p from ProductEntity p where p.id = :id")
    Optional<ProductEntity> findByWithLock(Long id);


}
