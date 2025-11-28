package com.plana.seniorjob.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema
public class AgencySearchDTO {
    @Schema(description = "기관 고유 코드")
    private String orgCd;     // 기관 코드

    @Schema(description = "기관명")
    private String orgName;   // 기관명

    @Schema(description = "주소")
    private String zipAddr;   // 도로명 주소

    @Schema(description = "상세 주소")
    private String dtlAddr;   // 상세 주소

    @Schema(description = "전화번호")
    private String tel;       // 전화번호

    @Schema(description = "위도")
    private double lat;

    @Schema(description = "경도")
    private double lng;

    @Schema(description = "거리 값")
    private Double distanceKm;   // km 단위

    @Schema(description = "거리 km, m 변환")
    private String distanceText; // 1.2km 로 보냄
}