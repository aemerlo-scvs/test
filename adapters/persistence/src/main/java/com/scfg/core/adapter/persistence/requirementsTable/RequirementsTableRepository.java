package com.scfg.core.adapter.persistence.requirementsTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequirementsTableRepository extends JpaRepository<RequirementsTableJpaEntity, Long> {

    @Query("SELECT r from RequirementsTableJpaEntity r " +
            "JOIN PlanJpaEntity pl ON pl.id = r.planId " +
            "JOIN GeneralRequestJpaEntity gr on gr.planId = pl.id " +
            "WHERE gr.id = :requestId AND r.status = :status AND pl.status = :status AND gr.status = :status")
    List<RequirementsTableJpaEntity> findAllByRequestId(@Param("requestId") long requestId, @Param("status") int status);
}
