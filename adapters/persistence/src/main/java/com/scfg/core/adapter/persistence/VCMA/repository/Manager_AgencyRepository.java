package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.Manager_AgencyJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Manager_AgencyRepository extends CrudRepository<Manager_AgencyJpaEntity, Integer> {
//    Manager_Agency findByMANAGER_AGENCY_ID(BigInteger id);
//    Manager_Agency findByMANAGER_IDAndAGENCY_ID(BigInteger managerId, BigInteger agencyId);
    List<Manager_AgencyJpaEntity> findAll();
}
