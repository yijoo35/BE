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
}