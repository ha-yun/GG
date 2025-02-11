package com.example.kafkachat.controller;

import com.example.kafkachat.KafkaDto;
//import com.example.kafkachat.service.KafkaProducer;
import com.example.kafkachat.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/send")
    public String sendMessage(@RequestBody KafkaDto kafkaDto) {
        producer.sendMsg(kafkaDto.getReceiverId(), kafkaDto);
        return "메시지 전송 완료: " + kafkaDto.getMsg();
    }
}
