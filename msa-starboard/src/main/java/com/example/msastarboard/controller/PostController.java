package com.example.msastarboard.controller;

import com.example.msastarboard.entity.Post;
import com.example.msastarboard.service.PostService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 생성 (연예인 전용)
    @PostMapping("/create")
    public Post createPost(@RequestPart("post") Post post,
                           @RequestPart(value = "image", required = false) MultipartFile image,
                           @RequestHeader("User-Id") Long userId) throws IOException {
        return postService.createPost(post, userId, image);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    // 모든 게시글 조회
    @GetMapping("/")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // 게시글 검색
    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String keyword, @RequestParam String filterType) {
        return postService.searchPosts(keyword, filterType);
    }

    // 게시글 수정 (연예인 전용)
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id,
                           @RequestPart("post") Post post,
                           @RequestPart(value = "image", required = false) MultipartFile image,
                           @RequestHeader("User-Id") Long userId) throws IOException {
        return postService.updatePost(id, post, userId, image);
    }
}
