package com.plana.seniorjob.agency.dto;

import lombok.Data;

@Data
public class AgencyUpdateRequest {
    private String tel;
    private String zipAddr;
    private String dtlAddr;
    private String zipCode;
}