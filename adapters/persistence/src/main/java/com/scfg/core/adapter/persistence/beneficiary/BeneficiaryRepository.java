package com.scfg.core.adapter.persistence.beneficiary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<BeneficiaryJpaEntity, Long> {

    List<BeneficiaryJpaEntity> findAllByPolicyItemId(long policyItemId);

    @Query("SELECT b FROM BeneficiaryJpaEntity b " +
            "INNER JOIN PolicyItemJpaEntity pit on pit.id = b.policyItemId " +
            "INNER JOIN PolicyJpaEntity p ON p.id = pit.policyId " +
            "INNER JOIN GeneralRequestJpaEntity gr on gr.id = p.generalRequestId " +
            "WHERE gr.id = :generalRequestId " +
            "AND b.status = :status " +
            "AND gr.status = :status " +
            "AND p.status = :status " +
            "AND pit.status =:status ")
    List<BeneficiaryJpaEntity> findAllByGeneralRequestId(
                                        @Param("generalRequestId") Long generalRequestId,
                                        @Param("status") Integer status);
}
