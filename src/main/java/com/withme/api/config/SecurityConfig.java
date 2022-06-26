package com.withme.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.withme.api.config.auth.CustomAuthenticationSuccessHandler;
import com.withme.api.config.auth.CustomOAuth2UserService;
import com.withme.api.filter.JwtAuthenticationFilter;
import com.withme.api.filter.JwtAuthorizationFilter;
import com.withme.api.jwt.JwtAccessDeniedHandler;
import com.withme.api.jwt.JwtAuthenticationEntryPoint;
import com.withme.api.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //@PreAuthorized 어노테이션을 메서드 단위로 추가하여 사용하기 위해 적용
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    //private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함

                .and()
                .formLogin().disable()  //form login 사용안함
                .httpBasic().disable()  //header에 ID, PW를 담고 요청하는 http basic 방식 사용 안함
                .addFilter(corsFilter)  //인증이 필요한 요청에도 cors 적용
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenProvider, objectMapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenProvider))

                .authorizeRequests()
                .antMatchers("/api/v1/user/**").authenticated()
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()

                .and()
                .oauth2Login()
                //.successHandler(customAuthenticationSuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
