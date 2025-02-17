package com.example.msastarboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 게시글 생성, 수정, 삭제는 인증된 사용자만 접근 가능
                        .requestMatchers("/api/posts/create", "/api/posts/{id}/**").authenticated()
                        //.hasRole("STAR")
                        // 댓글 및 좋아요 기능은 인증된 사용자만 접근 가능
                        .requestMatchers("/api/comments/**", "/api/hearts/**").authenticated()
                        // 기타 요청은 모두 허용
                        .anyRequest().permitAll()
                )
                .build();
    }
}
