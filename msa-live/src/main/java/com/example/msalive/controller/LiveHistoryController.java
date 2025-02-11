package com.example.msalive.controller;

import com.example.msalive.service.LiveConsumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class LiveHistoryController {
    private final LiveConsumer liveConsumer;

    public LiveHistoryController(LiveConsumer liveConsumer) {
        this.liveConsumer = liveConsumer;
    }

    @GetMapping("/history")
    public List<String> getLiveHistory(){
        return liveConsumer.getChatHistory();
    }
}
