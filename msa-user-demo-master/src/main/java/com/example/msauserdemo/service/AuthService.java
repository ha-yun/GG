package com.example.msauserdemo.service;

import com.example.msauserdemo.dto.LoginReqDto;
import com.example.msauserdemo.entity.UserEntity;
import com.example.msauserdemo.jwt.JwtTokenProvider;
import com.example.msauserdemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import com.example.msauserdemo.dto.LoginReqDto;
//import com.example.msauserdemo.entity.UserEntity;
//import com.example.msauserdemo.jwt.JwtTokenProvider;
//import com.example.msauserdemo.repository.UserRepository;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private TokenService tokenService;
//
//    public String login(LoginReqDto loginReqDto, HttpServletResponse response) {
//        String email = loginReqDto.getEmail();
//        String password = loginReqDto.getPassword();
//        try{
//            UserEntity userEntity = userRepository.findByEmail(email)
//                    .orElseThrow( ()-> new IllegalArgumentException("해당 이메일이 없습니다") );
//
//            if( !passwordEncoder.matches(password, userEntity.getPassword()) ) {
//                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
//            }
//            String accessToken = jwtTokenProvider.createAccessToken(email, password);
//            // ✅ 토큰을 헤더에 추가
//            response.addHeader("Authorization", "Bearer " + accessToken);
//            String refreshToken = tokenService.getRefreshToken(email);
//
//            if( refreshToken == null ) {
//                refreshToken = jwtTokenProvider.createRefreshToken();
//                tokenService.saveRefreshToken(email, refreshToken);
//            }
//            response.addHeader("RefreshToken", refreshToken);
//            response.addHeader("AccessToken", accessToken);
//            response.addHeader("X-Auth-User", email);
//
//        } catch (Exception e) {
//            System.out.println("로그인시 오류 발생" + e.getMessage());
//            return "로그인 실패";
//        }
//        return "로그인 성공";
//    }
//
//    public void logout(String email, String accessToken) {
//        if( !jwtTokenProvider.validateToken(accessToken) ) {
//            throw new IllegalArgumentException("변조된 토큰");
//        }
//        tokenService.deleteRefreshToken(email);
//    }
//}
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private TokenService tokenService;

    public String login(LoginReqDto loginReqDto, HttpServletResponse response) {
        String email = loginReqDto.getEmail();
        String password = loginReqDto.getPassword();

        try {
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일이 없습니다"));

            if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
            }

            // ✅ JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(email, userEntity.getRoles());
            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("JWT 토큰 생성 실패");
            }

            // ✅ 토큰을 헤더에 추가
            response.addHeader("Authorization", "Bearer " + accessToken);

            System.out.println("✅ JWT 토큰 생성 완료: " + accessToken); // 디버깅 로그 추가

            return accessToken; // ✅ JWT 토큰 반환
        } catch (Exception e) {
            System.out.println("🚨 로그인 오류 발생: " + e.getMessage());
            return null;
        }
    }

    public void logout(String email, String accessToken) {

    }

    public void logout(String accessToken) {
    }
}
