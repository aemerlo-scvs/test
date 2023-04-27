package com.scfg.core.adapter.persistence.consolidatedObservedCase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsolidatedObservedCaseRepository extends JpaRepository<ConsolidatedObservedCaseJpaEntity, Long> {


    @Query("SELECT cobs " +
            "FROM ConsolidatedObservedCaseJpaEntity cobs " +
            "JOIN FETCH cobs.insuranceRequest r "+
            "JOIN FETCH cobs.observedCase obs "+
            "JOIN FETCH cobs.currency cr "+
            "JOIN FETCH obs.mortgageReliefItem mri "+
            "JOIN FETCH obs.creditOperation ope "+
            "JOIN FETCH obs.client clt "+
            "JOIN FETCH r.requestStatus reqSt "+
            "JOIN FETCH mri.user "+
            "WHERE obs.mortgageReliefItem.activeRecord = 1 " +
            "AND cobs.observedCase.mortgageReliefItem.loadMonth.id = :monthId AND cobs.observedCase.mortgageReliefItem.loadYear.id = :yearId " +
            "AND cobs.observedCase.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND cobs.observedCase.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND cobs.observedCase.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND cobs.observedCase.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND cobs.observedCase.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<ConsolidatedObservedCaseJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode
    );
}
