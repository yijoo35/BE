package com.plana.seniorjob.agency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoGeocodingService {

    @Value("${kakao.local-api.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getLatLng(String address) {

        if (address == null || address.isBlank()) {
            return null;
        }

        try {
            // ★ 주소 전처리: 여러 공백 → 한 칸
            address = address.trim().replaceAll("\\s+", " ");

            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                    .queryParam("query", address)
                    .queryParam("analyze_type", "similar")
                    .queryParam("page", 1)
                    .queryParam("size", 1)
                    .build()
                    .encode(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri.toUri(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            System.out.println("Kakao response = " + response.getBody());

            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.getBody().get("documents");
            if (docs == null || docs.isEmpty()) {
                return null;
            }

            Map<String, Object> first = docs.get(0);

            double lat = Double.parseDouble((String) first.get("y"));
            double lng = Double.parseDouble((String) first.get("x"));

            return new double[]{lat, lng};

        } catch (Exception e) {
            System.out.println("Kakao 좌표 변환 실패 = " + address);
            return null; // ← 예외 던지면 DB 닫힘 → 무조건 swallow
        }
    }
}