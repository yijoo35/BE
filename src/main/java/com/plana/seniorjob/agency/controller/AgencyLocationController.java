package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.dto.AgencyDistanceDTO;
import com.plana.seniorjob.agency.service.AgencyLocationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agencies/location")
@RequiredArgsConstructor
@Tag(name="기관 조회 API", description = "현재 위치 기반 2km이내 기관 조회 (로그인 필요)")
public class AgencyLocationController {

    private final AgencyLocationService agencyLocationService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/nearby")
    public List<AgencyDistanceDTO> getNearbyAgencies(
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return agencyLocationService.findNearby(lat, lng);
    }
}