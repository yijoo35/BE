package com.plana.seniorjob.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    @Schema(description = "결과 메시지")
    private String message;

    @Schema(description = "실제 데이터")
    private T data;
}