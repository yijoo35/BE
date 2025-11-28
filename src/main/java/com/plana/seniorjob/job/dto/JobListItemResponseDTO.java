package com.plana.seniorjob.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "일자리 공고 목록 전체 DTO")
public class JobListItemResponseDTO {
    @Schema(description = "공고 건수")
    private int totalCount;
    @Schema(description = "공고 목록")
    private List<JobListItemDTO> items;
}
