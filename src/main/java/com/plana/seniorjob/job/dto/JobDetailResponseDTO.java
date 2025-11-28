package com.plana.seniorjob.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "일자리 공고 상세 응답 DTO")
public class JobDetailResponseDTO {

    @Schema(description = "공고 고유 ID")
    private String jobId;

    @Schema(description = "공고 제목")
    private String recrtTitle;

    @Schema(description = "공고 사업자명")
    private String oranNm;

    @Schema(description = "마감 날짜")
    private String toDd;

    @Schema(description = "마감 여부 (마감 / 진행중)")
    private String deadline;

    @Schema(description = "고용 형태")
    private String emplymShpNm;

    @Schema(description = "직종")
    private String jobclsNm;

    @Schema(description = "모집 인원")
    private String clltPrnnum;

    @Schema(description = "상세 내용")
    private String detCnts;

    @Schema(description = "기타 사항")
    private String etcItm;

    @Schema(description = "근무지 주소")
    private String plDetAddr;

    @Schema(description = "사업자명")
    private String plbizNm;

    @Schema(description = "접수 방법")
    private String acptMthd;

    @Schema(description = "연락처")
    private String clerkContt;

    @Schema(description = "등록일자")
    private String createDy;
}