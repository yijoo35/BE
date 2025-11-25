package com.plana.seniorjob.user.dto;

import com.plana.seniorjob.user.entity.UserResume;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResumeDetailResponse {
    private final String clientName;
    private final String clientEmail;
    private final String clientPhoneNumber;
    private final Integer age;
    private final String gender;

    private final String residence;
    private final String qualificationText;
    private final String careerText;
    private final String interestIndustry;
    private final String selfIntroText;

    private final String updatedAt;

    public static ResumeDetailResponse from(UserResume resume){
        return ResumeDetailResponse.builder()
                .clientName(resume.getClient().getName())
                .clientEmail(resume.getClient().getEmail())
                .clientPhoneNumber(resume.getClient().getPhoneNumber())

                .age(resume.getAge())
                .gender(resume.getGender())
                .residence(resume.getResidence())
                .qualificationText(resume.getQualificationText())
                .careerText(resume.getCareerText())
                .interestIndustry(resume.getInterestIndustry())
                .selfIntroText(resume.getSelfIntroText())
                .updatedAt(resume.getUpdatedAt().toString()) // 날짜 포맷은 필요에 따라 변경 가능
                .build();
    }
}
