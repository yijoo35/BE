package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "기관 test API", description = "기관 데이터 확인용 API")
@RestController
@RequiredArgsConstructor
public class AgencyTestController {

    private final AgencyRepository agencyRepository;

    @GetMapping("/test/agency")
    public List<String> testAgency() {
        return agencyRepository.findAll()
                .stream()
                .map(Agency::getOrgName)
                .toList();
    }
}