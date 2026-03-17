package com.example.flash_sale.user;

import com.example.flash_sale.ApiResponse;
import com.example.flash_sale.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/{id}/charge")
    public ApiResponse<Void> chargePoint(@PathVariable Long id, @RequestBody Long price) {
        UserEntity user= userRepository.findById(id).orElseThrow();
        user.charge(price);
        userRepository.save(user);
        return ApiResponse.successNull();
    }
}
