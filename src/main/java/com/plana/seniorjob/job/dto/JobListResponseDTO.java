package com.plana.seniorjob.job.dto;

import lombok.Data;

@Data
public class JobListResponseDTO {

    private Header header;
    private Body body;

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Integer numOfRows;   // 요청한 row 수
        private Integer pageNo;      // 현재 페이지
        private Integer totalCount;  // 전체 데이터 개수
        private Items items;         // 실제 공고 리스트
    }

    @Data
    public static class Items {
        private java.util.List<JobListItem> item;
    }

    @Data
    public static class JobListItem {

        private String acptMthd;     // 접수방법 (방문/온라인/이메일 등)
        private String deadline;     // 마감 여부 (마감/접수중)
        private String emplymShp;    // 고용형태 코드 (CM0101~CM0105)
        private String emplymShpNm;  // 고용형태명 (정규직/시간제 등)
        private String frDd;         // 시작접수일 (YYYYMMDD)
        private String jobId;        // 채용공고 ID (상세조회 키)
        private String jobcls;       // 직종 코드
        private String jobclsNm;     // 직종명
        private String oranNm;       // 기업명
        private String organYn;      // 구분값 (N=대민, Y=업무)
        private String recrtTitle;   // 채용 제목
        private String stmId;        // 시스템 ID (A=100세누리/B=워크넷/C=일모아)
        private String stmNm;        // 시스템명
        private String toDd;         // 종료 접수일 (YYYYMMDD)
        private String workPlc;      // 근무지 코드
        private String workPlcNm;    // 근무지명
    }
}
