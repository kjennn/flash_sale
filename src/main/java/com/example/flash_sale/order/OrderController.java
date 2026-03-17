package com.example.flash_sale.order;

import com.example.flash_sale.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<Long> createOrder(@RequestBody OrderDto orderDto) {
        Long order = orderService.createOrder(orderDto.getUserId(), orderDto.getProductId());
        return ApiResponse.success(order);

    }
}
