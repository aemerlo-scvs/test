package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManagerAgency;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabolousManagerAgencyRepository extends JpaRepository<FabolousManagerAgencyJpaEntity, Long> {
    @Query("SELECT B FROM FabolousManagerAgencyJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousManagerAgencyJpaEntity> getAll();
}
