package com.plana.seniorjob.user.dto;

import lombok.Data;

@Data
public class SelectAgencyRequest {
    private String orgCd;
    //수정할 정보
    private String tel;
    private String zipAddr;
    private String dtlAddr;
    private String zipCode;
}