package com.wefunding.ldp.publicdata.construct.rawdatachart.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by yes on 2020/10/12
 */

@Data
@Entity
@Table(name = "domestic_indicators", schema = "construct")
public class DomesticIndicatorsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String indicator;

    private double realGdpGrowth;

    private double inflation;

    private double privateConsumptionGrowth;

    @Column(nullable = true)
    private double grossGdp;

    @Column(nullable = true)
    private double exchangeRate;

    private double importUsd;

    private double importYoy;

    private double exportUsd;

    private double exportYoy;

    private double populationMillion;

    private double populationYoy;

    private double unemployment;
}
