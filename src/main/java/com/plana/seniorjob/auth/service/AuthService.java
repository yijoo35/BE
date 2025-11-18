package com.plana.seniorjob.auth.service;

import com.plana.seniorjob.auth.dto.KakaoUserInfo;
import com.plana.seniorjob.user.enums.MemberType;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.enums.MemberType;
import com.plana.seniorjob.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;

    public UserEntity login(String code) {

        KakaoUserInfo info = kakaoOAuthService.getUserInfoFromCode(code);

        return userRepository.findByKakaoId(info.getKakaoId())
                .orElseGet(() -> createUser(info));
    }

    private UserEntity createUser(KakaoUserInfo info) {

        UserEntity user = UserEntity.builder()
                .kakaoId(info.getKakaoId())
                .name(info.getName())
                .email(info.getEmail())
                .gender(info.getGender())
                .birthyear(info.getBirthyear())
                .build();

        return userRepository.save(user);
    }
}
