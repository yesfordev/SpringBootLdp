package com.wefunding.ldp.publicdata.construct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Data
public class Item {

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

//    @Id
//    @GeneratedValue()
//    private Integer id;
//
//    private Integer rnum;
//
//    private String platplc;
//
//    private String sigungucd;
//
//    private String bjdongcd;
//
//    @Column(columnDefinition = "char")
//    private String platgbcd;
//
//    private String bun;
//
//    private String ji;
//
//    @Id
//    private String mgmbldrgstpk;
//
//    private String regstrgbcd;
//
//    private String regstrgbcdnm;
//
//    private String regstrkindcd;
//
//    private String regstrkindcdnm;
//
//    private String newplatplc;
//
//    private String bldnm;
//
//    private String splotnm;
//
//    private String block;
//
//    private String lot;
//
//    private Integer bylotcnt;
//
//    private String naroadcd;
//
//    private String nabjdongcd;
//
//    private String naugrndcd;
//
//    private Integer namainbun;
//
//    private Integer nasubbun;
//
//    private String dongnm;
//
//    @Column(columnDefinition = "char")
//    private String mainatchgbcd;
//
//    private String mainatchgbcdnm;
//
//    private double platarea;
//
//    private double archarea;
//
//    private double bcrat;
//
//    private double totarea;
//
//    private double vlratestmtotarea;
//
//    private double vlrat;
//
//    @Column(columnDefinition = "char")
//    private String strctcd;
//
//    private String strctcdnm;
//
//    private String etcstrct;
//
//    private String mainpurpscd;
//
//    private String mainPurpsCdNm;
//
//    private String etcPurps;
//
//    private String roofCd;
//
//    private String roofCdNm;
//
//    private String etcRoof;
//
//    private Integer hhldCnt;
//
//    private Integer fmlyCnt;
//
//    private double heit;
//
//    private Integer grndFlrCnt;
//
//    private Integer ugrndFlrCnt;
//
//    private Integer rideUseElvtCnt;
//
//    private Integer emgenUseElvtCnt;
//
//    private Integer atchBldCnt;
//
//    private double atchBldArea;
//
//    private double totDongTotArea;
//
//    private Integer indrMechUtcnt;
//
//    private double indrMechArea;
//
//    private Integer oudrMechUtcnt;
//
//    private double oudrMechArea;
//
//    private Integer indrAutoUtcnt;
//
//    private double indrAutoArea;
//
//    private Integer oudrAutoUtcnt;
//
//    private double oudrAutoArea;
//
//    private String pmsDay;
//
//    private String stcnsDay;
//
//    private String useAprDay;
//
//    private String pmsnoYear;
//
//    @Column(columnDefinition = "char")
//    private String pmsnoKikCd;
//
//    private String pmsnoKikCdNm;
//
//    private String pmsnoGbCd;
//
//    private String pmsnoGbCdNm;
//
//    private Integer hoCnt;
//
//    private String engrGrade;
//
//    private double engrRat;
//
//    private Integer engrEpi;
//
//    @Column(columnDefinition = "char")
//    private String gnBldGrade;
//
//    private Integer gnBldCert;
//
//    @Column(columnDefinition = "char")
//    private String itgBldGrade;
//
//    private Integer itgBldCert;
//
//    private String crtnDay;

}
