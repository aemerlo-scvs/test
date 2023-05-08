package com.scfg.core.adapter.persistence.annexeRequirement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnexeRequirementRepository extends JpaRepository<AnnexeRequirementJpaEntity, Long>{

    List<AnnexeRequirementJpaEntity> findAllByAnnexeTypeIdAndStatus(Long annexeTypeId, Integer status);

    @Query("SELECT r FROM AnnexeRequirementJpaEntity r " +
            "WHERE r.status = :status")
    List<AnnexeRequirementJpaEntity> findAll(@Param("status") Integer status);



}
