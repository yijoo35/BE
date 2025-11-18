package com.plana.seniorjob.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter @Setter
public class AgencyLoginResponse {
    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;
}