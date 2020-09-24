package com.wefunding.ldp.publicdata.construct.license.mapper;

import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.Item;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseBasicEntity;
import org.mapstruct.Mapper;

/**
 * Created by yes on 2020/09/24
 */
@Mapper
public interface LicenseBasicEntityMapper {

    LicenseBasicEntity toLicenseBasicEntity(Item item);

}
