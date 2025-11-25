package com.plana.seniorjob.job.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.plana.seniorjob.job.dto.JobInfoResponseDTO;
import com.plana.seniorjob.job.dto.JobListResponseDTO;
import com.plana.seniorjob.job.entity.JobPosting;
import com.plana.seniorjob.job.repository.JobPostingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JobPostingFetchService {

    private final JobPostingRepository jobPostingRepository;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper = new XmlMapper();

    private final javax.sql.DataSource dataSource;

    @Value("${public-api.key}")
    private String serviceKey;

    //@Transactional
    public void fetchJobPostings() {
        int page = 1;
        int rows = 100;

        while (true) {

            String url = buildListUrl(page, rows);

            byte[] bytes = restTemplate.getForObject(url, byte[].class);
            if (bytes == null) break;

            String xml = new String(bytes, StandardCharsets.UTF_8);

            JobListResponseDTO listDto = parseList(xml);

            // 목록 데이터 없으면 종료
            if (listDto.getBody() == null ||
                    listDto.getBody().getItems() == null ||
                    listDto.getBody().getItems().getItem() == null ||
                    listDto.getBody().getItems().getItem().isEmpty()) break;

            //System.out.println("목록 item 수 = " + listDto.getBody().getItems().getItem().size());

            saveList(listDto);

            if (listDto.getBody().getItems().getItem().size() < rows) break;

            page++;
        }
    }

    // 목록 저장
    private void saveList(JobListResponseDTO listDto) {

        for (JobListResponseDTO.JobListItem item : listDto.getBody().getItems().getItem()) {

            // 이번 달 이후의 공고만 저장
            if (!isAfterThisMonth(item.getFrDd())) continue;

            // 상세 조회
            JobInfoResponseDTO detailDto = fetchDetail(item.getJobId());
            if (detailDto == null ||
                    detailDto.getBody() == null ||
                    detailDto.getBody().getItems() == null ||
                    detailDto.getBody().getItems().getItem() == null) continue;

            JobInfoResponseDTO.JobInfoItem d = detailDto.getBody().getItems().getItem();

            //System.out.println("상세 조회: " + d.getJobId());

            // 기존 데이터 있으면 update, 없으면 insert
            JobPosting posting = jobPostingRepository
                    .findByJobId(item.getJobId())
                    .orElse(new JobPosting());

            // 목록 데이터 매핑
            posting.setJobId(item.getJobId());
            posting.setRecrtTitle(item.getRecrtTitle());
            posting.setEmplymShp(item.getEmplymShp());
            posting.setEmplymShpNm(item.getEmplymShpNm());
            posting.setFrDd(item.getFrDd());
            posting.setToDd(item.getToDd());
            posting.setOranNm(item.getOranNm());
            posting.setDeadline(item.getDeadline());
            posting.setWorkPlc(item.getWorkPlc());
            posting.setWorkPlcNm(item.getWorkPlcNm());
            posting.setAcptMthd(item.getAcptMthd());

            // 상세 데이터 매핑
            posting.setAcptMthdCd(d.getAcptMthdCd());
            posting.setClerk(d.getClerk());
            posting.setClerkContt(d.getClerkContt());
            posting.setClltPrnnum(d.getClltPrnnum());
            posting.setCreateDy(d.getCreateDy());
            posting.setDetCnts(d.getDetCnts());
            posting.setEtcItm(d.getEtcItm());
            posting.setPlDetAddr(d.getPlDetAddr());
            posting.setPlbizNm(d.getPlbizNm());
            posting.setRepr(d.getRepr());
            posting.setUpdrId(d.getUpdrId());

            System.out.println("저장: " + posting.getJobId());

            try {
                jobPostingRepository.save(posting);
                System.out.println("DB 저장 성공: " + posting.getJobId());
            } catch (Exception e) {
                System.out.println("DB 저장 실패: " + posting.getJobId());
                e.printStackTrace();
            }
        }
    }

    // 상세 조회
    private JobInfoResponseDTO fetchDetail(String jobId) {

        String url = buildDetailUrl(jobId);

        byte[] bytes = restTemplate.getForObject(url, byte[].class);
        if (bytes == null) return null;

        String xml = new String(bytes, StandardCharsets.UTF_8);

        return parseDetail(xml);
    }



    // URL
    private String buildListUrl(int page, int rows) {
        return "https://apis.data.go.kr/B552474/SenuriService/getJobList"
                + "?ServiceKey=" + serviceKey
                + "&numOfRows=" + rows
                + "&pageNo=" + page;
    }

    private String buildDetailUrl(String jobId) {
        return "https://apis.data.go.kr/B552474/SenuriService/getJobInfo"
                + "?ServiceKey=" + serviceKey
                + "&id=" + jobId;
    }

    // 파싱
    private JobListResponseDTO parseList(String xml) {
        try {
            return xmlMapper.readValue(xml, JobListResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("목록 XML 파싱 실패", e);
        }
    }

    private JobInfoResponseDTO parseDetail(String xml) {
        try {
            return xmlMapper.readValue(xml, JobInfoResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("상세 XML 파싱 실패", e);
        }
    }

    // 날짜 필터링 -> 현재 날짜의 월일 이후 (25년 11월 이후) 것들만
    private boolean isAfterThisMonth(String frDd) {
        if (frDd == null || frDd.length() < 6) return false;

        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.YearMonth thisMonth = java.time.YearMonth.from(now);

        java.time.YearMonth frMonth = java.time.YearMonth.parse(
                frDd.substring(0, 6),
                java.time.format.DateTimeFormatter.ofPattern("yyyyMM")
        );

        return !frMonth.isBefore(thisMonth);
    }
}
