package com.withme.api.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.config.auth.CustomOauth2User;
import com.withme.api.config.auth.PrincipalDetails;
import com.withme.api.controller.dto.UserResponseDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    /**
     * OAuth2로 로그인 성공 후 클라이언트로 토큰을 전달하는 메서드
     * @param response
     * @param authResult JWT 토큰 생성을 위한 인증정보
     * @throws IOException
     */
    public void sendRedirectWithBase64EncodedToken(HttpServletResponse response, Authentication authResult) throws IOException {
        User user = ((CustomOauth2User) authResult.getPrincipal()).getUser();

        String jwt = "Bearer " + this.createToken(authResult, user.getId());
        log.debug("jwt : " + jwt);

        response.sendRedirect("http://localhost:3000/successOauth?"
                + RandomString.make(5)
                + "="
                + Base64.getEncoder().encodeToString(this.setBody(user, jwt).getBytes())
        );
    }

    /**
     * 일반 로그인 후 클라이언트로 토큰을 전달하는 메서드
     * @param response
     * @param authResult JWT 토큰 생성을 위한 인증정보
     * @throws IOException
     */
    public void sendResponseWithToken(HttpServletResponse response, Authentication authResult) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwt = "Bearer " + this.createToken(authResult, principalDetails.getUserId());
        log.debug("jwt : " + jwt);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader(AUTHORIZATION_HEADER, jwt);
        response.getWriter().write(this.setBody(principalDetails.getUser(), jwt));
    }

    /**
     * 로그인 성공 후 응답에 담을 내용을 생성하는 메서드
     * @param user 인증된 유저 정보
     * @param jwt 클라이언트로 전달한 JWT 토큰
     * @return 응답에 담길 내용 - 일반 로그인 : ResponseBody, 소셜 로그인 : QueryString
     * @throws JsonProcessingException
     */
    private String setBody(User user, String jwt) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new UserResponseDto(user, jwt));
    }


    /**
     * Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 메서드
     * @param authentication
     * @return jwt 토큰
     */
    private String createToken(Authentication authentication, Long id) {
        return Jwts.builder()
                .setSubject(authentication.getName())   //소셜로그인일 경우 sub값, 일반 로그인일경우 닉네임
                .setIssuer("WithMe")
                .claim(AUTHORITIES_KEY, this.getAuthoritiesFromAuthentication(authentication))  //권한
                .claim(USER_ID, id) //userId
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(this.getValidity())
                .compact();
    }

    /**
     * JWT 토큰의 유효기간을 설정하는 메서드
     * @return
     */
    private Date getValidity() {
        long now = (new Date()).getTime();
        return new Date(now + this.tokenValidityInMilliseconds);
    }

    /**
     * 유저의 권한을 리턴하는 메서드
     * @param authentication 인증된 유저 정보
     * @return
     */
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
    public Authentication getAuthentication(String token) throws AccessDeniedException{
        Long userIdFromToken = this.getUserIdFromToken(token);
        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new AccessDeniedException("User In Token Not Found. id : " + userIdFromToken));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    /**
     * 토큰을 파라미터로 받아 토큰 내부 body의 userId를 리턴하는 메서드
     * @param token
     * @return userId
     */
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getClaimsFromToken(token).get("id").toString());
    }

    /**
     * 토큰을 파라미터로 받아 토큰 내부의 body를 리턴하는 메서드
     * @param token
     * @return 토큰의 body
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody();
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
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.substring(7));
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