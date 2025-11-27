package com.plana.seniorjob.user.dto;

import com.plana.seniorjob.user.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerRequest {

    @NotNull(message = "시작 연도는 필수입니다.")
    private Integer startYear;

    @NotNull(message = "시작 월은 필수입니다.")
    @Min(value = 1, message = "월은 1월부터 12월 사이여야 합니다.")
    @Max(value = 12, message = "월은 1월부터 12월 사이여야 합니다.")
    private Integer startMonth;

    private Integer endYear; // 재직 중일 경우 null 허용

    @Min(value = 1, message = "월은 1월부터 12월 사이여야 합니다.")
    @Max(value = 12, message = "월은 1월부터 12월 사이여야 합니다.")
    private Integer endMonth; // 재직 중일 경우 null 허용

    // --- 회사 및 직무 정보 ---
    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotNull(message = "고용 형태는 필수입니다.")
    private EmploymentType employmentType; // Enum 타입으로 데이터 정합성 보장

    @NotBlank(message = "직위/직책은 필수입니다.")
    private String position;

    private String jobDescription; // 담당 업무 상세 (선택 사항일 수 있음)

    // --- 현재 상태 정보 ---
    @NotNull(message = "재직 여부는 필수입니다.")
    private Boolean isCurrentlyWorking; // Career 엔티티 업데이트 로직에 필수
}