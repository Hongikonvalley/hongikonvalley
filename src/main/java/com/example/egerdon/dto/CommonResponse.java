package com.example.egerdon.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 공통 API 응답 포맷
 */
@Getter
@Builder
public class CommonResponse<T> {
    private int statusCode;
    private String message;
    private T data;
} 