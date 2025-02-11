package com.example.kafkachat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 특정 오리진만 허용 (모든 오리진을 허용할 경우 allowedOriginPatterns 사용)
        config.setAllowedOriginPatterns(Arrays.asList("*")); // 🔥 `allowedOrigins("*")` 대신 `allowedOriginPatterns("*")`
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // ✅ Credentials 허용

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
