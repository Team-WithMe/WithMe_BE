package com.withme.api.controller;

import com.withme.api.controller.dto.ExceptionResponseDto;
import com.withme.api.exception.UserAlreadyExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        Map<String, Object> errorDetailsMap = new HashMap<>();
        errorDetailsMap.put(ex.getDuplicated(), false);

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), errorDetailsMap);

        return ResponseEntity.badRequest().body(exceptionResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), null);
        return ResponseEntity.badRequest().body(exceptionResponseDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> errorDetailsMap = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errorDetailsMap.put( ((FieldError) c).getField(), false));

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto("Validation Failed", errorDetailsMap);

        return ResponseEntity.unprocessableEntity().body(exceptionResponseDto);
    }



}
