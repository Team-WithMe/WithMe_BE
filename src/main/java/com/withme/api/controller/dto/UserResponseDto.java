package com.withme.api.controller.dto;

import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO 객체")
@Getter
@NoArgsConstructor
public class UserResponseDto {

    @Schema(description = "유저 id", example = "1", required = true)
    private String id;

    @Schema(description = "닉네임", example = "vV위드미Vv", required = true)
    private String nickname;

    @Schema(description = "유저 이미지 경로", example = "default or 경로", required = true)
    private String userImage;

    @Schema(description = "토큰", example = "Bearer eic985...")
    private String token;

    public UserResponseDto(User user, String token) {
        this.id = String.valueOf(user.getId());
        this.nickname = user.getNickname();
        this.userImage = user.getUserImage();
        this.token = token;
    }

    public UserResponseDto(User user) {
        this.id = String.valueOf(user.getId());
        this.nickname = user.getNickname();
        this.userImage = user.getUserImage();
    }
}