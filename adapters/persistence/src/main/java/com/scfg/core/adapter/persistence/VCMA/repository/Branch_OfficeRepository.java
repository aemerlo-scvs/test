package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.Branch_OfficeJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Branch_OfficeRepository extends CrudRepository<Branch_OfficeJpaEntity, Integer> {
//    Branch_Office findByBRANCH_OFFICE_ID(BigInteger id);
//    Branch_Office findByDESCRIPTION(String name);
    List<Branch_OfficeJpaEntity> findAll();
}
