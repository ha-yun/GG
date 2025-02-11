package com.example.msalive.controller;

import com.example.msalive.service.LiveProducer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveController {
    private final LiveProducer liveProducer;

    public LiveController(LiveProducer liveProducer) {
        this.liveProducer = liveProducer;
    }

    @MessageMapping("/send")
    public void sendMessage(String message) {
        liveProducer.sendMessage(message);
    }

    @GetMapping("/")
    public String chatPage() {
        return "live";
    }


}
