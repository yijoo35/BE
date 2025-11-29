package com.plana.seniorjob.counseling.service;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus;
import com.plana.seniorjob.counseling.repository.CounselingRepository;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.repository.UserEntityRepository;
import com.plana.seniorjob.user.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CounselingService {

    private final CounselingRepository counselingRepository;
    private final UserEntityRepository userEntityRepository;
    private final ResumeService resumeService;


    @Transactional
    public Counseling startCounseling(Long userId) {
        UserEntity client = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 클라이언트를 찾을 수 없습니다: " + userId));

        List<Counseling> activeCounselings = counselingRepository.findByClientAndStatusIn(
                client, List.of(CounselingStatus.APPLIED, CounselingStatus.IN_PROGRESS));

        if (!activeCounselings.isEmpty()) {
            throw new IllegalStateException("이미 진행 중이거나 대기 중인 상담이 있습니다.");
        }
        Counseling counseling = Counseling.builder()
                .client(client)
                .status(CounselingStatus.APPLIED) // 초기 상태는 상담 신청(APPLIED)
                .build();

        counseling = counselingRepository.save(counseling);
        resumeService.initializeResume(client, counseling);

        return counseling;
    }

    //특정 상담 상태 변경
    @Transactional
    public Counseling updateCounselingStatus(Long counselingId, CounselingStatus newStatus) {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 상담 정보를 찾을 수 없습니다: " + counselingId));

        // 상태 변경
        counseling.setStatus(newStatus);

        return counselingRepository.save(counseling);
    }

    //기관 기준 상담 조회
    @Transactional(readOnly = true)
    public List<Counseling> getClientCounselingList(Long userId) {
        UserEntity client = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 클라이언트를 찾을 수 없습니다: " + userId));

        // 최신순 정렬
        return counselingRepository.findAllByClientOrderByCreatedAtDesc(client);
    }

    //특정 상담 상세 조회
    @Transactional(readOnly = true)
    public Counseling getCounselingDetail(Long counselingId) {
        return counselingRepository.findById(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 상담 정보를 찾을 수 없습니다: " + counselingId));
    }
}