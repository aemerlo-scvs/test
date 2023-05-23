package com.scfg.core.adapter.persistence.policyItemMathReserve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyItemMathReserveRepository extends JpaRepository<PolicyItemMathReserveJpaEntity,Long> {

    @Query("SELECT p FROM PolicyItemMathReserveJpaEntity p \n" +
            "WHERE p.policyItemId = :policyItemId AND p.status = :status")
    List<PolicyItemMathReserveJpaEntity> findAllByPolicyItemId(@Param("policyItemId") Long policyItemId, @Param("status") Integer status);


    @Query("SELECT p FROM PolicyItemMathReserveJpaEntity p " +
            "WHERE p.policyItemId = :policyItemId " +
            "AND p.year = :year " +
            "AND p.status = :status")
    PolicyItemMathReserveJpaEntity findByPolicyItemIdAndYear(@Param("policyItemId") Long policyItemId,
                                                                   @Param("year") Integer year,
                                                                   @Param("status") Integer status);
}
