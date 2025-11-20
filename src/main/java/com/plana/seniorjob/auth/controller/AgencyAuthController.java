package com.plana.seniorjob.auth.controller;

import com.plana.seniorjob.user.dto.*;
import com.plana.seniorjob.user.service.AgencySignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="기관 회원가입/로그인", description = "기관 회원가입 및 로그인 API")
@RestController
@RequestMapping("/auth/agency")
@RequiredArgsConstructor
public class AgencyAuthController {

    private final AgencySignupService signupService;

    //회원가입
    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<AgencySignupResponse> signup(@RequestBody AgencySignupRequest req) {
        AgencySignupResponse response = signupService.signup(req);
        return ResponseEntity.ok(response);
    }

    //로그인
    @Operation(summary = "로그인 API", description = "accessToken으로 인증")
    @PostMapping("/login")
    public ResponseEntity<AgencyLoginResponse> login(@RequestBody AgencyLoginRequest req) {
        AgencyLoginResponse response = signupService.login(req);
        return ResponseEntity.ok(response);
    }
}