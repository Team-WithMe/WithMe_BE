package com.withme.api.controller;

import com.withme.api.controller.dto.ExceptionResponseDto;
import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.controller.dto.UserUpdateRequestDto;
import com.withme.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "회원가입"
        , description = "새로운 유저의 정보를 DB에 저장한다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201"
            , description = "회원가입 성공"
        )
        , @ApiResponse(
            responseCode = "422"
            , description = "파라미터 유효성 부적합"
            , content = {@Content(schema = @Schema(implementation = ExceptionResponseDto.class))}
        )
        , @ApiResponse(
            responseCode = "400"
            , description = "이메일 혹은 닉네임 중복"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
        )
    })
    @PostMapping("/join")
    public ResponseEntity<Object> createUser(@Valid @RequestBody JoinRequestDto dto) {
        log.debug("createUser{} invoked", dto);
        userService.createUser(dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/user/nickname/{id}")
    public ResponseEntity<Object> changeUserNickname(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto dto) {
        log.debug("changeUserNickname{} invoked", dto);
        userService.changeUserNickname(id, dto);

        return ResponseEntity.ok().build();
    }

}