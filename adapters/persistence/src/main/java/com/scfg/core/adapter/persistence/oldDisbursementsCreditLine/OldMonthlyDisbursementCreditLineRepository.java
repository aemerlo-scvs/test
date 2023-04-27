package com.scfg.core.adapter.persistence.oldDisbursementsCreditLine;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OldMonthlyDisbursementCreditLineRepository extends JpaRepository<OldMonthlyDisbursementCreditLineJpaEntity, Long> {


    @Query("SELECT old " +
            "FROM OldMonthlyDisbursementCreditLineJpaEntity old " +
            "JOIN FETCH old.regional r "+
            "JOIN FETCH old.client cl "+
            "JOIN FETCH old.agency ag "+
            "JOIN FETCH old.insuranceRequest req "+
            "JOIN FETCH old.currency cr "+
            "JOIN FETCH old.mortgageReliefItem mri "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE old.mortgageReliefItem.loadMonth.id = :monthId AND old.mortgageReliefItem.loadYear.id = :yearId " +
            "AND old.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND old.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND old.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND old.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND old.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<OldMonthlyDisbursementCreditLineJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    //List<ClasificadorJpaEntity> findAll();
    // Provisional
    //ClassifierJpaEntity findById(long classifierId);

    //ClassifierJpaEntity findById(long Id);
    //List<ClassifierJpaEntity> findAllByClassifierTypeId

}
