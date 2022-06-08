package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class JoinRequestDto {

    @NotNull
    @Size(min = 3, max = 100)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 100)
    private String nickname;

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
