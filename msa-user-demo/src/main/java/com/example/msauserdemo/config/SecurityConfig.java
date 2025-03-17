package com.example.msauserdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//            .csrf(ServerHttpSecurity.CsrfSpec::disable)
//            .authorizeExchange( auth ->
//                    auth.anyExchange().permitAll() )
//            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                ;
//        return http.build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }  -> 빌드 과정 중 오류가 발생하여 MVC 서비스로 변경
//}


import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;




@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/user/**").permitAll() // 로그인 & WebSocket 허용
                        .requestMatchers(request -> "OPTIONS".equals(request.getMethod())).permitAll() // ✅ Preflight 요청 허용
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowedOrigins(List.of("http://localhost:8085,http://localhost:8080")); // ✅ 클라이언트 도메인 허용
        config.setAllowedOrigins(List.of("http://52.77.19.120:8085","http://52.77.19.120:8080")); // ✅ ec2 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization")); // ✅ JWT 토큰 포함 가능
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
