package com.example.msastarboard.repository;

import com.example.msastarboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_Id(Long postId); // 게시글 ID로 댓글 조회
}
