package com.wefunding.ldp.publicdata.construct.mapper;

import org.mapstruct.Mapper;
import com.wefunding.ldp.publicdata.construct.dto.Item;
import com.wefunding.ldp.publicdata.construct.entity.TestLeeResultEntity;

/*
Dto to Entity Mapper
 */
@Mapper
public interface TestLeeResultMapper {
    TestLeeResultEntity toTestLeeResultEntity(Item item);
}
