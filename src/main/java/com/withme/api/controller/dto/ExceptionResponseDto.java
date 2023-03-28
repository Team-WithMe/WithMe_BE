package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Schema(description = "Exception 응답 DTO 객체")
@Getter
public class ExceptionResponseDto {

    @Schema(description = "Exception 상태 코드")
    private int statusCode;

    @Schema(description = "Exception 원인에 대한 간단한 내용")
    private String message;

    @Schema(description = "Exception 원인에 대한 Key-Value")
    private Map<String, String> details;

    public ExceptionResponseDto(int statusCode, String message, Map<String, String> details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

    public ExceptionResponseDto(int statusCode, String message) {
        this(statusCode, message, null);
    }
}
