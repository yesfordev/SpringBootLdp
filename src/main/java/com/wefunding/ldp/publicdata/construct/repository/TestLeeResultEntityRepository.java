package com.wefunding.ldp.publicdata.construct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wefunding.ldp.publicdata.construct.entity.TestLeeResultEntity;

import java.util.List;

public interface TestLeeResultEntityRepository extends JpaRepository<TestLeeResultEntity, String> {
//public interface TestLeeResultRepository extends JpaRepository<Item, String> {

    TestLeeResultEntity save(TestLeeResultEntity newTestLeeResult);

    List<TestLeeResultEntity> findAll();
}
