package com.example.msastarboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    private String imageUrl; // 이미지 URL

    @Column(nullable = false)
    private Long authorId; // 작성자 ID (msa-user에서 가져옴)

    @CreatedDate
    private LocalDateTime createdAt; // 게시글 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedAt; // 게시글 수정 시간
}
