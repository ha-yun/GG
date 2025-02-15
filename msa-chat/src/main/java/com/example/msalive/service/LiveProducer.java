package com.example.msalive.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LiveProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(String message) {
        kafkaTemplate.send("live-room", message);
    }
}
