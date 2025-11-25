package com.plana.seniorjob.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencySearchDTO {
    private String orgCd;     // 기관 코드
    private String orgName;   // 기관명
    private String zipAddr;   // 도로명 주소
    private String dtlAddr;   // 상세 주소
    private String tel;       // 전화번호

    private Double lat; //위도
    private Double lng; // 경도

    private Double distanceKm;   // km 단위
    private String distanceText; // 1.2km 로 보냄
}