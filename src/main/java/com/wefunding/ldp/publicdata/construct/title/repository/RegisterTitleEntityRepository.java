package com.wefunding.ldp.publicdata.construct.title.repository;

import com.wefunding.ldp.publicdata.construct.title.entity.RegisterTitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTitleEntityRepository extends JpaRepository<RegisterTitleEntity, Integer> {
}
