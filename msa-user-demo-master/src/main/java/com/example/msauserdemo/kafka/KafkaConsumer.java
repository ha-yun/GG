package com.example.msauserdemo.kafka;

import com.example.msauserdemo.dto.PaymentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) {
        System.out.println("프로듀서 메세지 " + message);

    }
    @KafkaListener(topics = "msa-sb-products-payment", groupId = "test-group")
    public void listen2(String message) {
        try {
            // 역직렬화
            PaymentDto orderDto = objectMapper.readValue(message, PaymentDto.class);
            System.out.println("프로듀서 메세지 " + orderDto.toString());
            // 메세지를 받고 처리할 부분 처리
            System.out.println("결제완료 이후 후속 작업 진행... ");
        }catch (Exception e){}
    }
}
