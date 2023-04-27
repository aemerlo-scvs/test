package com.scfg.core.adapter.persistence.sinister;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SinisterRepository extends JpaRepository<SinisterJpaEntity, Long> {

    @Query("SELECT s " +
            "FROM SinisterJpaEntity s " +
            "JOIN FETCH s.mortgageReliefItem mri " +
            "JOIN FETCH s.client cl " +
            "JOIN FETCH mri.user usr " +
            "WHERE s.mortgageReliefItem.activeRecord = 1 " +
            "AND s.mortgageReliefItem.loadMonth.id = :monthId AND s.mortgageReliefItem.loadYear.id = :yearId " +
            "AND s.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND s.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND s.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND s.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND s.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<SinisterJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode
    );

    @Query("SELECT s " +
            "FROM SinisterJpaEntity s " +
            "JOIN FETCH s.mortgageReliefItem mri " +
            "JOIN FETCH s.client cl " +
            "JOIN FETCH mri.user usr " +
            "WHERE s.mortgageReliefItem.activeRecord = 1 " +
            "AND s.client.documentNumber LIKE CONCAT('%',:documentNumber, '%')"  +
            "AND s.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND s.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND s.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND s.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND s.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<SinisterJpaEntity> findAllByClient_DocumentNumber(
            @Param("documentNumber") String documentNumber,
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode
    );
    //Optional<SinisterJpaEntity> findByOperationNumber(long operationNumber);
}
