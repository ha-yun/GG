package com.example.apigateway.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;


@Component
public class JwtFilter implements WebFilter, ApplicationContextAware {

    private final JwtTokenProvider jwtTokenProvider;
    private ApplicationContext applicationContext; // setApplicationContext 내부에서 사용

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${security.free-paths}")
    private String[] FREE_PATHS;

    // 맴버가 많을 경우는 생성자 초기화 방식을 직접 구성
    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
        // 요청-> 게이트웨이 -> 스프링시큐리티 점검 -> (*)필터팅 : 라우팅 or 401 에러 처리(서비스가 등록되면 달라질수 있음)-> 서비스진입
        System.out.println("JwtFilter filter() 호출, 요청 발생시 계속 호출");

        // 1. 요청 URL 확인, 획득
        String reqUrl = exchange.getRequest().getURI().getPath();
        System.out.println("요청 URL " + reqUrl );

        // 2. 스프링시큐리에서 인증 없이 통과 가능한 URL 들은 바로 통과 (체크 필요)->종료(요청을 넘김)
        //    인증없이 통과될 URL과 일치하는 URL 존재하는지 체킹 -> AntPathMatcher
        AntPathMatcher matcher = new AntPathMatcher();
        for(String path : FREE_PATHS) {
            if (matcher.match(path, reqUrl)) {
                System.out.println("인증없이 통과 처리 " + reqUrl );
                return chain.filter(exchange); // 다음 필터로 계속 진행
            }
        }
        // 3. 인증을 필요로 하는 요청만 도달 -> 요청 프로토콜의 헤더에서 토큰 추출
        String token = exchange.getRequest().getHeaders().getFirst("Authorization"); // 차후 상수값 확인
        System.out.println("요청 헤더에서 토큰 획득 : " + token);

        // 4. 토큰이 존재한다면
        if( token != null ){
            try{
                String email = jwtTokenProvider.getEmailFromToken(token); // 이메일 추출, 토큰 유효성 검사
                System.out.println("토큰에서 email 추출: " + email);
                // 사용자 인증 설정 -> 인증 객체 (UsernamePasswordAuthenticationToken)을 생성
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        new User(email, "", new ArrayList<>()), null, null
                );
                // exchange.mutate()를 사용하여 요청 헤더에 "X-Auth-User" 정보를 추가
                // ReactiveSecurityContextHolder.withAuthentication(auth)를 이용해 Spring Security 인증 컨텍스트에 등록
                return chain.filter(
                        exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-Auth-User",email)
                                        .build()
                        ).build())
                        // 인증 정보를 Spring Security의 SecurityContext에 설정
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

            }catch(ExpiredJwtException e){  // 토큰 만료 시 Refresh Token으로 재발급
                System.out.println("JWT 만료됨: " + e.getMessage());
                String email = e.getClaims().get("email", String.class);

                String refreshToken = redisTemplate.opsForValue().get(email);
                // 토큰이 만료된 경우, Redis에서 Refresh Token을 가져와 검증
                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    // Refresh Token이 유효하면 새로운 Access Token을 발급하여 헤더에 추가
                    // 인증 정보를 SecurityContext에 설정하고 필터 체인 계속 진행
                    String newAccessToken = jwtTokenProvider.createToken(email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            new User(email, "", new ArrayList<>()), null, new ArrayList<>()
                    );
                    return chain.filter(exchange.mutate()
                                    .request(exchange.getRequest().mutate()
                                            .header("X-Auth-User", email)
                                            .header(HttpHeaders.AUTHORIZATION, newAccessToken)
                                            .build())
                                    .build())
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                }else {
                    throw new RuntimeException(e);
                }
            }catch(Exception e){
                System.out.println("JWT 검증 실패: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("토큰 없음: 요청 차단");
        }
        return chain.filter(exchange);
    }

}
