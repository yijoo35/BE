package com.plana.seniorjob.agency.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.plana.seniorjob.agency.dto.AgencyApiResponseDTO;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyFetchService {

    private final AgencyRepository agencyRepository;
    private final RestTemplate restTemplate;          // 기본 RestTemplate 사용!
    private final XmlMapper xmlMapper = new XmlMapper();

    private final KakaoGeocodingService kakaoGeocodingService;

    @Value("${public-api.key}")
    private String serviceKey;

    public void fetchAndSaveAllAgencies() {
        int page = 1;
        int rows = 100;

        while (true) {
            String url = buildUrl(page, rows);

            byte[] bytes = restTemplate.getForObject(url, byte[].class);
            if (bytes == null) break;
            String xml = new String(bytes, StandardCharsets.UTF_8);

            // XML → DTO 변환
            AgencyApiResponseDTO data = parse(xml);

            List<AgencyApiResponseDTO.AgencyItem> items = data.getBody().getItems().getItem();

            if (items == null || items.isEmpty())
                break;

            save(items);

            if (items.size() < rows) break;

            page++;
        }
    }

    private String buildUrl(int page, int rows) {
        return "https://apis.data.go.kr/B552474/JobBsnInfoService/getJobOperInsttList"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + page
                + "&numOfRows=" + rows;
    }

    private AgencyApiResponseDTO parse(String xml) {
        try {
            return xmlMapper.readValue(xml, AgencyApiResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패 → 인코딩 확인 필요", e);
        }
    }

    private void save(List<AgencyApiResponseDTO.AgencyItem> items) {
        for (AgencyApiResponseDTO.AgencyItem i : items) {

            String searchAddress = normalizeAddress(i.getZipAddr());
            System.out.println(" 좌표 요청 주소 = " + searchAddress);

            double[] coords = kakaoGeocodingService.getLatLng(searchAddress);

            Agency agency = agencyRepository.findByOrgCd(i.getOrgCd())
                    .orElse(new Agency());

            agency.setOrgCd(i.getOrgCd());
            agency.setOrgName(i.getOrgName());
            agency.setOrgType(i.getOrgTypeNm());
            agency.setZipAddr(i.getZipAddr());
            agency.setDtlAddr(i.getDtlAddr());
            agency.setZipCode(i.getZipCode());

            String tel = (i.getTelNum() != null && i.getTelNum().contains("*")) ? null : i.getTelNum();
            String fax = (i.getFaxNum() != null && i.getFaxNum().contains("*")) ? null : i.getFaxNum();
            agency.setTel(tel);
            agency.setFax(fax);

            if (coords != null) {
                agency.setLat(coords[0]);
                agency.setLng(coords[1]);
            }

            agencyRepository.save(agency);
        }
    }

    private void updateCoordinates(List<Agency> agencies) {

        for (Agency agency : agencies) {

            try {
                Thread.sleep(1000);  // too many request 방지
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            String searchAddress = normalizeAddress(agency.getZipAddr());
            System.out.println("좌표 요청 주소 = " + searchAddress);

            double[] coords = kakaoGeocodingService.getLatLng(searchAddress);

            if (coords != null) {
                agency.setLat(coords[0]);
                agency.setLng(coords[1]);
                agencyRepository.save(agency);
            } else {
                System.out.println("좌표 못찾음: " + searchAddress);
            }
        }
    }

    private String normalizeAddress(String addr) {
        if (addr == null) return null;
        return addr
                .trim()                    // 앞뒤 공백 제거
                .replaceAll("\\s+", " ");  // 중복 공백 하나로
    }

    public void updateCoordinatesManually() {
        List<Agency> target = agencyRepository.findAllWithoutCoordinates();

        for (Agency agency : target) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String searchAddress = normalizeAddress(agency.getZipAddr());
            System.out.println(" 좌표 요청 주소 = " + searchAddress);

            double[] coords = kakaoGeocodingService.getLatLng(searchAddress);

            if (coords != null) {
                agency.setLat(coords[0]);
                agency.setLng(coords[1]);
                agencyRepository.save(agency);
            } else {
                System.out.println("좌표 못찾음 = " + searchAddress);
            }
        }
    }
}