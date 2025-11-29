package com.plana.seniorjob.counseling.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingStatus {

    BEFORE_MATCHING("매칭 전"), // 상담 완료 직후, 기업 추천/지원 시작 전
    IN_MATCHING("매칭 중"), // AI 추천 로직 구동 및 기업에 근무 신청/추천을 진행 중
    MATCHED("매칭 완료"), // 기업과 구직자 간 최종 합의 (업무 시작 확정)
    CANCELED("매칭 취소"), // 매칭 과정에서 구직자/기업의 사유로 취소
    FAILED("매칭 실패"); // 모든 매칭 시도가 성사되지 못했을 경우

    private final String description;
}