package com.plana.seniorjob.job.controller;

import com.plana.seniorjob.job.service.JobPostingFetchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name="공고 데이터 삽입", description = "일자리 공고 데이터 삽입 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/jobs")
public class JobPostingAdminController {

    private final JobPostingFetchService fetchService;

    @PostMapping("/fetch")
    public String fetchJobs() {
        fetchService.fetchJobPostings();
        return "일자리 공고 저장 완료";
    }
}
