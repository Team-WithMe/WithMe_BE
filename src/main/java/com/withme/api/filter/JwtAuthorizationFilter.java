package com.withme.api.filter;

import com.withme.api.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * SpringSecurity의 BasicAuthenticationFilter는 권한이나 인증이 필요한 특정 주소를 요청할 경우 수행된다.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("JwtAuthorizationFilter invoked.");

        String jwt = this.resolveToken(request);
        String requestURI = request.getRequestURI();

        if (tokenProvider.validateToken(jwt)) {
            saveAuthenticationOnSecurityContext(jwt, requestURI);
        } else {
            log.info("유효한 JWT 토큰 없음. uri : {}", requestURI);
        }

        chain.doFilter(request, response);;

    }

    private void saveAuthenticationOnSecurityContext(String jwt, String requestURI) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Security Context에 '{}' 인증 정보 저장. uri : {}", authentication, requestURI);
    }

    /**
     * Request Header에서 토큰 정보를 꺼내오기 위한 메서드
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(this.AUTHORIZATION_HEADER))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .orElse("No Token");
    }
}
