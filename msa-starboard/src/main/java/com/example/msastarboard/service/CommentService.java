package com.example.msastarboard.service;

import com.example.msastarboard.entity.Comment;
import com.example.msastarboard.repository.CommentRepository;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 생성
    public Comment createComment(Comment comment) {
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
