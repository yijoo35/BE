package com.plana.seniorjob.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class KakaoUserInfo {
    private String kakaoId;
    private String name;
    private String email;
    private String gender;
    private String phoneNumber; // 추가
    private String birthyear;
}
