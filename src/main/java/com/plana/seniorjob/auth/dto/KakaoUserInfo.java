package com.plana.seniorjob.auth.dto;

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
    private String birthyear;
}
