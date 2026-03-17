package com.example.flash_sale.product;

import com.example.flash_sale.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    public ApiResponse<ProductEntity> initProduct(@RequestBody ProductDto productDto) {
        ProductEntity entity = new ProductEntity(productDto.getName(), productDto.getPrice(), productDto.getStockQuantity());
        ProductEntity save = productRepository.save(entity);
        return ApiResponse.success(save);
    }
}
