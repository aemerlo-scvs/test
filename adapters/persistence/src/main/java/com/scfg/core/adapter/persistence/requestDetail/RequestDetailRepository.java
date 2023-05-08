package com.scfg.core.adapter.persistence.requestDetail;

import com.scfg.core.adapter.persistence.generalRequest.GeneralRequestJpaEntity;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestDetailRepository extends JpaRepository<GeneralRequestJpaEntity, Long> {


    @Query("SELECT new com.scfg.core.domain.dto.credicasas.RequestDetailDTO(gr.id, gr.requestNumber,\n" +
            "gr.requestStatusIdc, 'requestStatusDesc', gr.requestDate, gr.insuredTypeIdc, gr.creditNumber, gr.creditTerm,\n" +
            "gr.height, gr.weight, np.name, np.lastName, np.motherLastName, np.marriedLastName,\n" +
            "np.identificationNumber, gr.activationCode, np.complement, np.extIdc, 'extDesc', np.maritalStatusIdc,\n" +
            "gr.requestedAmount, gr.currentAmount, gr.accumulatedAmount, 0.0, 0.0, \n" +
            "'organizationName', np.birthDate, false, gr.planId ,pt.id, pt.pronouncementDate, np.workTypeIdc, \n" +
            "gr.acceptanceReasonIdc, gr.rejectedReasonIdc, gr.pendingReason, gr.exclusionComment, gr.rejectedComment, gr.inactiveComment,\n" +
            "gr.createdBy, gr.createdAt, gr.lastModifiedAt, '') \n" +
            "FROM GeneralRequestJpaEntity gr\n" +
            "INNER JOIN PersonJpaEntity p ON p.id = gr.personId\n" +
            "INNER JOIN NaturalPersonJpaEntity np ON np.person.id = p.id\n" +
            "INNER JOIN PolicyItemJpaEntity pt ON pt.generalRequestId = gr.id\n" +
            "WHERE gr.id = :requestId AND gr.status = :status AND pt.status = :status AND p.status = :status AND np.status = :status")
    RequestDetailDTO findRequestDetailById(@Param("requestId") Long requestId, @Param("status") Integer status);

    @Query("select p.name as oganization " +
            "from GeneralRequestJpaEntity gr " +
            "inner join PlanJpaEntity p on p.id = gr.planId " +
            "where gr.id = :requestId ")
    String findPlanNameByRequestId(@Param("requestId") Long requestId);

    @Query("select jp.name as oganization " +
            "from GeneralRequestJpaEntity gr " +
            "inner join PolicyJpaEntity p on p.generalRequestId = gr.id " +
            "join PolicyItemJpaEntity pl on pl.policyId = p.id " +
            "join PersonJpaEntity pr on pr.id = gr.personId " +
            "join JuridicalPersonJpaEntity jp on jp.person.id = pr.id " +
            "where pl.id = :policyItemId ")
    String findJuridicalNameByPolicyItemId(@Param("policyItemId") Long policyItemId);

}
