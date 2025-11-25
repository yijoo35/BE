package com.plana.seniorjob.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgencyAutocompleteDTO { // 검색 자동완성

    private String orgCd;     // 기관 코드
    private String orgName;   // 기관명
}