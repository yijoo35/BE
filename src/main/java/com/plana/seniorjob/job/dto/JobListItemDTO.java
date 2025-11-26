package com.plana.seniorjob.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class JobListItemDTO {

    private String jobId;        // 공고 ID
    private String oranNm;       // 사업자명
    private String recrtTitle;   // 공고 제목
    private String jobclsNm;     // 직종명
    private String region;    // 주소 (구까지)
    private String toDd;         // 마감일
    private String deadline;     // 마감 여부
    private String createDy;     // 생성일(최신순 정렬용)
}