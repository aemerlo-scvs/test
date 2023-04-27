package com.scfg.core.adapter.persistence.monthlyDisbursements;

import com.scfg.core.adapter.persistence.lastCasesObserved.LastObservedCaseJpaEntity;
import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.LinkedList;
import java.util.List;

public interface MonthlyDisbursementRepository extends JpaRepository<MonthlyDisbursementJpaEntity, Long> {

    @Query("SELECT mon " +
            "FROM MonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user " + // Sin uso
            "JOIN FETCH mri.loadMonth lm " +
            "JOIN FETCH mri.loadYear ly " +
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.mortgageReliefItem.loadMonth.id = :monthId AND mon.mortgageReliefItem.loadYear.id = :yearId " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<MonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query(value = "SELECT * FROM dev01.MonthlyDisbursement mon \n" +
            "INNER JOIN dev01.Client cl \n" +
            "ON mon.clientId = cl.id \n" +
            "INNER JOIN dev01.CreditOperation ope \n" +
            "ON mon.creditOperationId = ope.id\n" +
            "INNER JOIN dev01.Classifier cla1 \n" +
            "ON mon.creditTypeIdc = cla1.id \n" +
            "INNER JOIN dev01.Classifier cla2 \n" +
            "ON mon.coverageIdc = cla2.id \n" +
            "INNER JOIN  dev01.Classifier cla3 \n" +
            "ON mon.currencyIdc = cla3.id \n" +
            "INNER JOIN dev01.MortgageReliefItem mri \n" +
            "ON mon.mortgageReliefItemId = mri.id \n" +
            "CROSS JOIN dev01.Classifier cla4 \n" +
            "CROSS JOIN dev01.Classifier cla5 \n" +
            "CROSS JOIN dev01.Classifier cla6 \n" +
            "CROSS JOIN dev01.ClassifierType clat1 \n" +
            "CROSS JOIN dev01.Classifier cla7 \n" +
            "CROSS JOIN dev01.ClassifierType clat2 \n" +
            "WHERE (mon.status = 1) \n" +
            "AND mri.monthIdc = cla4.id \n" +
            "AND mri.yearIdc = cla5.id \n" +
            "AND mri.policyTypeIdc = cla6.id \n" +
            "AND cla6.classifierTypeId = clat1.id \n" +
            "AND mri.reportTypeIdc = cla7.id \n" +
            "AND cla7.classifierTypeId = clat2.id \n" +
            "AND mri.activeRecord = 1 \n" +
            "AND cla4.[order] = :monthNumber \n" +
            "AND cla5.[order] = :yearNumber \n" +
            "AND ((MONTH(ope.disbursementDate) <> :monthNumber) OR (MONTH(ope.disbursementDate) = :monthNumber AND YEAR(ope.disbursementDate) <> :yearNumber))\n" +
            "AND mri.insurancePolicyHolderIdc = :insurancePolicyHolderId \n" +
            "AND clat1.referenceId = :policyReferenceCode \n" +
            "AND clat1.referenceId = :policyTypeReferenceCode \n" +
            "AND cla7.referenceId= :reportReferenceCode \n" +
            "AND clat2.referenceId = :reportTypeReferenceCode", nativeQuery = true)
    List<MonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndDistinctMonth_NumberAndDistinctYear_NumberAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthNumber") Integer monthNumber,
            @Param("yearNumber") Integer yearNumber,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT mon " +
            "FROM MonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mon.creditOperation " +
            "JOIN FETCH mon.client " +
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.creditOperation.operationNumber = :creditOperationNumber AND mon.client.documentNumber LIKE CONCAT('%',:documentNumber, '%')" +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<MonthlyDisbursementJpaEntity> findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
            @Param("creditOperationNumber") Long creditOperationNumber,
            @Param("documentNumber") String documentNumber,
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT mon " +
            "FROM MonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user " + // Sin uso
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.caseStatus = :caseStatus " +
            "AND mon.mortgageReliefItem.loadMonth.id = :monthId AND mon.mortgageReliefItem.loadYear.id = :yearId " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<MonthlyDisbursementJpaEntity> findAllByCaseStatus(
            @Param("caseStatus") Integer caseStatus,
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    @Query("SELECT mon " +
            "FROM MonthlyDisbursementJpaEntity mon " +
            "JOIN FETCH mon.client c " +
            "JOIN FETCH mon.creditOperation ope " +
            "JOIN FETCH mon.agency ag " +
            "JOIN FETCH mon.creditType ct " +
            "JOIN FETCH mon.coverage cv " +
            "JOIN FETCH mon.currency cr " +
            "JOIN FETCH mon.mortgageReliefItem mri " +
            "JOIN FETCH mri.user  " + // Sin uso
            "WHERE mon.mortgageReliefItem.activeRecord = 1 " +
            "AND mon.mortgageReliefItem.loadMonth.order = CONCAT(:monthNumber,'') AND mon.mortgageReliefItem.loadYear.order = CONCAT(:year,'') " +
            "AND mon.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND mon.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND mon.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND mon.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<MonthlyDisbursementJpaEntity> findAllByPolicyType_IdAndMonth_NumberAndYear_NumberAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthNumber") long monthNumber,
            @Param("year") long year,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);


    @Query("SELECT COALESCE(SUM(mont.insuredValue),0) " +
            "FROM MonthlyDisbursementJpaEntity mont " +
            "JOIN mont.client cl " +
            "JOIN mont.mortgageReliefItem mri " +
            "WHERE mri.activeRecord = 1 " +
            "AND cl.documentNumber = :documentNumber " +
            "AND mri.loadMonth.order = :monthId " +
            "AND mri.loadYear.order = :yearId ")
    Double getCumulusByClientIdAndMonthIdAndYearId(
            @Param("documentNumber") String documentNumber,
            @Param("monthId") Integer monthId,
            @Param("yearId") Integer yearId
    );

    @Query("SELECT COALESCE(SUM(mont.insuredValue),0) " +
            "FROM PastMonthlyDisbursementJpaEntity mont " +
            "JOIN mont.client cl " +
            "JOIN mont.mortgageReliefItem mri " +
            "WHERE mri.activeRecord = 1 " +
            "AND cl.documentNumber = :documentNumber " +
            "AND mri.loadMonth.order = :monthId " +
            "AND mri.loadYear.order = :yearId ")
    Double getCumulusByClientIdAndMonthIdAndYearIdPastMonthlyDisbursement(
            @Param("documentNumber") String documentNumber,
            @Param("monthId") Integer monthId,
            @Param("yearId") Integer yearId
    );


    @Query(value = "SELECT * FROM {h-schema}MonthlyDisbursement mon" +
            " JOIN {h-schema}CreditOperation ope" +
            " on ope.id = mon.creditOperationId" +
            " JOIN {h-schema}Client cl" +
            " on cl.id = mon.clientId" +
            " JOIN {h-schema}MortgageReliefItem mri" +
            " on mri.id = mon.mortgageReliefItemId" +
            " WHERE mri.activeRecord = 1" +
            " AND MONTH(ope.disbursementDate) = :monthNumber" +
            " AND YEAR(ope.disbursementDate) = :year" +
            " AND cl.documentNumber = :documentNumber", nativeQuery = true)
    List<MonthlyDisbursementJpaEntity> findMonthlyDisbursementByClientAndMonthAndYear(
            @Param("documentNumber") String documentNumber,
            @Param("monthNumber") long monthNumber,
            @Param("year") long year);


    @Query(value = "SELECT InsuredsSummary.ID_ASEGURADO, " +
            "InsuredsSummary.NOMBRE_COMPLETO, " +
            "InsuredsSummary.NRO_DOCUMENTO, " +
            "InsuredsSummary.TIPO_DOCUMENTO, " +
            "[NUEVO_DESEMBOLSO] = CASE " +
            "WHEN MONTH(InsuredsSummary.FECHA_ULTIMO_DESEMBOLSO) = InsuredsSummary.MES_CARGA AND " +
            "YEAR(InsuredsSummary.FECHA_ULTIMO_DESEMBOLSO) = InsuredsSummary.GESTION_CARGA " +
            "THEN 'SI' " +
            "ELSE 'NO' " +
            "END, " +
            "InsuredsSummary.FECHA_ULTIMO_DESEMBOLSO, " +
            "InsuredsSummary.MONTO_DESEMBOLSADO, " +
            "InsuredsSummary.MONTO_ACUMULADO " +
            "FROM ( " +
            "SELECT [ID_ASEGURADO] = t1.clientId,   " +
            "[NOMBRE_COMPLETO] = CONCAT(COALESCE(t3.names,''), " +
            "COALESCE(' '+t3.lastName,''),  " +
            "COALESCE(' '+t3.mothersLastName,''),  " +
            "COALESCE(' '+t3.marriedLastName,'') " +
            "), " +
            "[NRO_DOCUMENTO] = t3.documentNumber, " +
            "[TIPO_DOCUMENTO] =  t4.abbreviation, " +
            "[MONTO_DESEMBOLSADO] = t1.disbursedAmount,  " +
            "[ROW_CONTROL] = ROW_NUMBER() OVER  " +
            "(PARTITION BY /*t2.disbursementDate ,*/ t1.clientId ORDER BY t2.disbursementDate DESC), " +
            "[FECHA_ULTIMO_DESEMBOLSO] = MAX(t2.disbursementDate) OVER (PARTITION BY t2.disbursementDate, t1.clientId), " +
            "[MONTO_ACUMULADO] = SUM(t1.insuredValue) OVER (PARTITION BY t1.clientId), " +
            "[MES_CARGA] = t6.[order], " +
            "[GESTION_CARGA] = t7.[order] " +
            "FROM {h-schema}MonthlyDisbursement t1 " +
            "INNER JOIN {h-schema}CreditOperation t2 " +
            "ON t1.creditOperationId = t2.id " +
            "INNER JOIN {h-schema}Client t3 " +
            "ON t1.clientId = t3.id " +
            "INNER JOIN {h-schema}Classifier t4 " +
            "ON t3.documentTypeIdc = t4.id " +
            "INNER JOIN {h-schema}MortgageReliefItem t5 " +
            "ON t1.mortgageReliefItemId = t5.id " +
            "INNER JOIN {h-schema}Classifier t6 " +
            "ON t5.monthIdc = t6.id " +
            "INNER JOIN {h-schema}Classifier t7 " +
            "ON t5.yearIdc = t7.id " +
            "WHERE t5.monthIdc = :monthId AND " +
            "t5.yearIdc = :yearId AND  " +
            "t5.policyTypeIdc = :policyTypeId AND  " +
            "t5.insurancePolicyHolderIdc = :insurancePolicyHolderId " +
            ") AS InsuredsSummary " +
            "WHERE InsuredsSummary.[ROW_CONTROL] = 1 " +
            "ORDER BY InsuredsSummary.FECHA_ULTIMO_DESEMBOLSO DESC", nativeQuery = true)
    List<Object[]> getInsuredsSummary(
            @Param("policyTypeId") long policyTypeId,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId
    );

/*

Update Notification n
SET n.noticed = true
Where n.notificationPost.postId in (
    SELECT n2.notificationPost.postId
    FROM Notification n2
    where n2.notificationPost.owner.userId =?
 )


**/


    @Modifying
    @Query("UPDATE MonthlyDisbursementJpaEntity m " +
            "SET m.caseStatus = :caseStatus " +
            "WHERE m.creditOperation.id IN ( " +
            "SELECT m2.creditOperation.id FROM MonthlyDisbursementJpaEntity m2 WHERE m2.creditOperation.operationNumber = :creditOperationNumber) "+
            "AND m.client.id IN (" +
            "SELECT m3.client.id FROM MonthlyDisbursementJpaEntity m3 WHERE m3.client.documentNumber = :clientDocumentNumber)")
    void updateCaseStatus(@Param("creditOperationNumber") Long creditOperationNumber,
                          @Param("clientDocumentNumber") String clientDocumentNumber,
                          @Param("caseStatus") Integer caseStatus);

    @Modifying
    @Query("UPDATE MonthlyDisbursementJpaEntity m " +
            "SET m.caseStatus = :caseStatus " +
            "WHERE m.mortgageReliefItem.id IN (" +
            "SELECT m2.mortgageReliefItem.id " +
            "FROM MonthlyDisbursementJpaEntity m2 " +
            "WHERE m2.mortgageReliefItem.loadMonth.id = :monthId " +
            "AND m2.mortgageReliefItem.loadYear.id = :yearId " +
            "AND m2.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND m2.mortgageReliefItem.policyType.id = :policyTypeId)")
    void updateAllCaseStatus(
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("policyTypeId") long policyTypeId,
            @Param("caseStatus") int caseStatus);
}
