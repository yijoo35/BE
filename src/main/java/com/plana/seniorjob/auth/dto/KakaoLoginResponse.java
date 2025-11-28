package com.plana.seniorjob.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "카카오 로그인 응답 DTO")
public class KakaoLoginResponse {
    private Long userId;
    private String kakaoId;
    private String name;
    private String accessToken;
    private boolean isNewUser;
}