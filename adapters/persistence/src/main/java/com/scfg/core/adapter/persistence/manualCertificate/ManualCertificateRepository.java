package com.scfg.core.adapter.persistence.manualCertificate;

import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManualCertificateRepository extends JpaRepository<ManualCertificateJpaEntity, Long> {


    @Query("SELECT man " +
            "FROM ManualCertificateJpaEntity man " +
            "JOIN FETCH man.insuranceRequest req "+
            "JOIN FETCH man.client cl "+
            "JOIN FETCH man.manager mg "+
            "JOIN FETCH man.agency ag "+
            "JOIN FETCH man.currency cr "+
            "JOIN FETCH man.coverage cv "+
            "JOIN FETCH man.creditType ct "+
            "JOIN FETCH man.mortgageReliefItem mri "+
            "JOIN FETCH req.requestStatus reqSts "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE man.mortgageReliefItem.activeRecord = 1 " +
            "AND man.mortgageReliefItem.loadMonth.id = :monthId AND man.mortgageReliefItem.loadYear.id = :yearId " +
            "AND man.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND man.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND man.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND man.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND man.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<ManualCertificateJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT man " +
            "FROM ManualCertificateJpaEntity man " +
            "JOIN FETCH man.insuranceRequest req "+
            "JOIN FETCH man.client cl "+
            "JOIN FETCH man.manager mg "+
            "JOIN FETCH man.agency ag "+
            "JOIN FETCH man.currency cr "+
            "JOIN FETCH man.coverage cv "+
            "JOIN FETCH man.creditType ct "+
            "JOIN FETCH man.mortgageReliefItem mri "+
            "JOIN FETCH req.requestStatus reqSts "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE man.mortgageReliefItem.activeRecord = 1 " +
            "AND man.client.documentNumber = :documentNumber AND man.creditOperationNumber = :operationNumber " +
            "AND man.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND man.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND man.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND man.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND man.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<ManualCertificateJpaEntity> findAllByClient_DocumentNumberAndCreditOperationNumber(
            @Param("documentNumber") String documentNumber,
            @Param("operationNumber") Long operationNumber,
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);
    //List<ClasificadorJpaEntity> findAll();
    // Provisional
    //ClassifierJpaEntity findById(long classifierId);

    //ClassifierJpaEntity findById(long Id);
    //List<ClassifierJpaEntity> findAllByClassifierTypeId

}
