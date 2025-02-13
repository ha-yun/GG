package com.example.msastarboard.service;

import com.example.msastarboard.entity.Post;
import com.example.msastarboard.repository.PostRepository;
import com.example.msastarboard.exception.UnauthorizedException;
import com.example.msastarboard.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    @Value("${celebrity.user.ids}")
    private List<Long> celebrityUserIds;

    public PostService(PostRepository postRepository, FileUploadService fileUploadService, @Value("${celebrity.user.ids}") List<Long> celebrityUserIds) {
        this.postRepository = postRepository;
        this.fileUploadService = fileUploadService;
        this.celebrityUserIds = celebrityUserIds;
    }

    // 게시글 생성 (연예인 전용)
    public Post createPost(Post post, Long userId, MultipartFile image) throws IOException {
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
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 검색 (제목, 내용, 제목+내용 필터 적용)
    public List<Post> searchPosts(String keyword, String filterType) {
        switch (filterType) {
            case "title":
                return postRepository.findByTitleContaining(keyword);
            case "content":
                return postRepository.findByContentContaining(keyword);
            case "title+content":
                return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
            default:
                return new ArrayList<>();
        }
    }

    // 게시글 수정 (연예인 전용)
    public Post updatePost(Long id, Post updatedPost, Long userId, MultipartFile image) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!post.getAuthorId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(image);
            post.setImageUrl(imageUrl);
        }
        return postRepository.save(post);
    }

    // 연예인 권한 확인 (msa-user 서비스와 통신 필요)
    private boolean isCelebrity(Long userId) {
        return celebrityUserIds.contains(userId);
    }
}
