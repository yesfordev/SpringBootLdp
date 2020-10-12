package com.wefunding.ldp.publicdata.construct.common.repository;

import com.wefunding.ldp.publicdata.construct.common.entity.LocalCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalCodeEntityRepository extends JpaRepository<LocalCodeEntity, Integer> {

    @Query(value = "select * from construct.local_code lc where lc.name like '서울특별시%' and lc.depth>='3' and lc.status='1'", nativeQuery = true)
    List<LocalCodeEntity> getSeoulConstruct();

    @Query(value = "select * from construct.local_code lc where lc.name like %:localName% and lc.depth>='3' and lc.status='1'", nativeQuery = true)
    List<LocalCodeEntity> getLocalConstruct(@Param("localName") String localName);

    @Query(value = "select * from construct.local_code lc where lc.depth>='3' and lc.status='1'", nativeQuery = true)
    List <LocalCodeEntity> getLocalCodeEntityList();

    @Query(value = "select * from construct.local_code lc where lc.depth>='3' and lc.status='1' and lc.id >= 18565", nativeQuery = true)
    List <LocalCodeEntity> getLocalCodeEntityListById();
//    @Query(value = "select * from construct.local_code lc where lc.depth>='3' and lc.status='1' and lc.id between 7478 and 7551", nativeQuery = true)
//    List <LocalCodeEntity> getLocalCodeEntityListById();
}
