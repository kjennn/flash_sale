package com.example.flash_sale.user;

import com.example.flash_sale.product.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;




public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)  //for_update
    @Query("select u from UserEntity u where u.id = :id")
    Optional<UserEntity> findByWithLock(Long id);

}
