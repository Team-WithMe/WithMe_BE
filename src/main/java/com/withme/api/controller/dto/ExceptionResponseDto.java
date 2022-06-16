package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Schema(description = "Exception Return을 위한 DTO 객체")
@Getter
@AllArgsConstructor
public class ExceptionResponseDto {

    @Schema(description = "Exception 원인에 대한 간단한 내용", example = "Information Duplicated")
    private String messgae;

    @Schema(description = "Exception 원인에 대한 Key-Value", example = "{\"email\": false")
    private Map<String, Object> details;

}
