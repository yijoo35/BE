package com.plana.seniorjob.auth.controller;

import com.plana.seniorjob.auth.service.AuthService;
import com.plana.seniorjob.auth.service.KakaoOAuthService;
import com.plana.seniorjob.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.plana.seniorjob.user.enums.MemberType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 (OAuth)", description = "카카오 로그인 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 인가 코드를 받아 액세스 토큰을 발급, 사용자 정보를 반환")
    @ApiResponse(responseCode = "200", description = "로그인 성공 (카카오 사용자 정보 반환)") // 3. 응답 설명
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {

        log.info("인가코드: {}", code);

        UserEntity user = authService.login(code, MemberType.NORMAL);

        return ResponseEntity.ok(user);
    }
}