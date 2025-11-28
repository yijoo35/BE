package com.plana.seniorjob.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "자동완성 응답 DTO")
public class AgencyAutocompleteDTO { // 검색 자동완성
    @Schema(description = "기관 고유 코드")
    private String orgCd;     // 기관 코드

    @Schema(description = "기관명")
    private String orgName;   // 기관명
}