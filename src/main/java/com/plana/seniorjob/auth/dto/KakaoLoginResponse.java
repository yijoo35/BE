package com.plana.seniorjob.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginResponse {
    private Long userId;
    private String kakaoId;
    private String name;
    private String accessToken;
    private boolean isNewUser;
}