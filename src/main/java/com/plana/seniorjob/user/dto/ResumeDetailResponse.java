package com.plana.seniorjob.user.dto;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.user.entity.UserResume;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResumeDetailResponse {
    // 클라이언트 정보
    private final String clientName;
    private final String clientEmail;
    private final String clientPhoneNumber;

    // 상담사 정보
    private final Long counselorId;
    private final String counselorName;

    // 이력서 기본 정보
    private final Integer age;
    private final String gender;
    private final String residence;
    private final String qualificationText;
    private final List<CareerResponse> careers;

    // 경력 요약
    private final Integer totalCareerYears;
    private final Integer totalCareerMonths;

    // 기타
    private final String interestIndustry;
    private final String selfIntroText;
    private final String updatedAt;

    public static ResumeDetailResponse from(UserResume resume){
        List<CareerResponse> careerResponses = resume.getCareers().stream()
                .map(career -> CareerResponse.from(career))
                .collect(Collectors.toList());

        // Counseling 엔티티를 통해 상담사 정보 가져오기
        Counseling counseling = resume.getCounseling();
        Long counselorId = counseling != null && counseling.getCounselor() != null ? counseling.getCounselor().getId() : null;
        String counselorName = counseling != null && counseling.getCounselor() != null ? counseling.getCounselor().getName() : null;


        return ResumeDetailResponse.builder()
                // 클라이언트 정보
                .clientName(resume.getClient().getName())
                .clientEmail(resume.getClient().getEmail())
                .clientPhoneNumber(resume.getClient().getPhoneNumber())

                // 상담사 정보 추가
                .counselorId(counselorId)
                .counselorName(counselorName)

                // 이력서 기본 정보
                .age(resume.getAge())
                .gender(resume.getGender())
                .residence(resume.getResidence())
                .qualificationText(resume.getQualificationText())

                .careers(careerResponses)

                .totalCareerYears(resume.getTotalCareerYears())
                .totalCareerMonths(resume.getTotalCareerMonths())

                .interestIndustry(resume.getInterestIndustry())
                .selfIntroText(resume.getSelfIntroText())
                .updatedAt(resume.getUpdatedAt() != null ? resume.getUpdatedAt().toString() : null)
                .build();
    }
}