package com.scfg.core.adapter.persistence.subscriptionTracking;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionTrackingRepository extends JpaRepository<SubscriptionTrackingJpaEntity, Long> {
    @Query("SELECT sbt " +
            "FROM SubscriptionTrackingJpaEntity sbt " +
            "JOIN FETCH sbt.regional c "+
            "JOIN FETCH sbt.client ope "+
            "JOIN FETCH sbt.manager ag "+
            "JOIN FETCH sbt.agency ct "+
            "JOIN FETCH sbt.insuranceRequest cv "+
            "JOIN FETCH sbt.currency cur "+
            "JOIN FETCH sbt.coverage cov "+
            "JOIN FETCH sbt.mortgageReliefItem mri "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE sbt.mortgageReliefItem.activeRecord = 1 " +
            "AND sbt.mortgageReliefItem.loadMonth.id = :monthId AND sbt.mortgageReliefItem.loadYear.id = :yearId " +
            "AND sbt.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND sbt.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND sbt.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND sbt.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND sbt.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<SubscriptionTrackingJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT sbt " +
            "FROM SubscriptionTrackingJpaEntity sbt " +
            "JOIN FETCH sbt.regional c "+
            "JOIN FETCH sbt.client ope "+
            "JOIN FETCH sbt.manager ag "+
            "JOIN FETCH sbt.agency ct "+
            "JOIN FETCH sbt.insuranceRequest cv "+
            "JOIN FETCH sbt.currency cur "+
            "JOIN FETCH sbt.coverage cov "+
            "JOIN FETCH sbt.mortgageReliefItem mri "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE sbt.mortgageReliefItem.activeRecord = 1 " +
            "AND sbt.client.documentNumber = :documentNumber AND sbt.operationNumber = :operationNumber " +
            "AND sbt.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND sbt.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND sbt.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND sbt.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND sbt.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<SubscriptionTrackingJpaEntity> findAllByClient_DocumentNumberAndOperationNumber(
            @Param("documentNumber") String documentNumber,
            @Param("operationNumber") Long operationNumber,
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);
}
