package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.dto.AgencyDetailDTO;
import com.plana.seniorjob.agency.dto.AgencySearchDTO;
import com.plana.seniorjob.agency.dto.AgencyUpdateRequest;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기관 검색/정보 수정", description = "회원가입 시 기관명 검색 & 정보 수정 API")
@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyRepository agencyRepository;

    @Operation(summary = "회원가입 시 기관명 검색", description = "두 글자 이상 검색, 리스트가 반환됨")
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
                        a.getTel()
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

    @Operation(summary = "기관 정보 수정(전화번호/주소)")
    @PostMapping("/{orgCd}/update")
    public AgencyDetailDTO updateAgencyInfo(
            @PathVariable String orgCd,
            @RequestBody AgencyUpdateRequest req
    ) {
        Agency agency = agencyRepository.findByOrgCd(orgCd)
                .orElseThrow(() -> new RuntimeException("기관을 찾을 수 없습니다."));

        // 전화번호
        if (req.getTel() != null && !req.getTel().isBlank()) {
            agency.setTel(req.getTel());
        }

        // 도로명 주소
        if (req.getZipAddr() != null && !req.getZipAddr().isBlank()) {
            agency.setZipAddr(req.getZipAddr());
        }

        // 상세 주소
        if (req.getDtlAddr() != null && !req.getDtlAddr().isBlank()) {
            agency.setDtlAddr(req.getDtlAddr());
        }

        // 우편번호
        if (req.getZipCode() != null && !req.getZipCode().isBlank()) {
            agency.setZipCode(req.getZipCode());
        }

        agencyRepository.save(agency);

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