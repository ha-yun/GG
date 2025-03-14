package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
    /**
     * 게이트웨이에서 각종 서비스를 등록하는 방법중 코드로 등록 -> 빈
     * RouteLocator 객체를 빈으로 등록
     * 특정 조건(예: 사용자 권한, 요청 헤더 값)에 따라 동적으로 API 라우팅을 변경하고 싶을 때
     */
    @Bean
    public RouteLocator starlinkRouteLocator(RouteLocatorBuilder builder) {
        System.out.println("게이트웨이에서 개별 서비스 등록");
        return builder.routes()
                .route("msa-user-auth", // msa-user 라우팅 추가
                        r -> r.path("/auth/**").uri("lb://msa-user-demo")) // /auth/**
                .route("msa-user",
                        r -> r.path("/user/**").uri("lb://msa-user-demo")) // /user/**
                .route("msa-starboard", // msa-starboard 라우팅 추가
                        r -> r.path("/api/posts/**").uri("lb://msa-starboard"))
                .route("msa-starboard",
                        r -> r.path("/api/comments/**").uri("lb://msa-starboard"))
                .route("msa-chat", // msa-chat 라우팅 추가
                        r -> r.path("/api/live/**").uri("lb://msa-chat"))
                .route("msa-chat",
                        r -> r.path("/chat/**").uri("lb://msa-chat"))
                .route("products", // products(msa-goods) 라우팅 추가
                        r -> r.path("/pdts/**").uri("lb://products"))
                .route("products",
                        r -> r.path("/orders/**").uri("lb://products"))
                .route("products",
                        r -> r.path("/customgoods/**").uri("lb://products"))
                .build();


    }
}

