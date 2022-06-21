package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO 객체")
@Getter
@NoArgsConstructor
public class LoginResponseDto {

    @Schema(description = "닉네임", example = "vV위드미Vv", required = true)
    private String nickname;

    @Schema(description = "토큰", example = "Bearer eic985...", required = true)
    private String token;

    @Builder
    public LoginResponseDto(String nickname, String token) {
        this.nickname = nickname;
        this.token = token;
    }

}
