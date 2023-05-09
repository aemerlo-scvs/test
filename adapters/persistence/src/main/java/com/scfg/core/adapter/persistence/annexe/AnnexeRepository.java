package com.scfg.core.adapter.persistence.annexe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AnnexeRepository extends JpaRepository<AnnexeJpaEntity, Long>{

    @Query("SELECT MAX(a.annexeNumber) FROM AnnexeJpaEntity a " +
            "WHERE a.policy =:policyId ")
    Long getNumberAnnexe(@Param("policyId") Long policyId);

    @Query("SELECT p FROM AnnexeJpaEntity p " +
            "WHERE p.requestAnnexe = :requestAnnexeId AND p.status = :status AND p.annexeTypeIdc = :annexeTypeId " +
            "ORDER BY p.id desc")
    List<AnnexeJpaEntity> getOptionalByAnnexeTypeIdAndRequestAnnexeId(
            @Param("annexeTypeId") Integer annexeTypeId,
            @Param("requestAnnexeId") Long requestAnnexeId,
            @Param("status") Integer status);

    @Query(value = "SELECT p FROM AnnexeJpaEntity p " +
            "WHERE p.id = :annexeId AND p.status = :status")
    Optional<AnnexeJpaEntity> findOptionalById(@Param("annexeId") Long annexeId, @Param("status") Integer status);
}
