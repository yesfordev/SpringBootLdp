package com.wefunding.ldp.publicdata.construct.rawdatachart.service;

import com.wefunding.ldp.publicdata.construct.rawdatachart.dto.DomesticIndicatorsDto;
import com.wefunding.ldp.publicdata.construct.rawdatachart.model.DefaultRes;

import java.util.List;

/**
 * Created by yes on 2020/10/12
 */
public interface DomesticIndicatorsService {

    DefaultRes<List<DomesticIndicatorsDto>> findDomesticIndicatorsList();
}
