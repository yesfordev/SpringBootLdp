package com.wefunding.ldp.publicdata.construct.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "construct_title", schema = "public")
public class ConstructTitleEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rnum;

    private String platPlc;

    private String sigunguCd;

    private String bjdongCd;

    private String platGbCd;

    private String bun;

    private String ji;

    private String mgmBldrgstPk;

    private String regstrGbCd;

    private String regstrGbCdNm;

    private String regstrKindCd;

    private String regstrKindCdNm;

    private String newPlatPlc;

    private String bldNm;

    private String splotNm;

    private String block;

    private String lot;

    private Integer bylotCnt;

    private String naRoadCd;

    private String naBjdongCd;

    private String naUgrndCd;

    private Integer naMainBun;

    private Integer naSubBun;

    private String dongNm;

    private String mainAtchGbCd;

    private String mainAtchGbCdNm;

    private double platArea;

    private double archArea;

    private double bcRat;

    private double totArea;

    private double vlRatEstmTotArea;

    private double vlRat;

    private String strctCd;

    private String strctCdNm;

    private String etcStrct;

    private String mainPurpsCd;

    private String mainPurpsCdNm;

    private String etcPurps;

    private String roofCd;

    private String roofCdNm;

    private String etcRoof;

    private Integer hhldCnt;

    private Integer fmlyCnt;

    private double heit;

    private Integer grndFlrCnt;

    private Integer ugrndFlrCnt;

    private Integer rideUseElvtCnt;

    private Integer emgenUseElvtCnt;

    private Integer atchBldCnt;

    private double atchBldArea;

    private double totDongTotArea;

    private Integer indrMechUtcnt;

    private double indrMechArea;

    private Integer oudrMechUtcnt;

    private double oudrMechArea;

    private Integer indrAutoUtcnt;

    private double indrAutoArea;

    private Integer oudrAutoUtcnt;

    private double oudrAutoArea;

    private String pmsDay;

    private String stcnsDay;

    private String useAprDay;

    private String pmsnoYear;

    private String pmsnoKikCd;

    private String pmsnoKikCdNm;

    private String pmsnoGbCd;

    private String pmsnoGbCdNm;

    private Integer hoCnt;

    private String engrGrade;

    private double engrRat;

    private Integer engrEpi;

    private String gnBldGrade;

    private Integer gnBldCert;

    private String itgBldGrade;

    private Integer itgBldCert;

    private String crtnDay;

    @CreationTimestamp
    private LocalDateTime regDt;

    @UpdateTimestamp
    private LocalDateTime updDt;
}