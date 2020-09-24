package com.wefunding.ldp.publicdata.construct.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "local_code", schema = "construct")
public class LocalCodeEntity {

    @Id
    private Integer id;

    private String sigunguCd;

    private String bjdongCd;

    private String name;

    private String status;

    private String depth;

    @CreationTimestamp
    private LocalDateTime regDt;

    @UpdateTimestamp
    private LocalDateTime updDt;
}
