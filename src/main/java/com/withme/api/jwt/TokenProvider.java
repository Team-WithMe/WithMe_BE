package com.withme.api.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.config.auth.PrincipalDetails;
import com.withme.api.controller.dto.LoginResponseDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 토큰의 생성 및 토큰 유효성 검증을 담당
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String USER_ID = "id";
    private static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private Key key;


    public TokenProvider (
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 주입받은 seret값을 BASE64 디코딩 후 key 변수에 할당하기 위한 메서드
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public void sendRedirectWithBase64EncodedToken(HttpServletResponse response, Authentication authResult) throws IOException {
        String jwt = "Bearer " + this.createToken(authResult);

        response.sendRedirect("http://localhost:3000/successOauth?"
                + RandomString.make(5)
                + "="
                + Base64.getEncoder().encodeToString(jwt.getBytes())
        );
    }

    public void sendResponseWithToken(HttpServletResponse response, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String jwt = "Bearer " + this.createToken(authResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader(AUTHORIZATION_HEADER, jwt);
        response.getWriter().write(this.setBody(userDetails, jwt));
    }

    private String setBody(UserDetails userDetails, String jwt) throws JsonProcessingException {
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .nickname(userDetails.getUsername())
                .token(jwt)
                .build();
        return objectMapper.writeValueAsString(loginResponseDto);
    }


    /**
     * Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 메서드
     * @param authentication
     * @return jwt 토큰
     */
    private String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, this.getAuthoritiesFromAuthentication(authentication))
                .claim("id", ((PrincipalDetails)authentication.getPrincipal()).getUserId())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(this.getValidity())
                .compact();
    }

    private Date getValidity() {
        long now = (new Date()).getTime();
        return new Date(now + this.tokenValidityInMilliseconds);
    }

    private String getAuthoritiesFromAuthentication(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * 토큰을 파라미터로 받아 Authentication 정보를 리턴하는 메서드
     * @param token
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User user = userRepository.findById(Long.parseLong(claims.get("id").toString()))
                .orElseThrow(() -> new UsernameNotFoundException("User not exist."));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    /**
     * 토큰을 파라미터로 받아 유효성 검사를 하는 메서드
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            if(token.equals("No Token")){
                return false;
            } else {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }
}