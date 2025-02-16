package com.example.msauserdemo.controller;

import com.example.msauserdemo.dto.UserDto;
import com.example.msauserdemo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDto userDto) {
        System.out.println("회원가입요청 : " + userDto.toString());
        userService.createUser( userDto);
        return ResponseEntity.ok("회원가입 성공");
    }
    // @GetMapping("/vaild")
    // public ResponseEntity<String> vaild(@RequestParam("token") String token) {
    //     try {
    //         // 고객 테이블 업데이트 - enable : f->t (유효할때만)
    //         userService.updateActivate(token);
    //         return ResponseEntity.ok("이메일 인증 완료. 계정이 활성화 되었습니다.");
    //     } catch (IllegalArgumentException e) {
    //         // 조작된(만료된) 토큰을 인증 -> 비정상, Bad Request
    //         return ResponseEntity.status(400).body("비정상, Bad Request : " + e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body("서버측 내부 오류 : " + e.getMessage());
    //     }

    // }
}
