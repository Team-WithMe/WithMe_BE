package com.withme.api.controller;

import com.withme.api.controller.dto.ExceptionResponseDto;
import com.withme.api.exception.UserAlreadyExistException;
import com.withme.api.exception.UserNotInTeamException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    // TODO: 2022/09/08 커스텀 에러처리 이대로 괜찮을까? 내부 에러코드로 대체하는 방법을 생각해봐야 할 것 같다. 
    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        Map<String, Object> errorDetailsMap = new HashMap<>();
        errorDetailsMap.put(ex.getDuplicated(), false);

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), errorDetailsMap);

        return ResponseEntity.badRequest().body(exceptionResponseDto);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), null);
        return ResponseEntity.status(460).body(exceptionResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exceptionResponseDto);
    }

    @ExceptionHandler(UserNotInTeamException.class)
    public final ResponseEntity<Object> handleUserNotInTeamException(UserNotInTeamException ex) {
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
