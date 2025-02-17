package com.example.msauserdemo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.raw_secret_key}")
    private String rawSecretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(rawSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ JWT에서 Role 가져오기
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("roles", String.class); // roles 필드에서 역할 가져오기
    }

    // ✅ JWT에서 Email 가져오기
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject(); // subject = email
    }

    // ✅ Access Token 생성
    public String createAccessToken(String email, String roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(email) // 이메일을 subject로 저장
                .claim("roles", roles) // 역할 추가
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact(); // ❌ parseClaimsJws 제거
    }

    // ✅ Refresh Token 생성
    public String createRefreshToken() {
        return createToken(null, null, refreshTokenExpiration);
    }

    private String createToken(String email, String roles, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        if (email != null) {
            claims.put("email", email);
        }
        if (roles != null) {
            claims.put("roles", roles);
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ JWT에서 Claims 추출
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
