package com.plana.seniorjob.user.service;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus; // CounselingStatus는 Enum이므로 import 경로 확인
import com.plana.seniorjob.user.entity.Career;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.entity.UserResume;
import com.plana.seniorjob.user.repository.UserResumeRepository;
import com.plana.seniorjob.counseling.repository.CounselingRepository;
import com.plana.seniorjob.user.repository.UserEntityRepository; // UserEntity를 찾기 위해 추가
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 어노테이션 추가

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
        // 이력서 생성
        UserResume userResume = UserResume.builder()
                .client(user)
                .counseling(counseling)
                .age(user.getBirthyear() != null ? (LocalDate.now().getYear() - Integer.parseInt(user.getBirthyear()) + 1) : null)
                .gender(user.getGender())
                .build();
        return userResumeRepository.save(userResume);
    }

    // 2. 총 경력 계산 메서드
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
                endDate = LocalDate.of(career.getEndYear(), career.getEndMonth(), 1).plusMonths(1).minusDays(1);
            } else {
                continue;
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

        userResume.setTotalCareerYears(years);
        userResume.setTotalCareerMonths(remainingMonths);
        userResumeRepository.save(userResume);
    }

    // 3.상담 ID를 통해 이력서를 조회하고 권한을 검증하는 메서드
    @Transactional(readOnly = true)
    public UserResume getResumeForCounseling(Long counselingId, Long currentUserId) {
        // 1. Counseling 엔티티 조회
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 상담 정보를 찾을 수 없습니다: " + counselingId));

        // 2. UserResume 엔티티 조회
        UserResume resume = userResumeRepository.findByCounselingWithClientAndCareers(counseling)
                .orElseThrow(() -> new EntityNotFoundException("해당 상담 정보와 연결된 이력서를 찾을 수 없습니다."));

        // 3. 권한 검증: 클라이언트 또는 담당 상담사만 접근 가능
        Long clientUserId = resume.getClient().getId();
        Long counselorUserId = counseling.getCounselor() != null ? counseling.getCounselor().getId() : null;

        if (!currentUserId.equals(clientUserId) && !currentUserId.equals(counselorUserId)) {
            throw new AccessDeniedException("이력서 정보를 조회할 권한이 없습니다. (클라이언트 또는 담당 상담사만 가능)");
        }

        return resume;
    }
}