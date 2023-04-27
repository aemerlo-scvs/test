package com.scfg.core.adapter.persistence.fabolousAccount.fabolousBranch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabolousBranchRepository extends JpaRepository<FabolousBranchJpaEntity, Long> {

    @Query("SELECT B FROM FabolousBranchJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousBranchJpaEntity> getAll();
}
