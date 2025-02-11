package com.example.kafkachat.kafka;


import com.example.kafkachat.KafkaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CHAT_HISTORY_KEY = "chat:history";

    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(SimpMessagingTemplate messagingTemplate,
                         RedisTemplate<String, String> redisTemplate,
                         ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void listenChat(String message) {
        try {
            KafkaDto kafkaDto = objectMapper.readValue(message, KafkaDto.class);
            System.out.println("Kafka Consumer received: " + kafkaDto);

            // receiverId가 null, 빈 값, 공백만 있는 값이면 오류 방지
            if (kafkaDto.getReceiverId() == null || kafkaDto.getReceiverId().trim().isEmpty()) {
                System.err.println("잘못된 receiverId: 메시지를 전송할 대상이 없습니다.");
                return;
            }

            //  목적지 경로 설정
            String destination = "/topic/chat/" + kafkaDto.getReceiverId();
            System.out.println("📨 WebSocket 전송 대상: " + destination);

            // 목적지가 올바르게 설정되었는지 확인 후 WebSocket 전송
            messagingTemplate.convertAndSend(destination, kafkaDto);

            // Redis에 메시지 저장
            redisTemplate.opsForList().rightPush("chat:history", message);
            if (redisTemplate.opsForList().size("chat:history") > 50) {
                redisTemplate.opsForList().leftPop("chat:history");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Redis에서 채팅 기록 가져오기
    public List<String> getChatHistory() {
        return redisTemplate.opsForList().range(CHAT_HISTORY_KEY, 0, -1);
    }
}

