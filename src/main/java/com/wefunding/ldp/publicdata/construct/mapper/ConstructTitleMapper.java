package com.wefunding.ldp.publicdata.construct.mapper;

import com.wefunding.ldp.publicdata.construct.dto.Item;
import com.wefunding.ldp.publicdata.construct.entity.ConstructTitleEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ConstructTitleMapper {
    ConstructTitleEntity toConstructTitleEntity(Item item);
}
