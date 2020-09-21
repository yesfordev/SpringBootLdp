package com.wefunding.ldp.publicdata.construct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wefunding.ldp.publicdata.construct.entity.TestLeeEntity;

import java.util.List;
import java.util.Optional;

public interface TestLeeEntityRepository extends JpaRepository<TestLeeEntity, Integer> {

    List<TestLeeEntity> findAll();

    Optional<TestLeeEntity> findById(Integer id);

    List<TestLeeEntity> findAllByStatus(String status);

}