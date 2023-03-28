package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "로그인 요청 DTO 객체")
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "이메일", example = "joinTest@withme.com", required = true)
    @NotBlank
    @Email(message = "이메일 형식에 맞추어 입력해주세요.")
    @Size(min = 1, max = 100, message = "이메일은 100글자 이하입니다.")
    private String email;

    @Schema(description = "비밀번호", example = "1234qwer%T", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8글자 이상 30글자 이하입니다.")
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
