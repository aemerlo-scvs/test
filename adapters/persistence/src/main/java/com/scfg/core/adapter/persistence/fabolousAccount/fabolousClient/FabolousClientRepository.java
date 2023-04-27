package com.scfg.core.adapter.persistence.fabolousAccount.fabolousClient;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabolousClientRepository extends JpaRepository<FabolousClientJpaEntity, Long> {
    @Query("SELECT B FROM FabolousClientJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousClientJpaEntity> getAll();
}
