package com.example.msalive.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate redisTemplate;
    private static final String CHAT_HISTORY_KEY = "chat:history";

    public LiveConsumer(SimpMessagingTemplate messagingTemplate, RedisTemplate redisTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "live-room", groupId = "test-group")
    public void listen(String message) {
        // 1️⃣ 메시지를 웹소켓을 통해 클라이언트에 전송
        messagingTemplate.convertAndSend("/topic/messages", message);

        // 2️⃣ 메시지를 Redis에 저장 (최대 50개 유지)
        redisTemplate.opsForList().rightPush(CHAT_HISTORY_KEY, message);
        if (redisTemplate.opsForList().size(CHAT_HISTORY_KEY) > 50) {
            redisTemplate.opsForList().leftPop(CHAT_HISTORY_KEY);
        }

    }


    // 3️⃣ Redis에서 채팅 기록 가져오기
    public java.util.List<String> getChatHistory() {
        return redisTemplate.opsForList().range(CHAT_HISTORY_KEY, 0, -1);
    }
}
