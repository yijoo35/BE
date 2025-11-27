package com.plana.seniorjob.user.service;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus;
import com.plana.seniorjob.user.entity.Career;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.entity.UserResume;
import com.plana.seniorjob.user.repository.UserResumeRepository;
import com.plana.seniorjob.counseling.repository.CounselingRepository;
import com.plana.seniorjob.user.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final UserResumeRepository userResumeRepository;
    private final UserService userService;
    private final CounselingRepository counselingRepository;
    private final UserEntityRepository userRepository;

    // 1. initializeResume 메서드
    @Transactional
    public UserResume initializeResume(UserEntity user, Counseling counseling){
        String birthYearString = user.getBirthyear();
        String genderString = user.getGender();

        Integer calculatedAge = userService.calculateKoreanAge(birthYearString);

        UserResume userResume = UserResume.builder()
                .client(user)
                .counseling(counseling)
                .gender(genderString)
                .age(calculatedAge)

                .residence("")
                .qualificationText("")
                .interestIndustry("")
                .selfIntroText("")
                .embeddingVector(null)
                .build();

        return userResumeRepository.save(userResume);
    }

    // 2. getResumeForCounseling 메서드
    @Transactional(readOnly = true)
    public UserResume getResumeForCounseling(Long counselingId, Long currentUserId){

        // 1. 상담 엔티티 조회
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("상담 내역을 찾을 수 없습니다. ID: " +counselingId));

        // 2. 상태 검증
        CounselingStatus status = counseling.getCounselingStatus();
        if(status != CounselingStatus.IN_MATCHING && status != CounselingStatus.MATCHED){
            throw new IllegalArgumentException("이력서 작성이 완료되지 않아 접근할 수 없습니다.");
        }

        // 3. 권한 검증 로직 삽입
        Long clientId = counseling.getName().getId();
        Long counselorId = (counseling.getCounselor() != null) ? counseling.getCounselor().getId() : null;

        boolean isClient = currentUserId.equals(clientId);
        boolean isAssignedCounselor = counselorId != null && currentUserId.equals(counselorId);

        if (!isClient && !isAssignedCounselor) {
            throw new AccessDeniedException("이력서 조회 권한이 없습니다.");
        }

        // 4. 이력서 조회 (JOIN FETCH 쿼리 사용으로 N+1 문제 방지 및 즉시 로딩)
        UserResume resume = userResumeRepository.findByCounselingWithClientAndCareers(counseling)
                .orElseThrow(() -> new EntityNotFoundException("해당 상담 ID에 연결된 이력서를 찾을 수 없습니다."));

        return resume;
    }

    // 3. calculateTotalCareer 메서드 (총 경력 계산 로직)
    @Transactional
    public void calculateTotalCareer(UserResume userResume) {
        List<Career> careers = userResume.getCareers();

        if (careers == null || careers.isEmpty()) {
            // 경력이 없으면 0으로 초기화하고 종료
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
                // 종료일의 마지막 날짜를 계산하기 위해 다음달 1일에서 1일을 뺌
                endDate = LocalDate.of(career.getEndYear(), career.getEndMonth(), 1).plusMonths(1).minusDays(1);
            } else {
                continue; // 유효하지 않은 경력 기간 스킵
            }

            // 시작일과 종료일이 유효한지 확인 (시작일이 종료일보다 늦으면 스킵)
            if (startDate.isAfter(endDate)) {
                continue;
            }

            // 기간 계산: ChronoUnit.MONTHS.between은 종료일 포함이 아니므로 + 1
            long monthsInCareer = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1).plusMonths(1));
            totalMonths += monthsInCareer;
        }

        // 년/월로 변환
        int years = (int) (totalMonths / 12);
        int remainingMonths = (int) (totalMonths % 12);

        userResume.setTotalCareerYears(years);
        userResume.setTotalCareerMonths(remainingMonths);
    }
}