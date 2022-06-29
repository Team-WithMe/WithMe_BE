package com.withme.api.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.controller.dto.LoginResponseDto;
import com.withme.api.filter.JwtAuthenticationFilter;
import com.withme.api.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("principal : {} ", authentication.getPrincipal());

        this.successfulAuthentication(response, authentication);

    }

    protected void successfulAuthentication(HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        log.debug("successfulAuthentication invoked.");

        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authResult.getPrincipal();
        log.debug("oauth2User : {}", oauth2User);

        String jwt = "Bearer " + tokenProvider.createToken(authResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .nickname(oauth2User.getName())
                .token(jwt)
                .build();

        String body = objectMapper.writeValueAsString(loginResponseDto);

        response.addHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER, jwt);
        response.getWriter().write(body);

    }

}
