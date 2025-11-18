package com.plana.seniorjob.user.dto;

import lombok.*;

@Data
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AgencyLoginRequest {
    private String username;
    private String password;
}