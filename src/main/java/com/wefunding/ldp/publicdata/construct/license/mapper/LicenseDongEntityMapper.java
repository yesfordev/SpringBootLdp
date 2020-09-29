package com.wefunding.ldp.publicdata.construct.license.mapper;

import com.wefunding.ldp.publicdata.construct.license.dto.licensedongdto.Item;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseDongEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by yes on 2020/09/28
 */
@Mapper
public interface LicenseDongEntityMapper {

    LicenseDongEntity toLicenseDongEntity(Item item);

    List<LicenseDongEntity> toLicenseDongEntityList(List<Item> itemList);

}
