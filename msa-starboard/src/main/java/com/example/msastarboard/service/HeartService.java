package com.example.msastarboard.service;

import com.example.msastarboard.entity.Heart;
import com.example.msastarboard.entity.Post;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.repository.HeartRepository;
import com.example.msastarboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    @Autowired
    private PostService postService;

    public HeartService(HeartRepository heartRepository, PostRepository postRepository) {
        this.heartRepository = heartRepository;
        this.postRepository = postRepository;
    }

    // 좋아요 추가
    public Heart addHeart(Long postId, String userEmail) {

        Long userId = postService.getUserIdFromEmail(userEmail);

        if (heartRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        Heart heart = new Heart();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        heart.setPost(post);
        heart.setUserId(userId);

        return heartRepository.save(heart);
    }

    // 좋아요 제거
    public void removeHeart(Long postId, String userEmail) {

        Long userId = postService.getUserIdFromEmail(userEmail);

        Heart heart = heartRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Heart not found"));

        heartRepository.delete(heart);
    }
}
