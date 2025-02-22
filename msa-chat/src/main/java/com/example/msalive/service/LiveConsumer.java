//package com.example.msalive.service;
//
//import com.example.msalive.dto.LiveMessageDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class LiveConsumer {
//    private final SimpMessagingTemplate messagingTemplate;
//    private final RedisTemplate redisTemplate;
//    private static final String CHAT_HISTORY_KEY = "chat:history";
//    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기
//
//    public LiveConsumer(SimpMessagingTemplate messagingTemplate, RedisTemplate redisTemplate, ObjectMapper objectMapper) {
//        this.messagingTemplate = messagingTemplate;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @KafkaListener(topics = "live-room", groupId = "test-group")
//    public void listen(String message) {
//        try{
//            // JSON 문자열 -> LiveMessageDto 객체 변환
//            LiveMessageDto liveMessageDto = objectMapper.readValue(message, LiveMessageDto.class); // JSON -> 객체 변환
//
//            // 1️⃣ 메시지를 웹소켓을 통해 클라이언트에 전송
//            messagingTemplate.convertAndSend("/topic/messages", liveMessageDto);
//
//            // 2️⃣ LiveMessageDto -> JSON 문자열로 변환 후 Redis에 저장
//            String jsonMessage = objectMapper.writeValueAsString(liveMessageDto);
//            redisTemplate.opsForList().rightPush(CHAT_HISTORY_KEY, jsonMessage);
//
//            if (redisTemplate.opsForList().size(CHAT_HISTORY_KEY) > 50) {
//                redisTemplate.opsForList().leftPop(CHAT_HISTORY_KEY);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public List<LiveMessageDto> getLiveHistory() {
//        List<String> jsonMessages = redisTemplate.opsForList().range(CHAT_HISTORY_KEY, 0, -1);
//        List<LiveMessageDto> liveHistory = new ArrayList<>();
//
//        if (jsonMessages != null) {
//            for (String json : jsonMessages) {
//                try {
//                    //  JSON을 LiveMessageDto 객체로 변환
//                    liveHistory.add(objectMapper.readValue(json, LiveMessageDto.class));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return liveHistory;
//    }
//}
package com.example.msalive.service;

import com.example.msalive.dto.LiveMessageDto;

import com.example.msalive.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String CHAT_HISTORY_KEY = "chat:history";

    @Autowired
    public LiveConsumer(
            SimpMessagingTemplate messagingTemplate,
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper,
            JwtTokenProvider jwtTokenProvider) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @KafkaListener(topics = "live-room", groupId = "test-group")
    public void listen(String message) {
        try {
            System.out.println("📩 [Kafka 수신] 원본 메시지: " + message);
            // ✅ 메시지 JSON -> 객체 변환
            LiveMessageDto liveMessageDto = objectMapper.readValue(message, LiveMessageDto.class);
            String token = liveMessageDto.getToken(); // ✅ JWT 추출
            // ✅ WebSocket을 통해 클라이언트에게 전송
            messagingTemplate.convertAndSend("/topic/messages", liveMessageDto);

            // ✅ Redis에 메시지 저장
            String jsonMessage = objectMapper.writeValueAsString(liveMessageDto);
            redisTemplate.opsForList().rightPush(CHAT_HISTORY_KEY, jsonMessage);
            System.out.println("✅ [Redis 저장] 메시지: " + jsonMessage);
            // ✅ JWT 검증
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                System.out.println("🚨 Invalid JWT Token");
                return;
            }

            // ✅ 역할 검증
            String senderRole = liveMessageDto.getRole();
            String userRole = jwtTokenProvider.getRole(token);

            if ("fan".equals(userRole) && !"star".equals(senderRole)) {
                return;
            }

            // ✅ 메시지를 WebSocket으로 전송
            messagingTemplate.convertAndSend("/topic/messages", liveMessageDto);

            // ✅ Redis에 채팅 내역 저장
            redisTemplate.opsForList().rightPush(CHAT_HISTORY_KEY, objectMapper.writeValueAsString(liveMessageDto));

            // ✅ 채팅 기록이 50개 이상이면 오래된 메시지 삭제
            if (redisTemplate.opsForList().size(CHAT_HISTORY_KEY) > 50) {
                redisTemplate.opsForList().leftPop(CHAT_HISTORY_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LiveMessageDto> getLiveHistory() {
        List<String> jsonMessages = redisTemplate.opsForList().range(CHAT_HISTORY_KEY, 0, -1);
        List<LiveMessageDto> liveHistory = new ArrayList<>();

        if (jsonMessages != null) {
            for (String json : jsonMessages) {
                try {
                    // ✅ JSON을 LiveMessageDto 객체로 변환
                    liveHistory.add(objectMapper.readValue(json, LiveMessageDto.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return liveHistory;
    }
}
