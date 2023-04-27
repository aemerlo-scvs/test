package com.scfg.core.adapter.persistence.observedCase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObservedCaseRepository extends JpaRepository<ObservedCaseJpaEntity, Long> {

    List<ObservedCaseJpaEntity> findByClient_IdAndCreditOperation_IdAndMortgageReliefItem_IdIn(Long clientId,
                                                                                               Long creditOperationId,
                                                                                               List<Long> mortgageReliefItemIds);

    @Query("SELECT oc " +
            "FROM ObservedCaseJpaEntity oc " +
            "JOIN FETCH oc.creditOperation ope "+
            "JOIN FETCH oc.client cl "+
            "JOIN FETCH oc.mortgageReliefItem mri "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE oc.mortgageReliefItem.activeRecord = 1 " +
            "AND oc.mortgageReliefItem.loadMonth.id = :monthId AND oc.mortgageReliefItem.loadYear.id = :yearId " +
            "AND oc.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND oc.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND oc.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND oc.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND oc.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<ObservedCaseJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

}
