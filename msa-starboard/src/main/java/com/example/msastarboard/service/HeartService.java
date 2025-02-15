package com.example.msastarboard.service;

import com.example.msastarboard.entity.Heart;
import com.example.msastarboard.repository.HeartRepository;
import org.springframework.stereotype.Service;

@Service
public class HeartService {

    private final HeartRepository heartRepository;

    public HeartService(HeartRepository heartRepository) {
        this.heartRepository = heartRepository;
    }

    // 좋아요 추가
    public Heart addHeart(Heart heart) {
        return heartRepository.save(heart);
    }

    // 좋아요 제거
    public void removeHeart(Long id) {
        heartRepository.deleteById(id);
    }
}
