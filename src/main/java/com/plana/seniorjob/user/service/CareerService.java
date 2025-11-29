package com.plana.seniorjob.user.service;

import com.plana.seniorjob.user.dto.CareerRequest;
import com.plana.seniorjob.user.entity.Career;
import com.plana.seniorjob.user.entity.UserResume;
import com.plana.seniorjob.user.repository.CareerRepository;
import com.plana.seniorjob.user.repository.UserResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;
    private final UserResumeRepository userResumeRepository;


    @Transactional
    public Career addCareer(Long resumeId, Long currentUserId, CareerRequest request) {
        // 1. 이력서(UserResume) 조회
        UserResume userResume = userResumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 이력서를 찾을 수 없습니다: " + resumeId));

        // 2. 권한 검증: 이력서 소유자 ID와 현재 사용자 ID 비교
        Long resumeOwnerId = userResume.getClient().getId();
        if (!resumeOwnerId.equals(currentUserId)) {
            // ID가 다르면 예외 발생
            throw new AccessDeniedException("경력 정보를 추가할 권한이 없습니다. (이력서 소유자만 가능)");
        }

        // 3. Career 엔티티 생성
        Career newCareer = Career.builder()
                .userResume(userResume)
                .startYear(request.getStartYear())
                .startMonth(request.getStartMonth())
                .endYear(request.getEndYear())
                .endMonth(request.getEndMonth())
                .companyName(request.getCompanyName())
                .employmentType(request.getEmploymentType())
                .position(request.getPosition())
                .jobDescription(request.getJobDescription())
                .isCurrentlyWorking(request.getIsCurrentlyWorking() != null ? request.getIsCurrentlyWorking() : false)
                .build();

        // 4. Career 저장
        Career savedCareer = careerRepository.save(newCareer);

        // 5. 총 경력 재계산 및 업데이트 (CareerService 내부에서 처리)
        calculateTotalCareer(userResume);

        return savedCareer;
    }

    @Transactional
    public Career updateCareer(Long careerId, Long currentUserId, CareerRequest request) {
        // 1. 경력 엔티티 조회
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 경력 정보를 찾을 수 없습니다: " + careerId));

        UserResume userResume = career.getUserResume();

        // 2. 권한 검증
        Long resumeOwnerId = userResume.getClient().getId();
        if (!resumeOwnerId.equals(currentUserId)) {
            throw new AccessDeniedException("경력 정보를 수정할 권한이 없습니다. (이력서 소유자만 가능)");
        }

        // 3. 경력 엔티티 업데이트 (Career 엔티티에 update 메서드가 정의되어 있다고 가정)
        career.update(request);

        // 4. 총 경력 재계산 및 업데이트
        calculateTotalCareer(userResume);

        return career;
    }

    @Transactional
    public void deleteCareer(Long careerId, Long currentUserId) {
        // 1. 경력 엔티티 조회
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 경력 정보를 찾을 수 없습니다: " + careerId));

        UserResume userResume = career.getUserResume();

        // 2. 권한 검증
        Long resumeOwnerId = userResume.getClient().getId();
        if (!resumeOwnerId.equals(currentUserId)) {
            throw new AccessDeniedException("경력 정보를 삭제할 권한이 없습니다. (이력서 소유자만 가능)");
        }

        // 3. 경력 엔티티 삭제
        careerRepository.delete(career);

        // 4. 총 경력 재계산 및 업데이트
        calculateTotalCareer(userResume);
    }

    @Transactional
    public void calculateTotalCareer(UserResume userResume) {
        List<Career> careers = userResume.getCareers();

        if (careers == null || careers.isEmpty()) {
            // 경력이 없으면 0으로 초기화
            userResume.setTotalCareerYears(0);
            userResume.setTotalCareerMonths(0);
            userResumeRepository.save(userResume);
            return;
        }

        long totalMonths = 0;
        LocalDate today = LocalDate.now();

        for (Career career : careers) {
            // 시작일 (필수)
            LocalDate startDate = LocalDate.of(career.getStartYear(), career.getStartMonth(), 1);

            LocalDate endDate;
            if (Boolean.TRUE.equals(career.getIsCurrentlyWorking())) {
                endDate = today;
            } else if (career.getEndYear() != null && career.getEndMonth() != null) {
                // 종료 월의 마지막 날로 설정 (기간 계산 시 해당 월 전체를 포함하도록)
                endDate = LocalDate.of(career.getEndYear(), career.getEndMonth(), 1).plusMonths(1).minusDays(1);
            } else {
                continue; // 유효하지 않은 경력 기간
            }

            // 시작일과 종료일이 유효한지 확인
            if (startDate.isAfter(endDate)) {
                continue;
            }

            // 기간 계산
            long monthsInCareer = ChronoUnit.MONTHS.between(startDate, endDate) + 1;
            totalMonths += monthsInCareer;
        }

        // 년/월로 변환
        int years = (int) (totalMonths / 12);
        int remainingMonths = (int) (totalMonths % 12);

        // UserResume 엔티티에 총 경력 업데이트
        userResume.setTotalCareerYears(years);
        userResume.setTotalCareerMonths(remainingMonths);
        userResumeRepository.save(userResume);
    }
}