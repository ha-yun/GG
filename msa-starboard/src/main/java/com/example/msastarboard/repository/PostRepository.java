package com.example.msastarboard.repository;

import com.example.msastarboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword); // 제목으로 게시글 검색

    List<Post> findByContentContaining(String keyword); // 내용으로 게시글 검색
}
