package com.scfg.core.adapter.persistence.policyItemMathReserve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyItemMathReserveRepository extends JpaRepository<PolicyItemMathReserveJpaEntity,Long> {

    @Query("SELECT p FROM PolicyItemMathReserveJpaEntity p \n" +
            "WHERE p.policyItemId = :policyItemId AND p.status = :status")
    List<PolicyItemMathReserveJpaEntity> findAllByPolicyItemId(@Param("policyItemId") Long policyItemId, @Param("status") Integer status);
}
