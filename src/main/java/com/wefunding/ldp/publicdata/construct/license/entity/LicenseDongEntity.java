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
@Table(name = "license_dong", schema = "construct")
public class LicenseDongEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rnum;

    private String platPlc;

    private String sigunguCd;

    private String bjdongCd;

    private String platGbCd;

    private String bun;

    private String ji;

    private String mgmDongOulnPk;

    private String mgmPmsrgstPk;

    private String bldNm;

    private String splotNm;

    private String block;

    private String lot;

    private String mainAtchGbCd;

    private String mainAtchGbCdNm;

    private String mainPurpsCd;

    private String mainPurpsCdNm;

    private Integer hoCnt;

    private Integer fmlyCnt;

    private Integer hhldCnt;

    private String strctCd;

    private String strctCdNm;

    private String roofCd;

    private String roofCdNm;

    private double archArea;

    private double totArea;

    private double vlRatEstmTotArea;

    private String crtnDay;

    @CreationTimestamp
    private LocalDateTime regDt;

    @UpdateTimestamp
    private LocalDateTime updDt;
}
