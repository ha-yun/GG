package com.example.msalive.controller;

import com.example.msalive.dto.LiveMessageDto;
import com.example.msalive.service.LiveProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveController {
    private final LiveProducer liveProducer;

    public LiveController(LiveProducer liveProducer) {
        this.liveProducer = liveProducer;
    }

    @MessageMapping("/send")
    public void sendMessage( LiveMessageDto liveMessageDto) {
//        liveProducer.sendMessage(liveMessageDto.toString());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(liveMessageDto); // JSON 변환
            liveProducer.sendMessage(jsonMessage); // Kafka에 JSON으로 전송
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public String chatPage() {
        return "live";
    }


}
