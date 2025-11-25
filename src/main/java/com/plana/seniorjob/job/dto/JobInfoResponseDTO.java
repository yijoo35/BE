package com.plana.seniorjob.job.dto;

import lombok.Data;

@Data
public class JobInfoResponseDTO {

    private Header header;
    private Body body;

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
    }

    @Data
    public static class Items {
        private JobInfoItem item;
    }

    @Data
    public static class JobInfoItem {

        private String acptMthdCd;   // 접수방법 코드 (CM0801~CM0804)
        private String age;          // 연령
        private String clerk;        // 담당자
        private String clerkContt;   // 담당자 연락처
        private String clltPrnnum;   // 모집 인원
        private String createDy;     // 생성일자 (2025-11-12T01:00:02)
        private String detCnts;      // 상세 내용
        private String etcItm;       // 기타사항
        private String frAcptDd;     // 시작접수일
        private String jobId;        // 공고 ID
        private String lnkStmId;     // 연계 시스템 ID (A,B,C)
        private String organYn;      // 구분값 (대민/업무)
        private String plDetAddr;    // 상세 주소
        private String plbizNm;      // 사업장명(기업명)
        private String repr;         // 대표자명
        private String stmId;        // 시스템 ID
        private String toAcptDd;     // 종료접수일
        private String updDy;        // 변경일자
        private String wantedAuthNo; // 구인 인증번호
        private String wantedTitle;  // 채용 제목
    }
}
