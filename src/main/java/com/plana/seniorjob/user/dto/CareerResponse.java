package com.plana.seniorjob.user.dto;

import com.plana.seniorjob.user.entity.Career;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CareerResponse {

    private final Long id;
    private final Integer startYear;
    private final Integer startMonth;
    private final Integer endYear;
    private final Integer endMonth;

    private final String period;
    private final String companyName;
    private final String employmentType;
    private final String position;
    private final String jobDescription;
    private final Boolean isCurrentlyWorking; // 현재 재직 여부 추가

    public static CareerResponse from(Career career) {
        String periodString = formatCareerPeriod(
                career.getStartYear(),
                career.getStartMonth(),
                career.getEndYear(),
                career.getEndMonth()
        );

        return CareerResponse.builder()
                .id(career.getId()) // ID 추가
                .startYear(career.getStartYear())
                .startMonth(career.getStartMonth())
                .endYear(career.getEndYear())
                .endMonth(career.getEndMonth())
                .period(periodString)
                .companyName(career.getCompanyName())
                .employmentType(career.getEmploymentType() != null ? career.getEmploymentType().getDescription() : null)
                .position(career.getPosition())
                .jobDescription(career.getJobDescription())
                .isCurrentlyWorking(career.getIsCurrentlyWorking()) // 재직 여부 추가
                .build();
    }

    // 기간 포맷팅 및 계산 로직
    private static String formatCareerPeriod(Integer startYear, Integer startMonth, Integer endYear, Integer endMonth) {
        if (startYear == null || startMonth == null) return "";

        if (endYear == null || endMonth == null) {
            return "재직 중";
        }

        // 월 단위 계산 (종료 월 포함: +1)
        long totalMonths = (long)(endYear - startYear) * 12 + (endMonth - startMonth + 1);
        if (totalMonths <= 0) return "";

        long years = totalMonths / 12;
        long months = totalMonths % 12;

        StringBuilder sb = new StringBuilder();
        if (years > 0) sb.append(years).append("년 ");
        if (months > 0) sb.append(months).append("개월");

        return sb.toString().trim();
    }
}