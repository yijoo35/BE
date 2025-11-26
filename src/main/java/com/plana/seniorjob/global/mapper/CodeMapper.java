package com.plana.seniorjob.global.mapper;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CodeMapper {

    private static final Map<String, String> EMPLOY_TYPE_MAP = Map.of(
            "CM0101", "정규직",
            "CM0102", "계약직",
            "CM0103", "시간제일자리",
            "CM0104", "일당직",
            "CM0105", "기타"
    );

    private static final Map<String, String> STM_MAP = Map.of(
            "A", "100세누리",
            "B", "워크넷",
            "C", "일모아"
    );

    private static final Map<String, String> ACPT_MTHD_MAP = Map.of(
            "CM0801", "온라인",
            "CM0802", "이메일",
            "CM0803", "팩스",
            "CM0804", "방문"
    );

    public String toEmplymName(String code) {
        return EMPLOY_TYPE_MAP.getOrDefault(code, code);
    }

    public String toStmName(String code) {
        return STM_MAP.getOrDefault(code, code);
    }

    public String toAcptMthd(String code) { return ACPT_MTHD_MAP.getOrDefault(code, code);}
}
