package com.plana.seniorjob.counseling.entity;

import com.plana.seniorjob.counseling.enums.CounselingStatus;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.entity.UserResume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "counseling")
@Getter
@Setter
@NoArgsConstructor
public class Counseling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counseling_id")
    private Long counselingId;


    // 내담자 (User) FK: user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_user_id")
    private UserEntity counselor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;


    @Column(name = "counseling_date", nullable = false)
    private LocalDate counselingDate;

    @Column(name = "counseling_time", nullable = false)
    private LocalTime counselingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "counseling_status", nullable = false)
    private CounselingStatus counselingStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.counselingStatus == null) {
            this.counselingStatus = CounselingStatus.IN_MATCHING;
        }
    }

    @OneToOne(mappedBy = "counseling", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserResume userResume;
}
