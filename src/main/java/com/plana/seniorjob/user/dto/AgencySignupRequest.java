package com.plana.seniorjob.user.dto;

import lombok.*;

@Data
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AgencySignupRequest {
    private String username;
    private String password;
    private String passwordCheck;

    private String orgCd;           // 기관 코드
    private String tel;             // 기관 전화번호
    private String zipAddr;         // 기관 주소
    private String dtlAddr;
}