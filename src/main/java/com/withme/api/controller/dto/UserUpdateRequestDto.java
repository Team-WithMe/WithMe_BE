package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Schema(description = "유저 정보 수정요청 DTO 객체")
@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Schema(description = "수정 비밀번호", example = "1234qwer%T")
    @Size(min = 8, max = 30, message = "비밀번호는 8글자 이상 30글자 이하입니다.")
    private String password;

    @Schema(description = "수정 닉네임", example = "위드미미미")
    @Size(min = 2, max = 8, message = "닉네임은 2글자 이상 8글자 이하입니다.")
    private String nickname;

    @Schema(description = "수정 이미지", example = "아마도 경로..?")
    private String userImage;

    @Builder
    public UserUpdateRequestDto(String password, String nickname, String userImage) {
        this.password = password;
        this.nickname = nickname;
        this.userImage = userImage;
    }

}
