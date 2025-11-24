package com.plana.seniorjob.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SelectAgencyRequest {
    @NotNull(message = "사용자 user_id는 필수값입니다.")
    private Long userId;

    @NotNull(message = "기관 코드번호는 필수값입니다.")
    private String orgCd;
    //수정할 정보
    private String tel;
    private String zipAddr;
    private String dtlAddr;
}