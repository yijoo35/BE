package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.dto.AgencyAutocompleteDTO;
import com.plana.seniorjob.agency.dto.AgencyDetailDTO;
import com.plana.seniorjob.agency.dto.AgencySearchDTO;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import com.plana.seniorjob.agency.service.AgencyLocationService;
import com.plana.seniorjob.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기관 검색", description = "기관명 검색 API")
@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyRepository agencyRepository;
    private final AgencyLocationService agencyLocationService;

    @Operation(
            summary = "기관명 검색", description = "두 글자 이상 검색, 리스트가 반환됨"
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AgencySearchDTO>>> searchAgency(
            @Parameter(description = "검색어")
            @RequestParam String keyword,

            @Parameter(description = "위도(필수값X)")
            @RequestParam(required = false) Double lat,

            @Parameter(description = "경도(필수값X)")
            @RequestParam(required = false) Double lng
    ) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<AgencySearchDTO>>builder()
                            .message("검색 결과가 없습니다.")
                            .data(List.of())
                            .build()
            );
        }

        List<Agency> agencies = agencyRepository.searchByKeyword(keyword);

        if (agencies.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<AgencySearchDTO>>builder()
                            .message("검색 결과가 없습니다.")
                            .data(List.of())
                            .build()
            );
        }

        List<AgencySearchDTO> dtoList;

        // 위도 & 경도 없을 때
        if (lat == null || lng == null) {
            dtoList = agencies.stream()
                    .map(a -> new AgencySearchDTO(
                            a.getOrgCd(),
                            a.getOrgName(),
                            a.getZipAddr(),
                            a.getDtlAddr(),
                            a.getTel(),
                            a.getLat(),
                            a.getLng(),
                            null,
                            null
                    ))
                    .toList();

            return ResponseEntity.ok(
                    ApiResponse.<List<AgencySearchDTO>>builder()
                            .message("검색 성공")
                            .data(dtoList)
                            .build()
            );
        }

        // 위치 기반 검색
        dtoList = agencies.stream()
                .map(a -> {
                    double dist = agencyLocationService.calculateDistance(
                            lat, lng,
                            a.getLat(), a.getLng()
                    );

                    return new AgencySearchDTO(
                            a.getOrgCd(),
                            a.getOrgName(),
                            a.getZipAddr(),
                            a.getDtlAddr(),
                            a.getTel(),
                            a.getLat(),
                            a.getLng(),
                            dist,
                            agencyLocationService.formatDistance(dist)
                    );
                })
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .toList();

        return ResponseEntity.ok(
                ApiResponse.<List<AgencySearchDTO>>builder()
                        .message("검색 성공")
                        .data(dtoList)
                        .build()
        );
    }

    @Operation(summary = "기관 선택 후 상세 정보")
    @GetMapping("/{orgCd}")
    public AgencyDetailDTO getAgencyDetail(
            @Parameter(description = "기관 고유 번호")
            @PathVariable String orgCd
    ) {

        Agency agency = agencyRepository.findByOrgCd(orgCd)
                .orElseThrow(() -> new RuntimeException("기관을 찾을 수 없습니다."));

        return new AgencyDetailDTO(
                agency.getOrgCd(),
                agency.getOrgName(),
                agency.getZipAddr(),
                agency.getDtlAddr(),
                agency.getTel()
        );
    }

    @Operation(
            summary = "기관명 검색 자동완성",
            description = "자동완성 검색, 리스트가 반환됨"
    )
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse<List<AgencyAutocompleteDTO>>> autocomplete(
            @Parameter(description = "검색어")
            @RequestParam String keyword
    ) {

        // 검색어 없음
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<AgencyAutocompleteDTO>>builder()
                            .message("검색 결과가 없습니다.")
                            .data(List.of())
                            .build()
            );
        }

        List<Agency> agencies = agencyRepository.autocomplete(keyword, PageRequest.of(0, 10));

        // 검색 결과 없음
        if (agencies.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<AgencyAutocompleteDTO>>builder()
                            .message("검색 결과가 없습니다.")
                            .data(List.of())
                            .build()
            );
        }

        // 정상 결과
        List<AgencyAutocompleteDTO> result = agencies.stream()
                .map(a -> new AgencyAutocompleteDTO(
                        a.getOrgCd(),
                        a.getOrgName()
                ))
                .toList();

        return ResponseEntity.ok(
                ApiResponse.<List<AgencyAutocompleteDTO>>builder()
                        .message("검색 성공")
                        .data(result)
                        .build()
        );
    }
}