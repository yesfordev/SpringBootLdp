package com.wefunding.ldp.publicdata.construct.dto;

import lombok.Data;

@Data
public class Body {

    private Items items;

    private Integer numOfRows;

    private Integer pageNo;

    private Integer totalCount;

}
