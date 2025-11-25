package com.plana.seniorjob.auth.service;

import com.plana.seniorjob.auth.dto.KakaoLoginResponse;
import com.plana.seniorjob.auth.dto.KakaoUserInfo;
import com.plana.seniorjob.auth.service.KakaoOAuthService;
import com.plana.seniorjob.global.jwt.JwtTokenProvider;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.enums.MemberType;
import com.plana.seniorjob.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserEntityRepository userRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public KakaoLoginResponse login(String code) {

        // 카카오 AccessToken 발급
        String kakaoAccessToken = kakaoOAuthService.getAccessToken(code);

        // 사용자 정보 조회
        KakaoUserInfo kakaoInfo = kakaoOAuthService.getUserInfo(kakaoAccessToken);

        // DB에서 카카오 사용자 조회
        UserEntity user = userRepo.findByKakaoId(kakaoInfo.getKakaoId()).orElse(null);

        boolean isNewUser = false;

        // 신규 유저 → 가입
        if (user == null) {
            user = UserEntity.builder()
                    .kakaoId(kakaoInfo.getKakaoId())
                    .name(kakaoInfo.getName())
                    .email(kakaoInfo.getEmail())
                    .gender(kakaoInfo.getGender())
                    .phoneNumber(kakaoInfo.getPhoneNumber())
                    .birthyear(kakaoInfo.getBirthyear())
                    .memberType(MemberType.NORMAL)
                    .build();

            userRepo.save(user);
            isNewUser = true;
        }

        String serverJwtToken = jwtTokenProvider.createToken(user.getKakaoId());

        return new KakaoLoginResponse(
                user.getId(),
                user.getKakaoId(),
                user.getName(),
                serverJwtToken,
                isNewUser
        );
    }
}