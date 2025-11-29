package com.plana.seniorjob.counseling.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CounselingStatus {

    APPLIED("상담 신청"), // 초기 신청 단계
    IN_PROGRESS("상담 진행 중"), // 이력서 작성 지원 등 상담사와의 실제 소통이 진행되는 단계 (이력서 열람 불가능)
    COMPLETED("상담 완료"), // 상담이 공식적으로 종료되었으며, 이력서 열람이 가능하고 매칭이 시작될 수 있는 단계
    CANCLED("상담 취소");

    private final String description;
}