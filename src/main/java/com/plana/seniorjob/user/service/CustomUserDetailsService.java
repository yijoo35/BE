package com.plana.seniorjob.user.service;

import com.plana.seniorjob.global.jwt.CustomUserDetails;
import com.plana.seniorjob.user.entity.UserAgency;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.repository.UserAgencyRepository;
import com.plana.seniorjob.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAgencyRepository userAgencyRepo;
    private final UserEntityRepository userEntityRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 기관 회원
        UserAgency agencyUser = userAgencyRepo.findByUsername(username).orElse(null);
        if (agencyUser != null) {
            return new CustomUserDetails(agencyUser);
        }

        // 카카오 회원
        UserEntity kakaoUser = userEntityRepo.findByKakaoId(username).orElse(null);
        if (kakaoUser != null) {
            return new CustomUserDetails(kakaoUser);
        }

        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
    }
}