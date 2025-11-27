package com.plana.seniorjob.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentType {

    FULL_TIME("정규직"),
    CONTRACT("계약직"),
    FREELANCER("프리랜서"),
    INTERN("인턴"),
    PART_TIME("시간제/파트타임"),
    ETC("기타");

    private final String description; // 한국어 설명
    public static EmploymentType fromDescription(String description) {
        for (EmploymentType type : EmploymentType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        return null;
    }
}