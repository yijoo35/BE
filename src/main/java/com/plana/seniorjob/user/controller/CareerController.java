package com.plana.seniorjob.user.controller;

import com.plana.seniorjob.user.dto.CareerRequest;
import com.plana.seniorjob.user.dto.CareerResponse;
import com.plana.seniorjob.user.entity.Career;
import com.plana.seniorjob.user.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="이력서 경력 테이블", description = "사용자의 경력 정보(회사, 기간 등)")
@RestController
@RequestMapping("/api/resumes/{resumeId}/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerService careerService;

    @PostMapping
    public ResponseEntity<CareerResponse> addCareer(
            @PathVariable Long resumeId,
            @RequestBody CareerRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 현재 사용자 ID 추출 (권한 검증에 사용)
        Long currentUserId = Long.parseLong(userDetails.getUsername());

        Career savedCareer = careerService.addCareer(resumeId, currentUserId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CareerResponse.from(savedCareer));
    }

    @PutMapping("/{careerId}")
    public ResponseEntity<CareerResponse> updateCareer(
            @PathVariable Long resumeId,
            @PathVariable Long careerId,
            @RequestBody CareerRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long currentUserId = Long.parseLong(userDetails.getUsername());

        // Service의 updateCareer 메서드를 호출
        Career updatedCareer = careerService.updateCareer(careerId, currentUserId, request);

        return ResponseEntity.ok(CareerResponse.from(updatedCareer));
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareer(
            @PathVariable Long resumeId,
            @PathVariable Long careerId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long currentUserId = Long.parseLong(userDetails.getUsername());

        careerService.deleteCareer(careerId, currentUserId);

        return ResponseEntity.noContent().build();
    }
}