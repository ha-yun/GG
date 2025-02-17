package com.example.msastarboard.service;

import com.example.msastarboard.entity.Post;
import com.example.msastarboard.exception.ResourceNotFoundException;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    private final RestTemplate restTemplate;

    @Value("${msa.user.service.url}")
    private String msaUserServiceUrl;

    public PostService(PostRepository postRepository, FileUploadService fileUploadService, RestTemplate restTemplate) {
        this.postRepository = postRepository;
        this.fileUploadService = fileUploadService;
        this.restTemplate = restTemplate;
    }

    // 게시글 생성 (연예인 전용)
    public Post createPost(Post post, String userEmail, MultipartFile image) throws IOException {
        logger.debug("Creating post with title: {}, content: {}, userEmail: {}", post.getTitle(), post.getContent(), userEmail);
        Long userId = getUserIdFromEmail(userEmail);
        if (!isCelebrity(userId)) {
            throw new UnauthorizedException("Only celebrities can create posts");
        }
        post.setAuthorId(userId);
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(image);
            post.setImageUrl(imageUrl);
        }
        return postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Long id, String userEmail) {
        Long userId = getUserIdFromEmail(userEmail);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
        postRepository.deleteById(id);
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 검색 (제목 또는 내용)
    public List<Post> searchPosts(String keyword, String filterType) {
        return switch (filterType.toLowerCase()) {
            case "title" -> postRepository.findByTitleContaining(keyword);
            case "content" -> postRepository.findByContentContaining(keyword);
            default -> throw new IllegalArgumentException("Invalid filter type: " + filterType);
        };
    }

    // 게시글 수정 (연예인 전용)
    public Post updatePost(Long id, Post updatedPost, String userEmail, MultipartFile image) throws IOException {
        Long userId = getUserIdFromEmail(userEmail);
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!existingPost.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(image);
            existingPost.setImageUrl(imageUrl);
        }
        return postRepository.save(existingPost);
    }

    // 연예인 권한 확인 (msa-user 서비스와 통신 필요)
    private boolean isCelebrity(Long userId) {
        try {
            String url = msaUserServiceUrl + "/user/celebrity/" + userId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            System.err.println("Error checking celebrity status: " + e.getMessage());
            return false;
        }
    }

    // 이메일로 사용자 ID 조회 (msa-user 서비스와 통신)
    public Long getUserIdFromEmail(String userEmail) {
        String url = msaUserServiceUrl + "/user/id?email=" + userEmail;
        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        return response.getBody();
    }
}
