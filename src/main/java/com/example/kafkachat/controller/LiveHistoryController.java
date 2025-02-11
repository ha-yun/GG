package com.example.kafkachat.controller;

import com.example.kafkachat.kafka.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class LiveHistoryController {
    private final KafkaConsumer kafkaConsumer;

    @Autowired
    public LiveHistoryController(KafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    @GetMapping("/history")
    public List<String> getLiveHistory(){
        return kafkaConsumer.getChatHistory();
    }
}