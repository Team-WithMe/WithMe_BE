package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// TODO: 2022/06/12 회원정보 validation 협의하기

@Schema(description = "로그인을 위한 DTO 객체")
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "이메일", example = "joinTest@withme.com", required = true)
    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @Schema(description = "비밀번호", example = "12345", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }

}
