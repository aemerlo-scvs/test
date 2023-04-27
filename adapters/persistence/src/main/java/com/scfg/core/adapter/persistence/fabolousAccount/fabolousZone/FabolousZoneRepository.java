package com.scfg.core.adapter.persistence.fabolousAccount.fabolousZone;

import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabolousZoneRepository extends JpaRepository<FabolousZoneJpaEntity, Long> {
    @Query("SELECT B FROM FabolousZoneJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousZoneJpaEntity> getAll();
}
