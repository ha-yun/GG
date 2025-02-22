package com.example.products.kafka;


import com.example.products.dto.PaymentDto;
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

    public void sendMsg(String topic, String message) { kafkaTemplate.send(topic, message); }
    public void sendMsg(String topic, PaymentDto paymentDto) throws JsonProcessingException {
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(paymentDto));
    }
}
