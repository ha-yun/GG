package com.example.kafkachat;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KafkaDto {
    private String senderId;
    private String receiverId;
    private String msg;
//    private boolean answered; // 답변 여부 추가

    @Builder
    public KafkaDto(String senderId, String receiverId, String msg, boolean answered) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msg = msg;
//        this.answered = answered;
    }
}
