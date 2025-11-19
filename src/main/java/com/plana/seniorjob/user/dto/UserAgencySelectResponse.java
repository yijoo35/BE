package com.plana.seniorjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAgencySelectResponse {

    private Long userId;
    private String username;

    private String orgCd;
    private String orgName;
}