package com.example.msastarboard.controller;

import com.example.msastarboard.entity.Heart;
import com.example.msastarboard.service.HeartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hearts")
public class HeartController {

    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    // 좋아요 추가
    @PostMapping("/add")
    public Heart addHeart(@RequestBody Heart heart) {
        return heartService.addHeart(heart);
    }

    // 좋아요 제거
    @DeleteMapping("/{id}")
    public void removeHeart(@PathVariable Long id) {
        heartService.removeHeart(id);
    }
}
