package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.dto.AgencyDetailDTO;
import com.plana.seniorjob.agency.dto.AgencySearchDTO;
import com.plana.seniorjob.agency.dto.AgencyUpdateRequest;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기관 검색", description = "기관명 검색 API")
@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyRepository agencyRepository;

    @Operation(
            summary = "기관명 검색", description = "두 글자 이상 검색, 리스트가 반환됨"
    )
    @GetMapping("/search")
    public List<AgencySearchDTO> searchAgency(@RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return agencyRepository.searchByKeyword(keyword)
                .stream()
                .map(a -> new AgencySearchDTO(
                        a.getOrgCd(),
                        a.getOrgName(),
                        a.getZipAddr(),
                        a.getDtlAddr(),
                        a.getTel(),
                        a.getLat(),
                        a.getLng()
                ))
                .toList();
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
                agency.getTel(),
                agency.getOrgType()
        );
    }
}