package com.plana.seniorjob.user.service;

import com.plana.seniorjob.user.dto.SelectAgencyRequest;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import com.plana.seniorjob.user.dto.UserAgencySelectResponse;
import com.plana.seniorjob.user.entity.UserAgency;
import com.plana.seniorjob.user.repository.UserAgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAgencyService {

    private final UserAgencyRepository userAgencyRepository;
    private final AgencyRepository agencyRepository;

    @Transactional
    public UserAgencySelectResponse selectAgency(SelectAgencyRequest req) {

        UserAgency user = getCurrentUser();

        Agency agency = agencyRepository.findByOrgCd(req.getOrgCd())
                .orElseThrow(() -> new RuntimeException("해당 기관을 찾을 수 없습니다."));

        if (req.getTel() != null && !req.getTel().isBlank()) {
            agency.setTel(req.getTel());
        }
        if (req.getZipAddr() != null && !req.getZipAddr().isBlank()) {
            agency.setZipAddr(req.getZipAddr());
        }
        if (req.getDtlAddr() != null && !req.getDtlAddr().isBlank()) {
            agency.setDtlAddr(req.getDtlAddr());
        }
        if (req.getZipCode() != null && !req.getZipCode().isBlank()) {
            agency.setZipCode(req.getZipCode());
        }

        agencyRepository.save(agency);

        user.setAgency(agency);
        userAgencyRepository.save(user);

        return new UserAgencySelectResponse(
                user.getId(),
                user.getUsername(),
                agency.getOrgCd(),
                agency.getOrgName()
        );
    }

    private UserAgency getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userAgencyRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 기관유저 정보를 찾을 수 없습니다."));
    }
}