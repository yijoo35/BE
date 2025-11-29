package com.plana.seniorjob.counseling.dto;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CounselingResponse {

    private final Long consultId;
    private final Long clientId;
    private final String clientUsername;
    private final CounselingStatus status;
    private final String statusDescription;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CounselingResponse from(Counseling counseling) {
        return CounselingResponse.builder()
                .consultId(counseling.getConsultId())
                .clientId(counseling.getClient().getId())
                .clientUsername(counseling.getClient().getName())
                .status(counseling.getStatus())
                .statusDescription(counseling.getStatus().toString())
                .createdAt(counseling.getCreatedAt())
                .updatedAt(counseling.getUpdatedAt())
                .build();
    }
}