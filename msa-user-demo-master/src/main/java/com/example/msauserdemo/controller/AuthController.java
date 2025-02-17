package com.example.msauserdemo.controller;

import com.example.msauserdemo.dto.LoginReqDto;
import com.example.msauserdemo.jwt.JwtTokenProvider;
import com.example.msauserdemo.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 관리

    // ✅ 로그인 (중복 제거 및 사용자 정보 반환)
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        String accessToken = authService.login(loginReqDto, response);

        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패"));
        }

        // 🔥 JWT 토큰에서 사용자 정보 추출
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

        return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "accessToken", accessToken,  // ✅ JWT 토큰 반환
                "email", email,  // ✅ 사용자 이메일 반환
                "role", role // ✅ 사용자 역할 반환
        ));
    }

    // ✅ OPTIONS 요청 처리 (Preflight CORS 요청 허용)
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    // ✅ 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.ok("로그아웃 성공");
    }
}
