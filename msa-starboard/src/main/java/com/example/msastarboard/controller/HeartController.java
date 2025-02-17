package com.example.msastarboard.controller;

import com.example.msastarboard.entity.Heart;
import com.example.msastarboard.service.HeartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Heart> addHeart(@RequestBody Heart heart,
                                          @RequestHeader("X-Auth-User") String userEmail) {
        Heart addedHeart = heartService.addHeart(heart.getPost().getId(), userEmail);
        return new ResponseEntity<>(addedHeart, HttpStatus.CREATED);
    }

    // 좋아요 제거
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeHeart(@PathVariable Long postId,
                                            @RequestHeader("X-Auth-User") String userEmail) {
        heartService.removeHeart(postId, userEmail);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
