package com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousBranch.FabolousBranchJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FabolousAgencyRepository extends JpaRepository<FabolousAgencyJpaEntity, Long> {
    @Query("SELECT B FROM FabolousAgencyJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousAgencyJpaEntity> getAll();
}
