package com.plana.seniorjob.user.controller;

import com.plana.seniorjob.user.dto.ResumeDetailResponse;
import com.plana.seniorjob.user.entity.UserResume;
import com.plana.seniorjob.user.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="이력서", description="사용자 이력서 정보조회 및 관리")
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/counseling/{counselingId}")
    public ResponseEntity<ResumeDetailResponse> getResumeByCounselingId(
            @PathVariable Long counselingId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        //  1. 현재 로그인된 사용자 ID 추출
        Long currentUserId = Long.parseLong(userDetails.getUsername());

        // 2. 서비스 호출 (상태 검증 및 권한 검증 포함)
        UserResume resume = resumeService.getResumeForCounseling(counselingId, currentUserId);

        // 3. DTO 변환 및 응답
        return ResponseEntity.ok(ResumeDetailResponse.from(resume));
    }
}