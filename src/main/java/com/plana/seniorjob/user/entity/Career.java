package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.user.dto.CareerRequest;
import com.plana.seniorjob.user.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_career")
@Getter
@Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private UserResume userResume;

    @Column(name = "start_year", nullable = false)
    private Integer startYear;
    @Column(name = "start_month", nullable = false)
    private Integer startMonth;

    @Column(name = "end_year")
    private Integer endYear;
    @Column(name = "end_month")
    private Integer endMonth;

    @Column(name = "company_name", length=100)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Column(name = "position", length=50)
    private String position;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "is_currently_working")
    private Boolean isCurrentlyWorking;

    public void update(CareerRequest request) {
        this.startYear = request.getStartYear();
        this.startMonth = request.getStartMonth();
        this.endYear = request.getEndYear();
        this.endMonth = request.getEndMonth();
        this.companyName = request.getCompanyName();

        this.employmentType = request.getEmploymentType();

        this.position = request.getPosition();
        this.jobDescription = request.getJobDescription();
        this.isCurrentlyWorking = request.getIsCurrentlyWorking();

        // 엔티티 내부 규칙: 재직 중(`isCurrentlyWorking`=true)이면 종료일을 null로 설정
        if (Boolean.TRUE.equals(this.isCurrentlyWorking)) {
            this.endYear = null;
            this.endMonth = null;
        }
    }
}