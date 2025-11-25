package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.counseling.entity.Counseling;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_resume")
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_id", nullable = false, unique = true)
    private Counseling counseling;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity client;

    private Integer age;
    @Column(length = 10)
    private String gender;

    @Column(length = 50)
    private String residence; // 거주지 정보 추가

    @Column(name = "qualification_text", columnDefinition = "TEXT")
    private String qualificationText; // 자격 사항

    @Column(name = "career_text", columnDefinition = "TEXT")
    private String careerText; // 경력 사항

    @Column(name = "interest_industry", length = 50)
    private String interestIndustry; // 관심 업종

    @Column(name = "self_intro_text", columnDefinition = "TEXT")
    private String selfIntroText; // 자기소개

    @Column(name = "embedding_vector", columnDefinition = "TEXT")
    private String embeddingVector; // AI 추천을 위한 임베딩 값


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- 엔티티 생명 주기 관리 ---
    @PrePersist // 저장 전
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate // 업데이트 전
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}