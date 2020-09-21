package com.wefunding.ldp.publicdata.construct.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Data
@Table(name = "test_lee", schema = "public")
public class TestLeeEntity {

    @Id
    private Integer id;

    private String sigungucd;

    private String bjdongcd;

    private String name;

    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestLeeEntity testLeeEntity = (TestLeeEntity) o;
        return Objects.equals(id, testLeeEntity.id) &&
                Objects.equals(sigungucd, testLeeEntity.sigungucd) &&
                Objects.equals(bjdongcd, testLeeEntity.bjdongcd) &&
                Objects.equals(name, testLeeEntity.name) &&
                Objects.equals(status, testLeeEntity.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sigungucd, bjdongcd, name, status);
    }
}
