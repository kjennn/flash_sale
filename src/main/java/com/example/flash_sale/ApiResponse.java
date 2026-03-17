package com.example.flash_sale;

import lombok.AllArgsConstructor;
import lombok.Getter;

 
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return  new ApiResponse<>(true, "Success", "성공", data);
    }

    public static <T> ApiResponse<T> successNull() {
        return  new ApiResponse<>(true, "Success", "성공", null);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
