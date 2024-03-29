package com.wefunding.ldp.publicdata.construct.license.mapper;

import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.Item;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseBasicEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by yes on 2020/09/24
 */
@Mapper
public interface LicenseBasicEntityMapper {

    LicenseBasicEntity toLicenseBasicEntity(Item item);

    List<LicenseBasicEntity> toLicenseBasicEntityList(List<Item> itemList);

}
