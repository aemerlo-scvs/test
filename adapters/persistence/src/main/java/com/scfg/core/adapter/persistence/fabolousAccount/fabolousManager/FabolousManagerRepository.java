package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManager;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabolousManagerRepository extends JpaRepository<FabolousManagerJpaEntity, Long> {
    @Query("SELECT B FROM FabolousManagerJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousManagerJpaEntity> getAll();
}
