package com.plana.seniorjob.job.service;

import com.plana.seniorjob.global.util.AddressUtil;
import com.plana.seniorjob.global.util.DateFormatUtil;
import com.plana.seniorjob.job.dto.JobDetailResponseDTO;
import com.plana.seniorjob.job.dto.JobListItemDTO;
import com.plana.seniorjob.job.dto.JobListItemResponseDTO;
import com.plana.seniorjob.job.entity.JobPosting;
import com.plana.seniorjob.job.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final DateFormatUtil dateFormatUtil;
    private final AddressUtil addressUtil;
    //private final CodeMapper codeMapper;

    // 전체 목록
    public JobListItemResponseDTO getAllJobs() {

        List<JobPosting> postings = jobPostingRepository.findJobs();

        List<JobListItemDTO> items = postings.stream()
                .map(this::convertToDTO)
                .toList();

        return JobListItemResponseDTO.builder()
                .totalCount(items.size())
                .items(items)
                .build();
    }

    // 검색
    public JobListItemResponseDTO search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllJobs();
        }

        List<JobPosting> postings = jobPostingRepository.searchJobs(keyword);

        List<JobListItemDTO> items = postings.stream()
                .map(this::convertToDTO)
                .toList();

        return JobListItemResponseDTO.builder()
                .totalCount(items.size())
                .items(items)
                .build();
    }

    //상세 목록
    public JobDetailResponseDTO getJob(String jobId){
        JobPosting j = jobPostingRepository.findByJobId(jobId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고를 찾을 수 없습니다."));
        return convertToDetailDTO(j);
    }

    private JobListItemDTO convertToDTO(JobPosting j) {

        return JobListItemDTO.builder()
                .jobId(j.getJobId())
                .oranNm(j.getOranNm())
                .recrtTitle(j.getRecrtTitle())
                .jobclsNm(j.getJobclsNm())
                .region(addressUtil.extractRegion(j.getPlDetAddr() == null ? "" : j.getPlDetAddr()))
                .toDd(dateFormatUtil.formatDate(j.getToDd()))
                .deadline(j.getDeadline())
                .createDy(j.getCreateDy())
                .build();
    }

    private JobDetailResponseDTO convertToDetailDTO(JobPosting j) {

        return JobDetailResponseDTO.builder()
                .jobId(j.getJobId())
                .recrtTitle(j.getRecrtTitle())
                .oranNm(j.getOranNm())
                .toDd(dateFormatUtil.formatDate(j.getToDd()))
                .deadline(j.getDeadline())
                .jobclsNm(j.getJobclsNm())
                .clltPrnnum(j.getClltPrnnum())
                .detCnts(j.getDetCnts())
                .etcItm(j.getEtcItm())
                .plDetAddr(j.getPlDetAddr())
                .plbizNm(j.getPlbizNm())
                .acptMthd(j.getAcptMthd())
                .clerkContt(j.getClerkContt())
                .createDy(j.getCreateDy())
                .build();
    }
}
