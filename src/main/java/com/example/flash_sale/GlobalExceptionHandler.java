package com.example.flash_sale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

 
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e) {
        String message = e.getMessage();

        if("SOLD_OUT".equals(message)) {
            return ApiResponse.fail("ORDER_ERROR" , "상품 재고 소진 !");
        }

        if("INSUFFICIENT_POINT".equals(message)) {
            return ApiResponse.fail("USER_ERROR" , "잔액 부족 ~!");
        }

        log.error("RuntimeException 발생: ", e);
        return ApiResponse.fail("ERROR" ,"알수없는 오류 발생~!");

    }
}
