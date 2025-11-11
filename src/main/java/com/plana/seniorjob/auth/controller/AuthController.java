package com.plana.seniorjob.auth.controller;

import com.plana.seniorjob.auth.service.AuthService;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.enums.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam String code,
            @RequestParam MemberType memberType
    ) {
        UserEntity user = authService.login(code, memberType);
        return ResponseEntity.ok(user);
    }
}
