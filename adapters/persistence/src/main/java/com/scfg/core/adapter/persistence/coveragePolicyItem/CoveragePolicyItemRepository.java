package com.scfg.core.adapter.persistence.coveragePolicyItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CoveragePolicyItemRepository extends JpaRepository<CoveragePolicyItemJpaEntity, Long> {


    @Modifying
    @Query("DELETE FROM CoveragePolicyItemJpaEntity WHERE policyItemId = :policyItemId")
    void customDeleteAllByPolicyItemId(@Param("policyItemId") Long policyItemId);

    Long deleteAllByPolicyItemId(@Param("policyItemId") Long policyItemId);

    List<CoveragePolicyItemJpaEntity> findAllByPolicyItemId(Long policyItemId);

    @Query("select cvp from CoveragePolicyItemJpaEntity cvp \n" +
            "join PolicyItemJpaEntity p on cvp.policyItemId = p.id\n " +
            "join GeneralRequestJpaEntity g on g.id = p.generalRequestId\n " +
            "where g.personId = :personId")
    List<CoveragePolicyItemJpaEntity> findAllByPersonId(@Param("personId") Long personId);

    @Query("select cvp from CoveragePolicyItemJpaEntity cvp \n" +
            "join PolicyItemJpaEntity p on cvp.policyItemId = p.id\n " +
            "join GeneralRequestJpaEntity g on g.id = p.generalRequestId\n " +
            "where g.personId = :personId AND g.planId = :planId AND g.status = :status")
    List<CoveragePolicyItemJpaEntity> findAllByPersonIdGEL(@Param("personId") Long personId, @Param("planId") Long planId, @Param("status") Integer status);

}
