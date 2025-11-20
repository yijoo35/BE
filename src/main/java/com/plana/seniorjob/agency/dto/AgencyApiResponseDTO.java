package com.plana.seniorjob.agency.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class AgencyApiResponseDTO {

    private Header header;
    private Body body;

    @Data //무시
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Items items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<AgencyItem> item;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgencyItem {
        private String dtlAddr; //상세 주소
        private String faxNum;
        private String orgCd; // 기관 코드
        private String orgName; // 기관 이름
        private String orgTypeNm;
        private String telNum;
        private String zipAddr; // 주소
        private String zipCode; // 우편 번호
    }
}