package com.wefunding.ldp.publicdata.construct.rawdatachart.service.impl;

import com.wefunding.ldp.publicdata.construct.rawdatachart.dto.DomesticIndicatorsDto;
import com.wefunding.ldp.publicdata.construct.rawdatachart.entity.DomesticIndicatorsEntity;
import com.wefunding.ldp.publicdata.construct.rawdatachart.mapper.DomesticIndicatorsMapper;
import com.wefunding.ldp.publicdata.construct.rawdatachart.model.DefaultRes;
import com.wefunding.ldp.publicdata.construct.rawdatachart.repository.DomesticIndicatorsRepository;
import com.wefunding.ldp.publicdata.construct.rawdatachart.service.DomesticIndicatorsService;
import com.wefunding.ldp.publicdata.construct.rawdatachart.utils.Message;
import com.wefunding.ldp.publicdata.construct.rawdatachart.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yes on 2020/10/12
 */

@Service
public class DomesticIndicatorsServiceImpl implements DomesticIndicatorsService {

    private final DomesticIndicatorsRepository domesticIndicatorsRepository;

    private final DomesticIndicatorsMapper domesticIndicatorsMapper;

    @Autowired
    public DomesticIndicatorsServiceImpl(DomesticIndicatorsRepository domesticIndicatorsRepository, DomesticIndicatorsMapper domesticIndicatorsMapper) {
        this.domesticIndicatorsRepository = domesticIndicatorsRepository;
        this.domesticIndicatorsMapper = domesticIndicatorsMapper;
    }

    @Override
    public DefaultRes<List<DomesticIndicatorsDto>> findDomesticIndicatorsList() {
        List<DomesticIndicatorsEntity> domesticIndicatorsEntityList = domesticIndicatorsRepository.findAll();
        List<DomesticIndicatorsDto> domesticIndicatorsDtoList = domesticIndicatorsMapper.toDomesticIndicatorsDtoList(domesticIndicatorsEntityList);

        if(domesticIndicatorsDtoList.isEmpty()) {
            System.out.println("test3");

            return DefaultRes.res(Status.NOT_FOUND, Message.NOT_FOUND_DOMESTIC_INDICATORS);
        }
        System.out.println("test4");

        return DefaultRes.res(Status.OK, Message.FIND_DOMESTIC_INDICATORS_LIST, domesticIndicatorsDtoList);
    }
}
