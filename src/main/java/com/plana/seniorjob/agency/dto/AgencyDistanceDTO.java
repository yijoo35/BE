package com.plana.seniorjob.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AgencyDistanceDTO {
    private String orgCd;
    private String orgName;
    private String zipAddr;
    private String dtlAddr;
    private String tel;
    private double lat;
    private double lng;

    private double distanceKm;   // km 단위
    private String distanceText; // 1.2km 로 보냄
}