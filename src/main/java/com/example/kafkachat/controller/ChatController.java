package com.example.kafkachat.controller;

import com.example.kafkachat.KafkaDto;
import com.example.kafkachat.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @MessageMapping("/chat.send")  // 클라이언트가 보낸 메시지를 처리
    public void sendMessage(@Payload KafkaDto kafkaDto) {
        try {
            System.out.println("WebSocket으로 받은 메시지: " + kafkaDto);
            kafkaProducer.sendMsg("chat-topic", kafkaDto);
        } catch (Exception e) {
            System.err.println("WebSocket 메시지 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
