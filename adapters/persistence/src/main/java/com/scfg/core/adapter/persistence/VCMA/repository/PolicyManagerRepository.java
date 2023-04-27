package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.PolicyManagerJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyManagerRepository extends CrudRepository<PolicyManagerJpaEntity,String> {
    //List<PolicyManager> findByPolicy_number(String number);
}
