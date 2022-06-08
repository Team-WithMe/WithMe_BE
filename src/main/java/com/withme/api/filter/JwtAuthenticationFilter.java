package com.withme.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.config.auth.PrincipalDetails;
import com.withme.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /login 요청에서 username, password를 post로 받으면
 * UsernamePasswordAuthenticationFilter가 동작한다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;  //로그인을 수행하는 주체

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

        //1. username, password를 받아서

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            System.out.println("user : " + user.toString());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService의 loadUserByUsername() 메서드가 실행된다.
            //정상이라면 authentication이 리턴된다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //authentication 객체가 session 영역에 저장됨.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info(principalDetails.getUsername());

            //return하면 authentication 객체가 session영역에 저장된다.
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 security가 권한 관리를 대신 해주므로 권한 처리를 목적으로 session에 저장
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2. authenticationManager로 로그인 시도 -> PrincipalDetailsService가 호출되고 loadUserByUsername 메서드가 수행된다.

        //3. PrincipalDetails를 세션에 담고

        //4. JWT 토큰을 만들어서 응답
        return null;
    }
}
