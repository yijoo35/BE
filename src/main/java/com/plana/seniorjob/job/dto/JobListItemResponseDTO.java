package com.plana.seniorjob.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class JobListItemResponseDTO {
    private int totalCount;
    private List<JobListItemDTO> items;
}
