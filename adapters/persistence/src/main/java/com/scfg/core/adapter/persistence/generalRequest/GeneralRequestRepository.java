package com.scfg.core.adapter.persistence.generalRequest;

import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.dto.vin.VinDetailOperationDTO;
import com.scfg.core.domain.smvs.ContactCenterRequestDTO;
import com.scfg.core.domain.smvs.VerifyActivationCodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GeneralRequestRepository extends JpaRepository<GeneralRequestJpaEntity, Long> {

    GeneralRequestJpaEntity findByActivationCode(String activationCode);

    @Query("SELECT p " +
            "FROM GeneralRequestJpaEntity gr " +
            "INNER JOIN PersonJpaEntity p ON p.id = gr.personId " +
            "JOIN FETCH NaturalPersonJpaEntity np ON np.person.id = p.id " +
            "WHERE gr.activationCode = :activationCode AND np.identificationNumber = :identificationNumber")
    PersonJpaEntity findByActivationCodeAndIdentificationNumberDeprecated(@Param("activationCode") String activationCode, @Param("identificationNumber") String identificationNumber);

    @Query("SELECT new com.scfg.core.domain.smvs.VerifyActivationCodeDTO(gr.id, p.id, gr.requestStatusIdc)" +
            "FROM GeneralRequestJpaEntity gr " +
            "INNER JOIN PersonJpaEntity p ON p.id = gr.personId " +
            "JOIN FETCH NaturalPersonJpaEntity np ON np.person.id = p.id " +
            "WHERE gr.activationCode = :activationCode AND np.identificationNumber = :identificationNumber AND gr.status = :status")
    VerifyActivationCodeDTO findByActivationCodeAndIdentificationNumber(@Param("activationCode") String activationCode, @Param("identificationNumber") String identificationNumber, @Param("status") Integer status);

    @Query(value = "SELECT TOP 1 G.* " +
            "from GeneralRequest G " +
            "INNER JOIN Payment P on G.id=P.generalRequestId " +
            "INNER JOIN PaymentPlan PL ON P.id=PL.paymentId " +
            "INNER JOIN [Transaction] T ON PL.id=T.paymentPlanId " +
            "INNER JOIN Receipt R ON T.id=R.transactionId " +
            "WHERE R.id = :id", nativeQuery = true)
    GeneralRequestJpaEntity findGeneralByReceiptById(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) \n" +
            "FROM GeneralRequest gr \n" +
            "WITH (NOLOCK) \n" +
            "WHERE gr.creditNumber = :operationNumber AND gr.planId = ( \n" +
            "SELECT g.planId \n" +
            "FROM GeneralRequest g \n" +
            "WHERE g.id = :requestId \n" +
            ") \n" +
            "AND gr.status = :status AND (gr.requestStatusIdc = :activeRequestStatus OR gr.requestStatusIdc = :pendingRequestStatus)", nativeQuery = true)
    Integer verifyOperationNumber(@Param("operationNumber") String operationNumber, @Param("requestId") Integer requestId, @Param("status") Integer status,
                                  @Param("activeRequestStatus") Integer activeRequestStatus,
                                  @Param("pendingRequestStatus") Integer pendingRequestStatus);

    @Query("SELECT DISTINCT new com.scfg.core.domain.smvs.ContactCenterRequestDTO(np.name, np.lastName, np.motherLastName, np.marriedLastName, gr.activationCode, d.cellPhone, p.telephone, p.email, gr.createdAt, r.salePlace, r.agencyName, r.voucherNumber, gr.requestStatusIdc)" +
            "FROM GeneralRequestJpaEntity gr " +
            "INNER JOIN PlanJpaEntity pl ON pl.id = gr.planId " +
            "INNER JOIN CoverageProductPlanJpaEntity cpp ON cpp.planId = pl.id " +
            "INNER JOIN CoverageProductJpaEntity cp ON cp.id = cpp.coverageProductId " +
            "INNER JOIN ProductJpaEntity pr ON pr.id = cp.productId " +
            "INNER JOIN PersonJpaEntity p ON p.id = gr.personId " +
            "INNER JOIN FETCH NaturalPersonJpaEntity np ON np.person.id = p.id " +
            "INNER JOIN DirectionJpaEntity d on d.personId = p.id " +
            "INNER JOIN PaymentJpaEntity py ON py.generalRequestId = gr.id " +
            "INNER JOIN PaymentPlanJpaEntity pp ON pp.paymentId = py.id " +
            "INNER JOIN TransactionJpaEntity t ON t.paymentPlanId = pp.id " +
            "INNER JOIN ReceiptJpaEntity r ON r.transactionId = t.id " +
            "WHERE gr.requestStatusIdc = :requestStatusIdc and pr.agreementCode = :productAgreementCode")
    List<ContactCenterRequestDTO> customFindAllByRequestStatusIdcAndProductAgreementCode(@Param("requestStatusIdc") Integer requestStatusIdc, @Param("productAgreementCode") Integer productAgreementCode);

    @Query("SELECT gr " +
            "FROM GeneralRequestJpaEntity gr " +
            "INNER JOIN PolicyJpaEntity p ON p.generalRequestId = gr.id " +
            "WHERE gr.personId = :personId and gr.requestStatusIdc = :requestStatusIdc AND p.policyStatusIdc = :policyStatusIdc")
    List<GeneralRequestJpaEntity> customFindAllWithActivePoliciesByPersonId(@Param("personId") Long personId, @Param("requestStatusIdc") Integer requestStatusIdc, @Param("policyStatusIdc") Integer policyStatusIdc);

    List<GeneralRequestJpaEntity> findAllByPersonIdAndRequestStatusIdc(Long personId, Integer requestStatusIdc);

    @Query("SELECT gr " +
            "FROM GeneralRequestJpaEntity gr " +
            "WHERE gr.personId = :personId" +
            "")
    List<GeneralRequestJpaEntity> findAllRequestsByPersonId(@Param("personId") Long personId);

    @Query(value = "SELECT CAST(gr.id AS varchar) AS id, CAST(gr.planId AS varchar) AS planId, gr.requestStatusIdc, \n" +
                    "IIF(oh.id IS NULL, gr.requestedAmount, (SELECT TOP 1 oi.currentAmount FROM OperationItem oi WHERE oi.operationHeaderId = oh.id AND oi.status = :status ORDER BY oi.createdAt DESC)) AS currentAmount, \n" +
                    "gr.exclusionComment \n" +
                    "FROM GeneralRequest gr \n" +
                    "LEFT JOIN PolicyItem po ON po.generalRequestId = gr.id \n" +
                    "LEFT JOIN OperationHeader oh On oh.policyItemId = po.id \n" +
                    "WHERE gr.personId = :personId AND gr.status = :status AND po.status = :status", nativeQuery = true)
    List<Object[]> findAllCurrentAmountRequestsByPersonId(@Param("personId") Long personId, @Param("status") Integer status);

    //#region Dynamic Query

    default String getFindAllByFiltersBaseQuery(String additionalJoins) {
        return "FROM GeneralRequestJpaEntity gr\n" +
                "INNER JOIN PersonJpaEntity p ON p.id = gr.personId\n" +
                "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = p.id\n" +
                "LEFT JOIN PolicyItemJpaEntity pt ON pt.generalRequestId = gr.id\n" +
                "LEFT JOIN ClassifierJpaEntity c1 on c1.referenceId = gr.requestStatusIdc\n" +
                "LEFT JOIN CoverageProductPlanJpaEntity cpp ON cpp.planId = gr.planId\n" +
                "LEFT JOIN CoverageProductJpaEntity cp ON cp.id = cpp.coverageProductId\n" +
                additionalJoins +
                "WHERE " +
                "c1.classifierType.id IN (SELECT ct1.id FROM ClassifierTypeJpaEntity ct1 WHERE ct1.referenceId = " + ClassifierTypeEnum.SMVSInsuranceRequestStatus.getReferenceId() + ") AND\n" +
                "gr.status = " + PersistenceStatusEnum.CREATED_OR_UPDATED.getValue() + " ";
    }
    default String getFindAllByFiltersCountQuery(String additionalJoins) {
        return "SELECT COUNT (DISTINCT gr.id)\n" +
                this.getFindAllByFiltersBaseQuery(additionalJoins);
    }

    default String getFindAllByFiltersSelectQuery(String additionalJoins) {
        return "SELECT DISTINCT new com.scfg.core.domain.dto.credicasas.RequestDetailDTO(gr.id, gr.requestNumber,\n" +
                "gr.requestStatusIdc, c1.description, gr.requestDate, gr.insuredTypeIdc, gr.creditNumber, gr.creditTerm,\n" +
                "gr.height, gr.weight, np.name, np.lastName, np.motherLastName, np.marriedLastName,\n" +
                "np.identificationNumber, gr.activationCode, np.complement, np.extIdc, 'extDesc', np.maritalStatusIdc,\n" +
                "gr.requestedAmount, gr.currentAmount, gr.accumulatedAmount, 0.0, 0.0,\n" +
                "'organizationName', np.birthDate, false, gr.planId, pt.id, pt.pronouncementDate, np.workTypeIdc, \n" +
                "gr.acceptanceReasonIdc, gr.rejectedReasonIdc, gr.pendingReason, gr.exclusionComment, gr.rejectedComment, gr.inactiveComment,\n" +
                "gr.createdBy, gr.createdAt, gr.lastModifiedAt, '') \n" +
                this.getFindAllByFiltersBaseQuery(additionalJoins);
    }

    default String getFindAllSMVSRequestBaseQuery(String additionalWhere) {
        return "FROM GeneralRequestJpaEntity gr\n" +
                "INNER JOIN PersonJpaEntity p ON p.id = gr.personId\n" +
                "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = p.id\n" +
                "WHERE gr.planId =\n" +
                "(\n" +
                "SELECT pl.id\n" +
                "FROM ProductJpaEntity p\n" +
                "LEFT JOIN CoverageProductJpaEntity cp ON cp.productId = p.id\n" +
                "INNER JOIN CoverageProductPlanJpaEntity cpp ON cpp.coverageProductId = cp.id\n" +
                "INNER JOIN PlanJpaEntity pl ON pl.id = cpp.planId\n" +
                "WHERE p.agreementCode = :agreementCode AND p.status = :status AND cp.status = :status AND cpp.status = :status\n" +
                ") AND gr.status = :status AND p.status = :status AND np.status = :status AND\n" +
                "gr.createdAt BETWEEN :startDate and :toDate" + additionalWhere + '\n';
    }

    default String getFindAllSMVSRequestCountQuery(String additionalWhere) {
        return "SELECT COUNT(gr)\n" +
                this.getFindAllSMVSRequestBaseQuery(additionalWhere);
    }

    default String getFindAllSMVSRequestSelectQuery(String additionalWhere) {
        return "SELECT new com.scfg.core.domain.smvs.SMVSRequestDTO( (CASE WHEN np.name IS NOT NULL THEN np.name ELSE '' END\n" +
                "\t\t\t\t+ ' ' + CASE WHEN np.lastName IS NOT NULL THEN np.lastName ELSE '' END\n" +
                "\t\t\t\t+ ' ' + CASE WHEN np.motherLastName IS NOT NULL THEN np.motherLastName ELSE '' END\n" +
                "\t\t\t\t+ ' ' + CASE WHEN np.marriedLastName IS NOT NULL THEN np.marriedLastName ELSE '' END) AS names,\n" +
                "\t\tnp.identificationNumber,\n" +
                "\t\tgr.requestStatusIdc,\n" +
                "\t\tgr.requestDate)\n" +
                this.getFindAllSMVSRequestBaseQuery(additionalWhere) + '\n' +
                "ORDER BY gr.createdAt ASC";
    }

    default String getFindAllClfByFiltersBaseQuery() {
        return "SELECT new com.scfg.core.domain.dto.credicasas.RequestDTO(np.identificationNumber, np.complement, np.name as names, np.lastName as lastname, \n" +
                "np.motherLastName as mothersLastname, np.marriedLastName as marriedLastname, gr.id as requestId, gr.requestNumber, gr.createdAt as requestDate, gr.requestStatusIdc, \n" +
                "gr.acceptanceReasonIdc, gr.rejectedReasonIdc, gr.pendingReason, gr.requestedAmount, gr.currentAmount, gr.accumulatedAmount, pt.pronouncementDate) \n" +
                "FROM GeneralRequestJpaEntity gr \n" +
                "INNER JOIN PersonJpaEntity p ON p.id = gr.personId \n" +
                "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = p.id \n" +
                "INNER JOIN PolicyItemJpaEntity pt ON pt.generalRequestId = gr.id \n" +
                "WHERE ";
    }

    //#endregion

    @Query(value = "select * from GeneralRequest\n" +
            "where planId in (\n" +
            "select planId from GeneralRequest r\n" +
            "where id in (select generalRequestId from Policy where numberPolicy like '%DHN%' and status = :status)\n" +
            ") and status = :status and activationCode is not null and currentAmount is null\n" +
            "order by requestDate asc, requestNumber asc, personId, planId", nativeQuery = true)
    List<GeneralRequestJpaEntity> findAllRequestToChangeData(@Param("status") Integer status);

    @Query("SELECT g FROM GeneralRequestJpaEntity g " +
            "INNER JOIN PlanJpaEntity pl on pl.id = g.planId and pl.name = 'SEP - BFS Plan1' " +
            "WHERE g.requestStatusIdc = :requestStatusIdc and g.status = :status and g.requestDate BETWEEN :fromDate AND :toDate")
    List<GeneralRequestJpaEntity> finAllRequestPendingForMoreThanThirtyDays(@Param("fromDate") LocalDateTime fromDate,
                                                                            @Param("toDate") LocalDateTime toDate,
                                                                            @Param("requestStatusIdc") Integer requestStatusIdc,
                                                                            @Param("status") Integer status);

    @Query("SELECT g FROM GeneralRequestJpaEntity g " +
            "INNER JOIN PlanJpaEntity pl on pl.id = g.planId and pl.name = 'SEP - BFS Plan1' " +
            "WHERE g.requestStatusIdc = :requestStatusIdc and g.status = :status and g.requestDate <= :date")
    List<GeneralRequestJpaEntity> finAllRequestPendingForMoreThanThirtyOneDays(@Param("date") LocalDateTime fromDate,
                                                                            @Param("requestStatusIdc") Integer requestStatusIdc,
                                                                            @Param("status") Integer status);

    @Query(value = "SELECT g FROM GeneralRequestJpaEntity  g " +
                    "WHERE g.planId = :planId AND g.creditNumber = :operationNumber AND g.status = :status " +
                    "ORDER BY g.id DESC")
    List<GeneralRequestJpaEntity> findAllProposalRequestByOperationNumberAndPlanId(@Param("status") Integer status,
                                    @Param("planId") Long planId, @Param("operationNumber") String operationNumber);

    @Query("SELECT g FROM GeneralRequestJpaEntity g " +
            "INNER JOIN PlanJpaEntity pl on pl.id = g.planId and pl.name = :planName " +
            "WHERE g.requestStatusIdc = :requestStatusIdc and g.status = :status and g.requestDate <= :date")
    List<GeneralRequestJpaEntity> finAllRequestProposalPendingToCancel(@Param("date") LocalDateTime fromDate,
                                                                       @Param("requestStatusIdc") Integer requestStatusIdc,
                                                                       @Param("status") Integer status,
                                                                       @Param("planName") String planName);

    @Query("SELECT COUNT (g) FROM GeneralRequestJpaEntity  g " +
            "INNER JOIN PolicyJpaEntity p ON p.generalRequestId = g.id " +
            "INNER JOIN PlanJpaEntity pl ON pl.id = g.planId " +
            "WHERE g.creditNumber = :operationNumber " +
            "AND g.status = :status " +
            "AND pl.status = :status " +
            "AND p.status =:status ")
    List<Integer> findIfExistOperationNumbers(@Param("operationNumber") String operationNumber,
                                              @Param("status") Integer status);

    @Query("SELECT COUNT (g) \n" +
            "FROM GeneralRequestJpaEntity g \n" +
            "INNER JOIN PlanJpaEntity pl ON pl.id = g.planId \n" +
            "INNER JOIN ProductJpaEntity p ON p.id = pl.productId \n" +
            "WHERE g.creditNumber = :operationNumber " +
            "AND pl.agreementCode = :planAgreementCode " +
            "AND p.agreementCode = :productAgreementCode " +
            "AND g.status = :status " +
            "AND pl.status = :status " +
            "AND p.status = :status ")
    Integer existOperationNumber(@Param("operationNumber") String operationNumber,
                                 @Param("productAgreementCode") Integer productAgreementCode,
                                 @Param("planAgreementCode") Integer planAgreementCode,
                                 @Param("status") Integer status);

    @Query("SELECT new com.scfg.core.domain.dto.vin.VinDetailOperationDTO(" +
            "np.identificationNumber, np.complement, np.name, np.lastName, np.motherLastName, np.marriedLastName, " +
            "gr.id, gr.creditNumber, gr.requestNumber, gr.requestStatusIdc , p.id, p.policyStatusIdc, p.numberPolicy, " +
            "p.totalPremium, pr.name,  pl.name, gr.creditTermInYears, pit.id) " +
            "FROM GeneralRequestJpaEntity gr " +
            "INNER JOIN PolicyJpaEntity p ON p.generalRequestId = gr.id " +
            "INNER JOIN PlanJpaEntity pl ON pl.id = gr.planId " +
            "INNER JOIN ProductJpaEntity pr ON pr.id = p.productId " +
            "INNER JOIN PersonJpaEntity  pe ON pe.id = gr.personId " +
            "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = p.id " +
            "INNER JOIN PolicyItemJpaEntity pit ON pit.generalRequestId = gr.id " +
            "WHERE gr.creditNumber =:operationNumber " +
            "AND np.identificationNumber =:identificationNumber " +
            "AND np.documentType =:documentType " +
            "AND (:extIdc >= 0 OR np.extIdc =:extIdc) " +
            "AND (:complement IS NULL OR np.complement =:complement) " +
            "AND gr.status =:status " +
            "AND np.status =:status " +
            "AND pe.status =:status " +
            "AND p.status =:status " +
            "ORDER BY gr.id DESC " )
    List<VinDetailOperationDTO> findAPersonForOperationDetail(
            @Param("documentType") Integer documentType,
            @Param("identificationNumber") String identificationNumber,
            @Param("operationNumber") String operationNumber,
            @Param("extIdc") Integer extIdc,
            @Param("complement") String complement,
            @Param("status") Integer status
    );


    default String getFindAllByIdPlanFiltersSelectQuery(Long planId) {
        return "SELECT gr.id AS generalRequestId, p.id AS personId, np.identificationNumber, np.complement, \n" +
                "np.name, np.lastName, np.motherLastName, np.marriedLastName, gr.creditNumber, \n" +
                "gr.requestStatusIdc, gr.requestDate,\n" +
                "\n" +
                "(SELECT TOP 1 ms.messageTypeIdc\n" +
                "FROM MessageSent ms\n" +
                "WHERE ms.referenceId = gr.id\n" +
                "ORDER BY ms.id DESC) AS msMessageTypeIdc,\n" +
                "\n" +
                "(SELECT TOP 1 ms.[to]\n" +
                "FROM MessageSent ms\n" +
                "WHERE ms.referenceId = gr.id\n" +
                "ORDER BY ms.id DESC) AS msTo,\n" +
                "\n" +
                "(SELECT TOP 1 ms.lastModifiedAt\n" +
                "FROM MessageSent ms\n" +
                "WHERE ms.referenceId = gr.id\n" +
                "ORDER BY ms.id DESC) AS msCreatedAt,\n" +
                "\n" +
                "(SELECT TOP 1 mts.messageTypeIdc\n" +
                "FROM MessageToSend mts\n" +
                "WHERE mts.referenceId = gr.id\n" +
                "ORDER BY mts.id DESC) AS mtsMessageTypeIdc,\n" +
                "\n" +
                "(SELECT TOP 1 mts.[to]\n" +
                "FROM MessageToSend mts\n" +
                "WHERE mts.referenceId = gr.id\n" +
                "ORDER BY mts.id DESC) AS mtsTo,\n" +
                "\n" +
                "(SELECT TOP 1 mts.lastModifiedAt\n" +
                "FROM MessageToSend mts\n" +
                "WHERE mts.referenceId = gr.id\n" +
                "ORDER BY mts.id DESC) AS mtsCreatedAt,\n" +
                "\n" +
                "po.totalPremium, gr.creditTermInYears, p.email \n" +
                this.getFindAllByIdPlanFiltersBaseQuery(planId);
    }

    default String getFindAllByIdPlanFiltersCountQuery(Long planId) {
        return "SELECT COUNT (DISTINCT gr.id)\n" +
                this.getFindAllByIdPlanFiltersBaseQuery(planId);
    }

    default String getFindAllByIdPlanFiltersBaseQuery(Long planId) {
        return "FROM GeneralRequest gr\n" +
                "INNER JOIN Person p ON p.id = gr.personId\n" +
                "INNER JOIN NaturalPerson np ON np.personId = p.id\n" +
                "INNER JOIN Policy po ON po.generalRequestId = gr.id \n" +
                "WHERE " +
                "gr.typeIdc = " + ClassifierEnum.TYPE_REQUEST_PROPOSAL.getReferenceCode() + " AND\n" +
                "gr.planId = " + planId + " AND\n" +
                "gr.status = " + PersistenceStatusEnum.CREATED_OR_UPDATED.getValue() + " ";
    }

}
