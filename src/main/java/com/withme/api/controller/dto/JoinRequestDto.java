package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "회원가입을 위한 DTO 객체")
@Getter
@NoArgsConstructor
public class JoinRequestDto {

    @Schema(description = "사용자 이름", example = "withMe", required = true)
    @NotNull
    @Size(min = 3, max = 100)
    private String username;

    @Schema(description = "이메일", example = "withMe@withme.com", required = true)
    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @Schema(description = "비밀번호", example = "12345", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @Schema(description = "닉네임", example = "vV짱짱위드미짱짱Vv", required = true)
    @NotNull
    @Size(min = 3, max = 100)
    private String nickname;

    @Schema(hidden = true)
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Builder
    public JoinRequestDto(String username, String email, String password, String nickname){
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity(){
        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .nickname(this.nickname)
                .activated(true)
                .userImage(null)
                .role("ROLE_USER")
                .build();
    }

}
