package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.counseling.entity.Counseling;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_resume")
@Getter
@Setter
@Accessors(chain = true)
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

    @OneToMany(mappedBy = "userResume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @Column(name = "total_career_years")
    private Integer totalCareerYears = 0; // 총 경력 (년)

    @Column(name = "total_career_months")
    private Integer totalCareerMonths = 0; // 총 경력 (월)

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
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate // 업데이트 전
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String residence, String qualificationText, String interestIndustry, String selfIntroText) {
        this.residence = residence;
        this.qualificationText = qualificationText;
        this.interestIndustry = interestIndustry;
        this.selfIntroText = selfIntroText;
    }
}