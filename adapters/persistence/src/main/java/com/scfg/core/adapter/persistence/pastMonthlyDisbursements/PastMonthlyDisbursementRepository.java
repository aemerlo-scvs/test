package com.scfg.core.adapter.persistence.pastMonthlyDisbursements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PastMonthlyDisbursementRepository extends JpaRepository<PastMonthlyDisbursementJpaEntity, Long> {

    @Query("SELECT mon " +
            "FROM PastMonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user " + // Sin uso
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.mortgageReliefItem.loadMonth.id = :monthId AND mon.mortgageReliefItem.loadYear.id = :yearId " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<PastMonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT mon " +
            "FROM PastMonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user " + // Sin uso
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.mortgageReliefItem.loadMonth.order = :monthNumber AND mon.mortgageReliefItem.loadYear.order = :year " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND ((EXTRACT(month FROM ope.disbursementDate) <> :monthNumber) OR (EXTRACT(month FROM ope.disbursementDate) = :monthNumber AND EXTRACT(year FROM ope.disbursementDate) <> :year)) " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<PastMonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndDistinctMonth_NumberAndDistinctYear_NumberAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthNumber") long monthNumber,
            @Param("year") long year,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT mon " +
            "FROM PastMonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user " + // Sin uso
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.mortgageReliefItem.loadMonth.order = :monthNumber AND mon.mortgageReliefItem.loadYear.order = :year " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<PastMonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndMonth_NumberAndYear_NumberAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthNumber") long monthNumber,
            @Param("year") long year,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);


    @Query("SELECT b " +
            "FROM PastMonthlyDisbursementJpaEntity b " +
            "JOIN FETCH b.mortgageReliefItem mri " +
            "JOIN FETCH b.creditOperation " +
            "JOIN FETCH b.client " +
            "WHERE b.mortgageReliefItem.activeRecord = 1 " +
            "AND b.creditOperation.operationNumber = :creditOperationNumber AND b.client.documentNumber LIKE CONCAT('%',:documentNumber, '%')")
    List<PastMonthlyDisbursementJpaEntity> findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
            @Param("creditOperationNumber") Long creditOperationNumber,
            @Param("documentNumber") String documentNumber);


    @Query("SELECT COALESCE(SUM(mont.insuredValue),0) " +
            "FROM PastMonthlyDisbursementJpaEntity mont " +
            "JOIN mont.client cl "+
            "JOIN mont.mortgageReliefItem mri " +
            "WHERE mri.activeRecord = 1 " +
            "AND mri.loadMonth.id = :monthId " +
            "AND mri.loadYear.id = :yearId " +
            "AND cl.documentNumber = :documentNumber ")
    Double getCumulusByClientIdAndMonthIdAndYearId(
            @Param("documentNumber") String documentNumber,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId
    );


    @Query("SELECT mon " +
            "FROM PastMonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.client cl " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "WHERE mri.activeRecord = 1 " +
            "AND EXTRACT(month FROM ope.disbursementDate) = :monthNumber " +
            "AND EXTRACT(year FROM ope.disbursementDate) = :year " +
            "AND cl.documentNumber = :documentNumber")
    List<PastMonthlyDisbursementJpaEntity> findMonthlyDisbursementByClientAndMonthAndYear(
            @Param("documentNumber") String documentNumber,
            @Param("monthNumber") long monthNumber,
            @Param("year") long year);




}
