package com.plana.seniorjob.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "기관회원 회원가입 요청 DTO")
public class AgencySignupRequest {
    private String username;
    private String password;
    private String passwordCheck;

    @Schema(description = "기관 고유 번호(필수)")
    private String orgCd;           // 기관 코드
    @Schema(description = "기관 전화번호")
    private String tel;             // 기관 전화번호
    @Schema(description = "기관 주소")
    private String zipAddr;         // 기관 주소
    @Schema(description = "기관 상세 주소")
    private String dtlAddr;
}