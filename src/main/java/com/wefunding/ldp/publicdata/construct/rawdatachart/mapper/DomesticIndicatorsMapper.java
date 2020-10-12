package com.wefunding.ldp.publicdata.construct.rawdatachart.mapper;

import com.wefunding.ldp.publicdata.construct.rawdatachart.dto.DomesticIndicatorsDto;
import com.wefunding.ldp.publicdata.construct.rawdatachart.entity.DomesticIndicatorsEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by yes on 2020/10/12
 */
@Mapper
public interface DomesticIndicatorsMapper {
    List<DomesticIndicatorsDto> toDomesticIndicatorsDtoList(List<DomesticIndicatorsEntity> domesticIndicatorsEntityList);
}
