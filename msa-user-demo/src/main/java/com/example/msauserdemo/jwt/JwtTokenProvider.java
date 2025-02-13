package com.example.msauserdemo.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        return Keys.hmacShaKeyFor(rawSecretKey.getBytes());
    }

    public String createAccessToken(String email, String roles) {
        return createToken(email, roles, accessTokenExpiration);
    }

    public String createRefreshToken() {
        return createToken(null, null, refreshTokenExpiration); // 7일 만료시간
    }

    public String createToken(String email, String roles, long expiration) {
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

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}