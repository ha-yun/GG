package com.example.msastarboard.service;

import com.example.msastarboard.entity.Comment;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    // 댓글 생성
    public Comment createComment(Comment comment, String userEmail) {

        comment.setAuthorId(userEmail);
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id, String userEmail) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getAuthorId().equals(userEmail)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        commentRepository.deleteById(id);
    }
}
