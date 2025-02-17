package com.example.msastarboard.controller;

import com.example.msastarboard.entity.Post;
import com.example.msastarboard.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class
PostController {

    private final PostService postService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${msa.user.service.url}")
    private String msaUserServiceUrl;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 생성 (연예인 전용)
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestPart("post") Post post,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestHeader("X-Auth-User") String userEmail,
                                           @RequestHeader("X-Auth-Role") String role) throws IOException {
        if(role.equals("ROLE_STAR")) {
            System.out.println(" 게시글 생성"+post);
            Post createdPost = postService.createPost(post, userEmail, image);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new Post(), HttpStatus.OK);
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @RequestHeader("X-Auth-User") String userEmail) {
        postService.deletePost(id, userEmail);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 모든 게시글 조회
    @GetMapping("")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword,
                                                  @RequestParam String filterType) {
        List<Post> posts = postService.searchPosts(keyword, filterType);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 게시글 수정 (연예인 전용)
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
                                           @RequestPart("post") String postJson,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestHeader("X-Auth-User") String userEmail) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Post updatedPost = objectMapper.readValue(postJson, Post.class);
        Post result = postService.updatePost(id, updatedPost, userEmail, image);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
