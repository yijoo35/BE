package com.plana.seniorjob.user.service;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus; // CounselingStatus는 Enum이므로 import 경로 확인
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.entity.UserResume;
import com.plana.seniorjob.user.repository.UserResumeRepository;
import com.plana.seniorjob.counseling.repository.CounselingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final UserResumeRepository userResumeRepository;
    private final UserService userService;
    private final CounselingRepository counselingRepository;

    // 1. initializeResume 메서드
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
                .careerText("")
                .interestIndustry("")
                .selfIntroText("")
                .embeddingVector(null)
                .build();

        return userResumeRepository.save(userResume);
    }

    // 2. getResumeForCounseling 메서드
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

        // 4. 이력서 조회
        UserResume resume = userResumeRepository.findByCounseling(counseling)
                .orElseThrow(() -> new EntityNotFoundException("해당 상담에 대한 이력서가 작성되지 않았습니다."));

        return resume;
    }
}