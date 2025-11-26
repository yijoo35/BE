package com.plana.seniorjob.job.controller;

import com.plana.seniorjob.job.dto.JobListItemResponseDTO;
import com.plana.seniorjob.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public JobListItemResponseDTO searchJobs(@RequestParam String keyword) {
        return jobPostingService.search(keyword);
    }
}
