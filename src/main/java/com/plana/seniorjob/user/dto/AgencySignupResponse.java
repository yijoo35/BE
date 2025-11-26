package com.plana.seniorjob.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter @Setter
public class AgencySignupResponse {
    private Long userId;
    private String username;

    private String orgCd;       
    private String orgName;

    private String message;
}
