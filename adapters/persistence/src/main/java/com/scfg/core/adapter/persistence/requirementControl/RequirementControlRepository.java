package com.scfg.core.adapter.persistence.requirementControl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RequirementControlRepository extends JpaRepository<RequirementControlJpaEntity, Long> {

    @Query("SELECT r " +
            "FROM RequirementControlJpaEntity r " +
            "JOIN PolicyItemJpaEntity pi ON " +
            "pi.id = r.policyItemId " +
            "JOIN GeneralRequestJpaEntity gr ON " +
            "gr.id = pi.generalRequestId " +
            "WHERE gr.id = :requestId AND r.status = 1")
    List<RequirementControlJpaEntity> findAllByRequestId(@Param("requestId") Long requestId);
}
