package com.example.kafkachat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMsg(String topic, KafkaDto kafkaDto) {
        try {
            String message = objectMapper.writeValueAsString(kafkaDto);
            System.out.println("Kafka 메시지 전송: " + message);
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            System.err.println("Kafka 메시지 직렬화 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Kafka 메시지 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
