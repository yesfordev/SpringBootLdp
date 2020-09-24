package com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto;

import lombok.Data;

@Data
public class Body {

    private Items items;

    private Integer numOfRows;

    private Integer pageNo;

    private Integer totalCount;

}
