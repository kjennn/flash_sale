package com.example.flash_sale.order;

import com.example.flash_sale.product.ProductEntity;
import com.example.flash_sale.product.ProductRepository;
import com.example.flash_sale.user.UserEntity;
import com.example.flash_sale.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


    @Transactional // 이 메서드가 끝날 때까지 DB Lock를 유지하고, 하나라도 실패하면 모든 과정을 되돌리기
    public Long createOrder(Long userId, Long productId) {

        ProductEntity product = productRepository.findByWithLock(productId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        UserEntity user = userRepository.findByWithLock(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        product.decreaseStock(1);
        user.usePoint(product.getPrice());

        OrderEntity order = new OrderEntity(user.getId(), product.getId(), product.getPrice());
        return orderRepository.save(order).getId();

    }
}
