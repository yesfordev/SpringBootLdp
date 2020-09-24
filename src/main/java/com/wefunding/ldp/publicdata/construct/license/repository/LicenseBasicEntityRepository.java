package com.wefunding.ldp.publicdata.construct.license.repository;

import com.wefunding.ldp.publicdata.construct.license.entity.LicenseBasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yes on 2020/09/24
 */
public interface LicenseBasicEntityRepository extends JpaRepository<LicenseBasicEntity, Integer> {
}
