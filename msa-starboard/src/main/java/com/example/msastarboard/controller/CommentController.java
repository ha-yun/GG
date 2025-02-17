package com.example.msastarboard.controller;

import com.example.msastarboard.entity.Comment;
import com.example.msastarboard.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment,
                                                 @RequestHeader("X-Auth-User") String userEmail) {
        Comment createdComment = commentService.createComment(comment, userEmail);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              @RequestHeader("X-Auth-User") String userEmail) {
        commentService.deleteComment(id, userEmail);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id,
                                                 @RequestBody Comment comment,
                                                 @RequestHeader("X-Auth-User") String userEmail) {
        Comment updatedComment = commentService.updateComment(id, comment, userEmail);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
}
