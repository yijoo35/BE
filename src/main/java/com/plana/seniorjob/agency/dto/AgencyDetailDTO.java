package com.plana.seniorjob.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AgencyDetailDTO {
    private String orgCd;     // 기관 코드
    private String orgName;   // 기관명
    private String zipAddr;   // 도로명 주소
    private String dtlAddr;   // 상세 주소
    private String tel;       // 전화번호
    private String orgType;   // 기관 유형
}