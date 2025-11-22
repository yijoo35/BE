package com.plana.seniorjob.agency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class KakaoGeocodingService {

    @Value("${kakao.local-api.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, double[]> cache = new ConcurrentHashMap<>();

    public double[] getLatLng(String address) {

        if (address == null || address.isBlank()) {
            return null;
        }

        address = address.trim().replaceAll("\\s+", " ");

        // 캐싱 먼저
        if (cache.containsKey(address)) {
            return cache.get(address);
        }

        // keyword 우선
        double[] coords = searchKeyword(address);

        // 실패 → address fallback
        if (coords == null) {
            coords = searchAddress(address);
        }

        if (coords != null) {
            cache.put(address, coords);
        }

        return coords;
    }

    private double[] searchKeyword(String query) {

        try {
            var uri = UriComponentsBuilder.fromHttpUrl(
                            "https://dapi.kakao.com/v2/local/search/keyword.json")
                    .queryParam("query", query)
                    .queryParam("page", 1)
                    .queryParam("size", 1)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response;
            try {
                response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            } catch (HttpClientErrorException.TooManyRequests e) {
                System.out.println("Keyword 429 → 5초 대기 후 재시도");
                sleep(5000);
                return searchKeyword(query);
            }

            List<Map<String, Object>> docs =
                    (List<Map<String, Object>>) response.getBody().get("documents");

            if (docs == null || docs.isEmpty()) return null;

            Map<String, Object> first = docs.get(0);

            System.out.println("Keyword 검색 성공: " + query);

            return new double[]{
                    Double.parseDouble((String) first.get("y")),
                    Double.parseDouble((String) first.get("x"))
            };

        } catch (Exception e) {
            return null;
        }
    }

    private double[] searchAddress(String query) {

        try {
            var uri = UriComponentsBuilder.fromHttpUrl(
                            "https://dapi.kakao.com/v2/local/search/address.json")
                    .queryParam("query", query)
                    .queryParam("analyze_type", "similar")
                    .queryParam("page", 1)
                    .queryParam("size", 1)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response;
            try {
                response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            } catch (HttpClientErrorException.TooManyRequests e) {
                System.out.println("Address 429 → 5초 대기 후 재시도");
                sleep(5000);
                return searchAddress(query);
            }

            List<Map<String, Object>> docs =
                    (List<Map<String, Object>>) response.getBody().get("documents");

            if (docs == null || docs.isEmpty()) return null;

            Map<String, Object> first = docs.get(0);

            System.out.println("Address 검색 성공: " + query);

            return new double[]{
                    Double.parseDouble((String) first.get("y")),
                    Double.parseDouble((String) first.get("x"))
            };

        } catch (Exception e) {
            return null;
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
