package com.plana.seniorjob.agency.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agency")
@Getter @Setter
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_cd", unique = true, nullable = false)
    private String orgCd; //기관 고유 코드

    @Column(name = "org_name", nullable = false)
    private String orgName; //기관 이름

    @Column(name = "org_type")
    private String orgType; //기관 타입

    @Column(name = "zip_addr")
    private String zipAddr; //도로명 주소

    @Column(name = "dtl_addr")
    private String dtlAddr; //상세 주소

    @Column(name = "zip_code")
    private String zipCode; // 우편번호

    @Column(name = "tel")
    private String tel; //전화번호

    @Column(name = "fax")
    private String fax; // 팩스 번호

    @Column(name = "lat")
    private Double lat; //위도

    @Column(name = "lng")
    private Double lng; //경도
}
