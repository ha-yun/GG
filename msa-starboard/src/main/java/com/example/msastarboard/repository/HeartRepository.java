package com.example.msastarboard.repository;

import com.example.msastarboard.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
