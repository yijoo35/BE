package com.plana.seniorjob.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "일자리 공고 목록(item) DTO")
public class JobListItemDTO {

    @Schema(description = "공고 고유 ID")
    private String jobId;        // 공고 ID
    @Schema(description = "사업자명")
    private String oranNm;       // 사업자명
    @Schema(description = "공고 제목")
    private String recrtTitle;   // 공고 제목
    @Schema(description = "직종")
    private String jobclsNm;     // 직종명
    @Schema(description = "주소")
    private String region;    // 주소 (구까지)
    @Schema(description = "마감일")
    private String toDd;         // 마감일
    @Schema(description = "마감여부")
    private String deadline;     // 마감 여부
    @Schema(description = "생성일")
    private String createDy;     // 생성일(최신순 정렬용)
}