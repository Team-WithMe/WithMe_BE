package com.withme.api.controller;

import com.withme.api.controller.dto.ExceptionResponseDto;
import com.withme.api.controller.dto.MyPageResponseDto;
import com.withme.api.controller.dto.UserUpdateRequestDto;
import com.withme.api.jwt.TokenProvider;
import com.withme.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Operation(
        summary = "마이페이지 유저 및 팀 정보 조회"
        , description = "마이페이지에서 본인의 닉네임과 속해있는 팀 정보를 조회한다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200"
            , description = "조회 성공"
            , content = @Content(schema = @Schema(implementation = MyPageResponseDto.class))
        )
        , @ApiResponse(
            responseCode = "406"
            , description = "토큰의 유저 id와 pathVariable의 id가 불일치"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
    )
        , @ApiResponse(
            responseCode = "460"
            , description = "id에 일치하는 유저 없음"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
        )
    })
    @GetMapping("/user/mypage/{id}")
    public ResponseEntity<Object> getUserAndTeamInfo(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws IllegalAccessException {
        log.debug("getUserAndTeamInfo{} invoked", id);

        if(!tokenProvider.getUserIdFromToken(authHeader).equals(id)){
            throw new IllegalArgumentException("Requested User ID != Token User ID");
        }

        MyPageResponseDto myPageResponseDto = userService.getUserAndTeamInfo(id);
        log.debug("myPageResponseDto : " + myPageResponseDto);

        return ResponseEntity.ok().body(myPageResponseDto);
    }

    @Operation(
        summary = "닉네임 변경"
        , description = "유저의 닉네임을 수정한다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200"
            , description = "닉네임 변경 성공"
        )
        , @ApiResponse(
            responseCode = "422"
            , description = "파라미터 유효성 부적합"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
        )
        , @ApiResponse(
            responseCode = "400"
            , description = "닉네임 중복"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
        )
        , @ApiResponse(
            responseCode = "406"
            , description = "토큰의 유저 id와 pathVariable의 id가 불일치"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
    )
        , @ApiResponse(
            responseCode = "460"
            , description = "id에 일치하는 유저 없음"
            , content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
        )
    })
    @PutMapping("/user/nickname/{id}")
    public ResponseEntity<Object> changeUserNickname(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto dto, @RequestHeader("Authorization") String authHeader) {
        log.debug("changeUserNickname{} invoked", dto);

        if(!tokenProvider.getUserIdFromToken(authHeader).equals(id)){
            throw new IllegalArgumentException("Requested User ID != Token User ID");
        }
        userService.changeUserNickname(id, dto);
        return ResponseEntity.ok().build();
    }

}