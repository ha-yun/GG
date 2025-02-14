package com.example.msastarboard.service;

import com.example.msastarboard.entity.Heart;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.repository.HeartRepository;
import com.example.msastarboard.repository.PostRepository; // PostRepository 추가
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository, PostRepository postRepository) {
        this.heartRepository = heartRepository;
        this.postRepository = postRepository;
    }

    // 좋아요 추가 (중복 방지 로직 추가)
    public Heart addHeart(Heart heart) {
        // 게시글이 존재하는지 확인
        if (!postRepository.existsById(heart.getPost().getId())) {
            throw new ResourceNotFoundException("Post not found");
        }

        // 이미 좋아요를 누른 사용자인지 확인
        if (heartRepository.existsByUserIdAndPostId(heart.getUserId(), heart.getPost().getId())) {
            throw new IllegalStateException("Already liked this post");
        }

        return heartRepository.save(heart);
    }

    // 좋아요 제거
    public void removeHeart(Long id) {
        heartRepository.deleteById(id);
    }
}
