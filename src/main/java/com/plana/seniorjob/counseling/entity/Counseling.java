package com.plana.seniorjob.counseling.entity;

import com.plana.seniorjob.counseling.enums.CounselingStatus;
import com.plana.seniorjob.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counseling")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counseling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_id")
    private Long consultId;

    // 클라이언트 (상담 신청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    // 상담사
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = true)
    private UserEntity counselor;

    // 상담 진행 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CounselingStatus status;

    // 상담 신청 시간
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 최종 업데이트 시간
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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