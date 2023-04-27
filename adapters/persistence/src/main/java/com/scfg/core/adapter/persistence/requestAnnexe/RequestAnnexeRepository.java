package com.scfg.core.adapter.persistence.requestAnnexe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestAnnexeRepository extends JpaRepository<RequestAnnexeJpaEntity, Long> {
    List<RequestAnnexeJpaEntity> findAllByPolicyIdAndAnnexeTypeIdAndStatusIdc(Long policyId, Long annexeTypeId, Integer status );

    @Query("SELECT r FROM RequestAnnexeJpaEntity r " +
            "WHERE r.status = :status")
    List<RequestAnnexeJpaEntity> findAll(@Param("status") Integer status);

}
