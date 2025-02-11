package com.example.msalive.controller;

import com.example.msalive.dto.LiveMessageDto;
import com.example.msalive.service.LiveConsumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/live")
public class LiveHistoryController {
    private final LiveConsumer liveConsumer;

    public LiveHistoryController(LiveConsumer liveConsumer) {
        this.liveConsumer = liveConsumer;
    }

    @GetMapping("/history")
    public List<LiveMessageDto> getLiveHistory(){
        return liveConsumer.getLiveHistory();
    }
}
