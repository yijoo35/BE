package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.dto.AgencyAutocompleteDTO;
import com.plana.seniorjob.agency.dto.AgencyDetailDTO;
import com.plana.seniorjob.agency.dto.AgencySearchDTO;
import com.plana.seniorjob.agency.dto.AgencyUpdateRequest;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import com.plana.seniorjob.agency.service.AgencyLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity<?> searchAgency(
            @RequestParam String keyword,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng
    ) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok("검색 결과가 없습니다.");
        }

        List<Agency> agencies = agencyRepository.searchByKeyword(keyword);

        if (agencies.isEmpty()) {
            return ResponseEntity.ok("검색 결과가 없습니다.");
        }

        // 위치 없을 경우
        if (lat == null || lng == null) {
            List<AgencySearchDTO> dtoList = agencies.stream()
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

            return ResponseEntity.ok(dtoList);
        }

        List<AgencySearchDTO> dtoList = agencies.stream()
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
                //거리순 정렬
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "기관 선택 후 상세 정보")
    @GetMapping("/{orgCd}")
    public AgencyDetailDTO getAgencyDetail(@PathVariable String orgCd) {

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
            summary = "기관명 검색 자동완성", description = "자동완성 검색, 리스트가 반환됨"
    )
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<Agency> agencies = agencyRepository.autocomplete(keyword, PageRequest.of(0, 10));

        List<AgencyAutocompleteDTO> result = agencies.stream()
                .map(a -> new AgencyAutocompleteDTO(
                        a.getOrgCd(),
                        a.getOrgName()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}