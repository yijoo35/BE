package com.plana.seniorjob.global.jwt;

import com.plana.seniorjob.user.entity.UserAgency;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.repository.UserAgencyRepository;
import com.plana.seniorjob.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAgencyRepository userAgencyRepo;
    private final UserEntityRepository userEntityRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 기관 사용자 찾기
        UserAgency agencyUser = userAgencyRepo.findByUsername(username).orElse(null);
        if (agencyUser != null) {
            return new User(
                    agencyUser.getUsername(),
                    agencyUser.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_AGENCY"))
            );
        }

        // 카카오 사용자 찾기
        UserEntity kakaoUser = userEntityRepo.findByKakaoId(username).orElse(null);
        if (kakaoUser != null) {
            return new User(
                    kakaoUser.getKakaoId(),   // password는 null 가능 → social login은 password 미인증 방식으로 처리
                    "",
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
}
