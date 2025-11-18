package com.plana.seniorjob.agency.controller;

import com.plana.seniorjob.agency.service.AgencyFetchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기관 데이터 삽입", description = "기관 데이터를 넣는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AgencyAdminController {

    private final AgencyFetchService agencyFetchService;

    @PostMapping("/fetch-agencies")
    public String fetch() {
        agencyFetchService.fetchAndSaveAllAgencies();
        return "OK";
    }
}