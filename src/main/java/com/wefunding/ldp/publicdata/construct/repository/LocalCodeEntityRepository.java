package com.wefunding.ldp.publicdata.construct.repository;

import com.wefunding.ldp.publicdata.construct.entity.LocalCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalCodeEntityRepository extends JpaRepository<LocalCodeEntity, Integer> {

    @Query(value = "select * from local_code where name like '서울특별시%'", nativeQuery = true)
    List<LocalCodeEntity> getSeoulConstruct();

    @Query(value = "select * from local_code l where l.name like %:localName%", nativeQuery = true)
    List<LocalCodeEntity> getLocalConstruct(@Param("localName") String localName);
}
