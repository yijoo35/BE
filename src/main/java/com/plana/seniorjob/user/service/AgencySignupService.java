package com.plana.seniorjob.user.service;

import com.plana.seniorjob.agency.repository.AgencyRepository;
import com.plana.seniorjob.global.jwt.JwtTokenProvider;
import com.plana.seniorjob.global.validation.AgencySignupValidator;
import com.plana.seniorjob.user.dto.*;
import com.plana.seniorjob.user.entity.UserAgency;
import com.plana.seniorjob.user.enums.MemberType;
import com.plana.seniorjob.user.repository.UserAgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencySignupService {

    private final UserAgencyRepository userAgencyRepo;
    private final AgencySignupValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AgencyRepository agencyRepository;

    public AgencySignupResponse signup(AgencySignupRequest req) {

        validator.validate(req);

        if (userAgencyRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        String encodedPw = passwordEncoder.encode(req.getPassword());

        UserAgency user = new UserAgency();
        user.setUsername(req.getUsername());
        user.setPassword(encodedPw);
        user.setMemberType(MemberType.AGENCY);

        userAgencyRepo.save(user);

        return new AgencySignupResponse(
                user.getId(),
                user.getUsername(),
                "회원가입이 완료되었습니다."
        );
    }

    // 로그인
    public AgencyLoginResponse login(AgencyLoginRequest req) {

        UserAgency user = userAgencyRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUsername());

        return new AgencyLoginResponse(
                user.getId(),
                user.getUsername(),
                token
        );
    }
}
