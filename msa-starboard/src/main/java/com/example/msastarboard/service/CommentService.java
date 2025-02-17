package com.example.msastarboard.service;

import com.example.msastarboard.entity.Comment;
import com.example.msastarboard.entity.Post;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.repository.CommentRepository;
import com.example.msastarboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Autowired
    private PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 댓글 생성
    public Comment createComment(Comment comment, String userEmail) {

        Long userId = postService.getUserIdFromEmail(userEmail);
        comment.setAuthorId(userId);

        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id, String userEmail) {

        Long userId = postService.getUserIdFromEmail(userEmail);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        commentRepository.deleteById(id);
    }

    // 댓글 수정
    public Comment updateComment(Long id, Comment updatedComment, String userEmail) {

        Long userId = postService.getUserIdFromEmail(userEmail);
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!existingComment.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }

        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return commentRepository.findByPost_Id(postId);
    }
}
