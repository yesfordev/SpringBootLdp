package com.wefunding.ldp.publicdata.construct.license.dto.licensedongdto;

import lombok.Builder;
import lombok.Data;

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
}
