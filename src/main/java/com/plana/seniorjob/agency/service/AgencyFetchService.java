package com.plana.seniorjob.agency.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.plana.seniorjob.agency.dto.AgencyApiResponseDTO;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyFetchService {

    private final AgencyRepository agencyRepository;
    private final RestTemplate restTemplate;
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

            AgencyApiResponseDTO data = parse(xml);
            List<AgencyApiResponseDTO.AgencyItem> items = data.getBody().getItems().getItem();

            if (items == null || items.isEmpty()) break;

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
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    private void save(List<AgencyApiResponseDTO.AgencyItem> items) {
        for (AgencyApiResponseDTO.AgencyItem i : items) {

            if (i.getZipAddr() == null) continue;
            String addr = i.getZipAddr();
            if (!(addr.contains("서울") || addr.contains("서울특별시"))) {
                continue;
            }

            Agency agency = agencyRepository.findByOrgCd(i.getOrgCd())
                    .orElse(new Agency());

            agency.setOrgCd(i.getOrgCd());
            agency.setOrgName(i.getOrgName());
            agency.setOrgType(i.getOrgTypeNm());
            agency.setZipAddr(i.getZipAddr());
            agency.setDtlAddr(i.getDtlAddr());
            agency.setZipCode(i.getZipCode());

            String tel = (i.getTelNum() != null && i.getTelNum().contains("*")) ? null : i.getTelNum();
            if (tel == null) {

                // 서울인지 체크
                boolean isSeoul = i.getZipAddr().contains("서울") || i.getZipAddr().contains("서울특별시");

                if (isSeoul) {
                    // 전화번호 추가
                    String kakaoPhone = kakaoGeocodingService.findPhoneByKeyword(i.getOrgName());
                    if (kakaoPhone != null && !kakaoPhone.isBlank()) {
                        tel = kakaoPhone;
                        System.out.println("전화번호 업데이트: " + i.getOrgName() + " → " + tel);
                    }
                }
            }

            String fax = (i.getFaxNum() != null && i.getFaxNum().contains("*")) ? null : i.getFaxNum();
            agency.setTel(tel);
            agency.setFax(fax);

            agencyRepository.save(agency);
        }
    }

    public void updateCoordinatesManually() {

        List<Agency> targets =
                agencyRepository.findAllWithoutCoordinates();

        System.out.println("좌표 업데이트 대상: " + targets.size() + "개");

        for (Agency agency : targets) {

            String addr = normalizeAddress(agency.getZipAddr());
            if (addr == null || !(addr.contains("서울") || addr.contains("서울특별시"))) {
                //System.out.println("서울 아님 → 스킵: " + addr);
                continue;
            }

            double[] coords = kakaoGeocodingService.getLatLng(addr);

            if (coords == null) {
                sleep(800);
                coords = kakaoGeocodingService.getLatLng(addr);
            }

            if (coords != null) {
                agency.setLat(coords[0]);
                agency.setLng(coords[1]);
                agencyRepository.save(agency);
                System.out.println("저장됨 = " + addr);
            } else {
                System.out.println("좌표 변환 실패 = " + addr);
            }

            sleep(400); // rate-limit 기본 딜레이
        }

        System.out.println("좌표 업데이트 완료");
    }

    private String normalizeAddress(String addr) {
        if (addr == null) return null;
        return addr.trim().replaceAll("\\s+", " ");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
