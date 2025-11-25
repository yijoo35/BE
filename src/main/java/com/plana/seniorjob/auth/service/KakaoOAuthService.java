package com.plana.seniorjob.auth.service;

import com.plana.seniorjob.auth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.oauth.client-id}")
    private String clientId;

    @Value("${kakao.oauth.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    public String getAccessToken(String code) {

        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

        return (String) response.get("access_token");
    }

    public KakaoUserInfo getUserInfo(String accessToken) {

        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        Map<String, Object> result =
                restTemplate.exchange(url, HttpMethod.GET, request, Map.class).getBody();

        Map<String, Object> kakaoAccount = (Map<String, Object>) result.get("kakao_account");

        return new KakaoUserInfo(
                result.get("id").toString(),
                (String) kakaoAccount.get("name"),
                (String) kakaoAccount.get("email"),
                (String) kakaoAccount.get("gender"),
                (String) kakaoAccount.get("phone_number"),
                (String) kakaoAccount.get("birthyear")
        );
    }

    public KakaoUserInfo getUserInfoFromCode(String code) {
        String token = getAccessToken(code);
        return getUserInfo(token);
    }
}
