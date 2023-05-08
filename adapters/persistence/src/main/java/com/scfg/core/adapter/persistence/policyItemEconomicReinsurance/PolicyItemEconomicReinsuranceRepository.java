package com.scfg.core.adapter.persistence.policyItemEconomicReinsurance;

import com.scfg.core.domain.common.PolicyItemEconomicReinsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyItemEconomicReinsuranceRepository extends JpaRepository<PolicyItemEconomicReinsuranceJpaEntity, Long> {

    List<PolicyItemEconomicReinsuranceJpaEntity> findAllByPolicyItemEconomicIdAndStatus(Long policyItemEconomicId, Integer status);


    @Modifying
    @Query("UPDATE PolicyItemEconomicReinsuranceJpaEntity " +
            "SET status = :status " +
            "WHERE policyItemEconomicId = :policyItemEconomicId")
    void setStatusByPolicyEconomicId(@Param("status") Integer status, @Param("policyItemEconomicId") Long policyItemEconomicId);

}
