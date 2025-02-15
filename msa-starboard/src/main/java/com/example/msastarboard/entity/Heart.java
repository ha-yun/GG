package com.example.msastarboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hearts")
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 좋아요가 달린 게시글

    @Column(nullable = false)
    private Long userId; // 좋아요를 누른 사용자 ID (msa-user에서 가져옴)
}
