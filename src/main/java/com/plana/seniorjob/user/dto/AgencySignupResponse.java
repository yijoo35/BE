package com.plana.seniorjob.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter @Setter
@Schema(description = "기관회원 회원가입 응답 DTO")
public class AgencySignupResponse {
    private Long userId;
    private String username;

    @Schema(description = "기관 고유 번호")
    private String orgCd;
    @Schema(description = "기관명")
    private String orgName;

    private String message;
}
