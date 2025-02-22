package com.example.msauserdemo.service;

import com.example.msauserdemo.dto.UserDto;
import com.example.msauserdemo.entity.UserEntity;
import com.example.msauserdemo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    @Autowired
//    private JavaMailSenderImpl mailSender;

    // ✅ 이메일을 기반으로 User 조회
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email) // DB에서 User 찾기
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));

        return new UserDto(user.getEmail(), user.getRoles(), user.getUsername()); // ✅ UserDto로 변환 후 반환
    }

    public void createUser(@Valid UserDto userDto) {
        if( userDto.getEmail() == null || userDto.getEmail().isEmpty() ) {
            throw new IllegalArgumentException("이메일을 입력해야 합니다");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입한 이메일입니다");
        }
        if( userDto.getUserName() == null || userDto.getUserName().isEmpty() ) {
            throw new IllegalArgumentException("이름을 입력하시오");
        }
        if( userDto.getPassword() == null || userDto.getPassword().isEmpty() ) {
            throw new IllegalArgumentException("비밀번호를 입력하시오");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(userDto.getRoles()) // 사용자 선택 값 저장
                .enable(false) // 이메일 인증 전
                .build();

        userRepository.save(userEntity);

        // sendValidEmail(userEntity);
    }
    // private void sendValidEmail(UserEntity userEntity) {
    //     String token = UUID.randomUUID().toString();
    //     redisTemplate.opsForValue().set(token, userEntity.getEmail(),1, TimeUnit.DAYS);
    //     String url = "http://localhost:8080/user/vaild?token=" + token;
    //     sendMail( userEntity.getEmail(), "이메일 인증", "링크를 눌러서 인증: " + url);
    // }

    // private void sendMail(String email, String subject, String content) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(email);
    //     message.setSubject(subject);
    //     message.setText(content);
    //     mailSender.send(message);
    // }
    public void updateActivate(String token) {
        String email = (String) redisTemplate.opsForValue().get(token);
        if (email == null) {
            throw new IllegalArgumentException("잘못된 토큰 혹은 만료된 토큰");
        }
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 오류(존재x)"));
        userEntity.setEnable(true);
        userRepository.save(userEntity);
        redisTemplate.delete(token);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
