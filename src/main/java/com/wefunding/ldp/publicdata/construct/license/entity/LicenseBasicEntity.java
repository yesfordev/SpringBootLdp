package com.wefunding.ldp.publicdata.construct.license.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by yes on 2020/09/24
 */

@Data
@Entity
@Table(name = "license_basic", schema = "construct")
public class LicenseBasicEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rnum;

    private String platPlc;

    private String sigunguCd;

    private String bjdongCd;

    private String platGbCd;

    private String bun;

    private String ji;

    private String mgmPmsrgstPk;

    private String bldNm;

    private String splotNm;

    private String block;

    private String lot;

    private String jimokCdNm;

    private String jiyukCdNm;

    private String jiguCdNm;

    private String guyukCdNm;

    private String jimokCd;

    private String jiyukCd;

    private String jiguCd;

    private String guyukCd;

    private String archGbCdNm;

    private String archGbCd;

    private double platArea;

    private double archArea;

    private double bcRat;

    private double totArea;

    private double vlRatEstmTotArea;

    private double vlRat;

    private Integer mainBldCnt;

    private Integer atchBldDongCnt;

    private String mainPurpsCd;

    private String mainPurpsCdNm;

    private Integer hhldCnt;

    private Integer hoCnt;

    private Integer fmlyCnt;

    private Integer totPkngCnt;

    private String stcnsSchedDay;

    private String stcnsDelayDay;

    private String realStcnsDay;

    private String archPmsDay;

    private String useAprDay;

    private String crtnDay;

    @CreationTimestamp
    private LocalDateTime regDt;

    @UpdateTimestamp
    private LocalDateTime updDt;

}
