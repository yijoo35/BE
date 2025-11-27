package com.plana.seniorjob.user.dto;

import com.plana.seniorjob.user.entity.UserResume;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
    private final List<CareerResponse> careers;

    private final Integer totalCareerYears;
    private final Integer totalCareerMonths;

    private final String interestIndustry;
    private final String selfIntroText;

    private final String updatedAt;

    public static ResumeDetailResponse from(UserResume resume){
        List<CareerResponse> careerResponses = resume.getCareers().stream()
                .map(career -> CareerResponse.from(career))
                .collect(Collectors.toList());

        return ResumeDetailResponse.builder()
                .clientName(resume.getClient().getName())
                .clientEmail(resume.getClient().getEmail())
                .clientPhoneNumber(resume.getClient().getPhoneNumber())

                .age(resume.getAge())
                .gender(resume.getGender())
                .residence(resume.getResidence())
                .qualificationText(resume.getQualificationText())

                .careers(careerResponses)

                .totalCareerYears(resume.getTotalCareerYears())
                .totalCareerMonths(resume.getTotalCareerMonths())

                .interestIndustry(resume.getInterestIndustry())
                .selfIntroText(resume.getSelfIntroText())
                .updatedAt(resume.getUpdatedAt().toString())
                .build();
    }
}