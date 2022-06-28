package com.withme.api.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.config.auth.PrincipalDetails;
import com.withme.api.controller.dto.LoginRequestDto;
import com.withme.api.controller.dto.LoginResponseDto;
import com.withme.api.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /login 요청에서 username, password를 post로 받으면 UsernamePasswordAuthenticationFilter가 동작한다.
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;  //로그인을 수행하는 주체
    private TokenProvider tokenProvider;
    private ObjectMapper objectMapper;

    public static final String AUTHORIZATION_HEADER = "Authorization";


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    /**
     * login을 시도하기 위해 실행되는 메서드
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        log.debug("attemptAuthentication invoked.");

        try {
            //PrincipalDetailsService의 loadUserByUsername() 메서드가 실행된다.
            //정상이라면 authentication이 리턴된다.
            Authentication authentication = authenticationManager.authenticate(this.getUsernamePasswordAuthenticationToken(request));
            log.debug("authentication : {}", authentication);

            //return하면 authentication 객체가 session 영역에 저장된다.
            //JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 security가 권한 관리를 대신 해주므로 권한 처리를 목적으로 session에 저장
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(HttpServletRequest request) throws IOException {
        LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

        return new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }

    /**
     * attemptAuthentication 실행 후 인증이 정상적으로 수행되면 실행되는 메서드
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("successfulAuthentication invoked.");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        log.debug("principalDetails : {}", principalDetails);

        String jwt = this.creatJwt(authResult);

        this.sendResponse(response, principalDetails, jwt);

    }

    private String creatJwt(Authentication authResult) {
        return "Bearer " + tokenProvider.createToken(authResult);
    }

    private void sendResponse(HttpServletResponse response, PrincipalDetails principalDetails, String jwt) throws IOException {
        String body = this.getBody(principalDetails, jwt);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader(AUTHORIZATION_HEADER, jwt);
        response.getWriter().write(body);
    }

    private String getBody(PrincipalDetails principalDetails, String jwt) throws JsonProcessingException {
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .nickname(principalDetails.getNickname())
                .token(jwt)
                .build();
        return objectMapper.writeValueAsString(loginResponseDto);
    }
}
