package com.example.msalive.controller;

import com.example.msalive.dto.LiveMessageDto;
import com.example.msalive.service.LiveConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//
//@RestController
//@RequestMapping("/api/live")
//public class LiveHistoryController {
//    private final LiveConsumer liveConsumer;
//
//    public LiveHistoryController(LiveConsumer liveConsumer) {
//        this.liveConsumer = liveConsumer;
//    }
//
//    @GetMapping("/history")
//    public List<LiveMessageDto> getLiveHistory(){
//        return liveConsumer.getLiveHistory();
//    }
//}
@RestController
@RequestMapping("/api/live")
public class LiveHistoryController {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LiveHistoryController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/history")
    public ResponseEntity<List<LiveMessageDto>> getChatHistory() {
        List<String> rawMessages = redisTemplate.opsForList().range("chat:history", 0, -1);
        List<LiveMessageDto> messages = new ArrayList<>();

        if (rawMessages != null) {
            for (String rawMessage : rawMessages) {
                try {
                    // 이중 JSON 디코딩 수행
                    String decodedJson = objectMapper.readValue(rawMessage, String.class);
                    LiveMessageDto message = objectMapper.readValue(decodedJson, LiveMessageDto.class);
                    messages.add(message);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return ResponseEntity.ok(messages);
    }
}

