package com.rentcarapp.model.response;

import lombok.Data;


@Data
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int statusCode;

    public ApiResponse(boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public static <T> ApiResponse<T> of(boolean success, String message, T data, int statusCode) {
        return new ApiResponse<>(success, message, data, statusCode);
    }
}