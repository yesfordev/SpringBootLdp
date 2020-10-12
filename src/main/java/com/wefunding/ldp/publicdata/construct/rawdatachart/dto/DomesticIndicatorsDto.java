package com.wefunding.ldp.publicdata.construct.rawdatachart.dto;

import lombok.Data;

/**
 * Created by yes on 2020/10/12
 */
@Data
public class DomesticIndicatorsDto {

    private Integer id;

    private String indicator;

    private double realGdpGrowth;

    private double inflation;

    private double privateConsumptionGrowth;

    private double grossGdp;

    private double exchangeRate;

    private double importUsd;

    private double importYoy;

    private double exportUsd;

    private double exportYoy;

    private double populationMillion;

    private double populationYoy;

    private double unemployment;

}
