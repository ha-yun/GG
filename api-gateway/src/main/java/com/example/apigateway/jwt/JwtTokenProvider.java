package com.example.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 토큰 생성시 필요한 비밀키 -> 재료값 (외부 노출 x) -> @Value("...")
    @Value("${jwt.token.raw_secret_key}")
    private String rawSecretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // 엑세스토큰은 통상 1시간, 리플레시토큰 통상 7일 정도 부여 (컨셉)
    // 비밀키(rawSecretKey) 바이트배열로 변경 -> 가공처리(HMAC알고리즘적용)후 사용 -> 변수 정의
    private SecretKey secretKey;

    // 초가화 메소드 @PostConstruct init()
    // Spring이 객체를 생성한 후 자동으로 호출되는 메서드
    @PostConstruct
    public void init() {
        System.out.println("게이트웨이 시크릿 초기화 처리");
        // 시크릿키를 사용에 맞게 변환 처리(HMAC 방식으로 변환하여 저장)
        this.secretKey = Keys.hmacShaKeyFor(rawSecretKey.getBytes());
    }

    // 토큰 생성 -> 기본 재료는 이메일
    // 이메일 정보가 Claims(페이로드)에 저장
    public String createToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("email", email);

        // 시간 세팅
        Date now = new Date(); // 현재시간
        Date expiryDate = new Date(now.getTime() + expiration); // 현재시간+만료시간(양)

        // 추가정보를 토큰에 삽입
        // 시간 -> 토큰생성시간, 만료시간 세팅되야함
        // 서명 (토큰에는 서명이 기입 -> 이를 통해서 향후 검증 활용)
        return Jwts.builder()
                .setClaims( claims )
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(this.secretKey, SignatureAlgorithm.HS256) // 기본(HMAC SHA-256) 알고리즘 적용
                .compact();
    }
    // 토큰 유효성 검사
    // 토큰을 디코딩하고 서명을 검증하여 유효한 경우 true, 그렇지 않으면 false 반환
    // http 헤더 정보에 jwt 토큰(엑세스 토큰)이 전달 -> 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    // 주어진 토큰을 통해서 이메일 획득
    public String getEmailFromToken(String token){
        try{
            // 추가 정보를 가진 그릇 획득
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token)
                    .getBody();
            // 이메일 키값을 넣어서 획득
            // return claims.get("email", String.class);
            return claims.getSubject(); // JWT의 Payload에는 "email" 필드가 없고, "sub"(subject) 필드에 이메일이 저장되어 있다
        }catch (ExpiredJwtException e){
            System.out.println("getEmailFromToken() 기간 만료 토큰 오류");
            throw e;
        }catch (Exception e){
            System.out.println("getEmailFromToken() 토큰 디코딩과정 일반 오류(서명오류), 토큰 조작(손실)되었다 ");
            throw e;
        }
    }
}
