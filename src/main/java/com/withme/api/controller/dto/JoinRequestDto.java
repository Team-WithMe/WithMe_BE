package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "회원가입을 위한 DTO 객체")
@Getter
@ToString
@NoArgsConstructor
public class JoinRequestDto {

    @Schema(description = "이메일", example = "joinTest@withme.com", required = true)
    @NotNull
    @Email(message = "이메일 형식에 맞추어 입력해주세요.")
    @Size(min = 1, max = 100, message = "이메일은 100자 이내로 입력해주세요.")
    private String email;

    @Schema(description = "비밀번호", example = "1234qwer%T", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 8, max = 30, message = "비밀번호는 8글자 이상 30글자 이하입니다.")
    private String password;

    @Schema(description = "닉네임", example = "vV위드미Vv", required = true)
    @NotNull
    @Size(min = 2, max = 8, message = "닉네임은 2글자 이상 8글자 이하입니다.")
    private String nickname;

    @Builder
    public JoinRequestDto(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .activated(true)
                .userImage(null)
                .role("ROLE_USER")
                .build();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password= passwordEncoder.encode(this.password);
    }

}
