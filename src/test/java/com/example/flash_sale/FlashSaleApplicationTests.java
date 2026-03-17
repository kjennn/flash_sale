package com.example.flash_sale;

import com.example.flash_sale.order.OrderService;
import com.example.flash_sale.product.ProductEntity;
import com.example.flash_sale.product.ProductRepository;
import com.example.flash_sale.user.UserEntity;
import com.example.flash_sale.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FlashSaleApplicationTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	private ProductEntity productId;
	private List<Long> userIds = new ArrayList<>();

	@BeforeEach
	void setUp() {
		// 1. 테스트용 상품 생성 (ID 1L, 가격 1000원, 재고 100개)
		ProductEntity product = new ProductEntity("특가 상품", 1000L, 100L);
        this.productId = productRepository.save(product); // 생성된 ID 저장

		// 2. 테스트용 유저 100명 생성 (각 10,000 포인트씩)
		for (int i = 0; i < 100; i++) {
			UserEntity user = new UserEntity("유저" + i, 10000L);
			UserEntity savedUser = userRepository.save(user);
			userIds.add(savedUser.getId());
		}
	}

	@Test
	@DisplayName("100명이 동시에")
	void contextLoads() throws InterruptedException {

		int threadCount = 100;

		// 멀티스레드 테스트를 위한 도구
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);


		for (int i = 0; i < threadCount; i++) {
			Long userId = (long)(i + 1); //
			executorService.submit(() -> {
				try {
					orderService.createOrder( userId, productId.getId());
				}catch (Exception e){
					System.out.println("구매 실패 ! : " + e.getMessage());
				}finally {
					countDownLatch.countDown(); //작업완료
				}

			});
		}

		countDownLatch.await(); //모든 스레드 작업 끝날떄까지 대기

		ProductEntity productEntity = productRepository.findById(productId.getId()).orElseThrow();

		//100개 있었는데 100명이 사서 재고는 0이여야함
		assertEquals(0, productEntity.getStockQuantity());
	}


}
