package com.plana.seniorjob.job.controller;

import com.plana.seniorjob.global.dto.ApiResponse;
import com.plana.seniorjob.job.dto.JobDetailResponseDTO;
import com.plana.seniorjob.job.dto.JobListItemResponseDTO;
import com.plana.seniorjob.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="일자리 공고 API", description = "공고 목록 및 상세 조회")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary="일자리 공고 목록 조회 API")
    @GetMapping("/list")
    public JobListItemResponseDTO getAllJobs(){
        return jobPostingService.getAllJobs();
    }

    @Operation(summary = "일자리 공고 검색 API")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<JobListItemResponseDTO>> searchJobs(
            @Parameter(description = "검색어")
            @RequestParam String keyword
    ) {
        JobListItemResponseDTO jobs = jobPostingService.search(keyword);

        if (jobs.getItems().isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<JobListItemResponseDTO>builder()
                            .message("검색 결과가 없습니다.")
                            .data(jobs) // 빈 리스트 포함
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<JobListItemResponseDTO>builder()
                        .message("검색 성공")
                        .data(jobs)
                        .build()
        );
    }

    @Operation(summary = "일자리 공고 상세 조회 API")
    @GetMapping("/detail")
    public JobDetailResponseDTO getDetailJob(
            @Parameter(description = "채용공고 ID")
            @PathVariable String jobId
    ){
        return jobPostingService.getJob(jobId);
    }
}
