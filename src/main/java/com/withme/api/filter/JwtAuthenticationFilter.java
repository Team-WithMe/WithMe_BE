package com.withme.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.config.auth.PrincipalDetails;
import com.withme.api.domain.user.User;
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


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.objectMapper = new ObjectMapper();
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
        log.info("attemptAuthentication invoked.");
        log.info("objectMapper : " + objectMapper);

        //1. username, password를 받아서

        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            log.info("user : {}", user);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService의 loadUserByUsername() 메서드가 실행된다.
            //정상이라면 authentication이 리턴된다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("authentication : {}", authentication);

            //return하면 authentication 객체가 session 영역에 저장된다.
            //JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 security가 권한 관리를 대신 해주므로 권한 처리를 목적으로 session에 저장
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
        log.info("successfulAuthentication invoked.");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        log.info("principalDetails : {}", principalDetails);

        String jwt = tokenProvider.createToken(authResult);

        response.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
    }
}