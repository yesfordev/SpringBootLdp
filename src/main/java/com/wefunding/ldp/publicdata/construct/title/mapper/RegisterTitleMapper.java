package com.wefunding.ldp.publicdata.construct.title.mapper;

import com.wefunding.ldp.publicdata.construct.title.dto.Item;
import com.wefunding.ldp.publicdata.construct.title.entity.RegisterTitleEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterTitleMapper {
    RegisterTitleEntity toRegisterTitleEntity(Item item);
}
