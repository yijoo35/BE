package com.plana.seniorjob.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Swagger 테스트용 CSRF 해제
                .authorizeHttpRequests(auth -> auth
                        // Swagger 문서 & H2 콘솔 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**"
                        ).permitAll()
                        // 그 외 허용(개발)
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔용
                .formLogin(login -> login.disable()) // 로그인 페이지 비활성
                .httpBasic(basic -> basic.disable()); // 기본 인증창 비활성

        return http.build();
    }
}