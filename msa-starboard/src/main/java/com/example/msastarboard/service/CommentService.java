package com.example.msastarboard.service;

import com.example.msastarboard.entity.Comment;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.repository.CommentRepository;
import com.example.msastarboard.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository; // PostRepository 추가

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository; // 생성자에 주입
    }

    // 댓글 생성
    public Comment createComment(Comment comment) {
        // 게시글 존재 여부 확인
        if (!postRepository.existsById(comment.getPost().getId())) {
            throw new ResourceNotFoundException("Post not found");
        }
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // 댓글 수정
    public Comment updateComment(Long id, Comment updatedComment, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }
        comment.setContent(updatedComment.getContent());
        return commentRepository.save(comment);
    }
}
