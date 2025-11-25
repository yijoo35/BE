package com.plana.seniorjob.job.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_posting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String jobId;          // 공고ID

    // 목록 + 상세 필드 통합
    private String recrtTitle;     // 제목
    private String emplymShp;      // 고용형태 코드
    private String emplymShpNm;    // 고용형태명
    private String frDd;           // 시작 접수일
    private String toDd;           // 종료 접수일
    private String oranNm;         // 기업명
    private String deadline;       // 마감 여부
    private String workPlc;        // 근무지 코드
    private String workPlcNm;      // 근무지명
    private String acptMthd;       // 목록 응답 접수방법
    private String acptMthdCd;     // 상세 응답 접수방법 코드
    private String clerk;          // 담당자
    private String clerkContt;     // 담당자 연락처
    private String clltPrnnum;     // 모집 인원
    private String createDy;       // 생성일자
    @Column(length = 4000)
    private String detCnts;        // 상세 내용
    private String etcItm;         // 기타사항
    private String plDetAddr;      // 주소
    private String plbizNm;        // 사업장명
    private String repr;           // 대표자명
    private String updrId;          // 변경일자
}
