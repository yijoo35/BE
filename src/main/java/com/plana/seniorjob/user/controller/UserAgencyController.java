package com.plana.seniorjob.user.controller;

import com.plana.seniorjob.user.dto.SelectAgencyRequest;
import com.plana.seniorjob.user.dto.UserAgencySelectResponse;
import com.plana.seniorjob.user.service.UserAgencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기관 선택", description = "기관회원 로그인 후 자기 기관 선택 API")
@RestController
@RequestMapping("/api/user/agency")
@RequiredArgsConstructor
public class UserAgencyController {

    private final UserAgencyService userAgencyService;

    @Operation(
            summary = "기관 선택/ 정보 수정",
            description = "기관 검색 후 선택한 기관의 정보를 수정하고, 기관 회원과 기관 연결",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/select")
    public ResponseEntity<UserAgencySelectResponse> selectAgency(
            @RequestBody SelectAgencyRequest req
    ) {
        UserAgencySelectResponse response = userAgencyService.selectAgency(req);
        return ResponseEntity.ok(response);
    }
}