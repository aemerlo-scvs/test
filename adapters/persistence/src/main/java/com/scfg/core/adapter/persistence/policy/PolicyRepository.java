package com.scfg.core.adapter.persistence.policy;


import com.scfg.core.adapter.persistence.product.ProductJpaEntity;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<PolicyJpaEntity,Long>{

    @Query(value ="select max(po.correlativeNumber) " +
            "from PlanJpaEntity pl " +
            "inner join CoverageProductPlanJpaEntity cp on pl.id=cp.planId " +
            "inner join CoverageProductJpaEntity cop on cp.coverageProductId=cop.id " +
            "inner join ProductJpaEntity pr on cop.productId=pr.id " +
            "inner join GeneralRequestJpaEntity ge on pl.id=ge.planId " +
            "inner join PolicyJpaEntity po on ge.id=po.generalRequestId " +
            "where pr.id=:id and pl.status=1")//p2.id
    Optional<Object> findTopByNumberPolicy(@Param("id") Long productId);

    @Query(value = "SELECT P FROM PolicyJpaEntity P " +
            "JOIN PolicyItemJpaEntity pi " +
            "ON pi.policyId = P.id " +
            "WHERE pi.id = :policyItemId")
    PolicyJpaEntity findByIdByPolicyItemId (@Param("policyItemId") long policyItemId);

    @Query(value = "SELECT P FROM PolicyJpaEntity P " +
            "JOIN GeneralRequestJpaEntity gr " +
            "ON P.generalRequestId = gr.id " +
            "JOIN PolicyJpaEntity pl " +
            "ON pl.generalRequestId = gr.id " +
            "WHERE gr.planId = :planId AND pl.id = :policyId")
    PolicyJpaEntity findByIdByPlanId (@Param("planId") long planId, @Param("policyId") long policyId);

    @Query(value = "SELECT P FROM PolicyJpaEntity P " +
            "WHERE P.generalRequestId IN (SELECT g.id FROM GeneralRequestJpaEntity g " +
            "WHERE g.personId IN (SELECT per.id FROM PersonJpaEntity per " +
            "WHERE per.id IN (SELECT jp.person.id FROM JuridicalPersonJpaEntity jp " +
            "WHERE jp.businessTypeIdc IN (SELECT cl.referenceId FROM ClassifierJpaEntity cl " +
            "WHERE cl.classifierType.id IN (SELECT clt.id FROM ClassifierTypeJpaEntity clt " +
            "WHERE clt.referenceId = :businessGroupIdc)))))")
    List<PolicyJpaEntity> getAllGELPolicies (@Param("businessGroupIdc") long businessGroupIdc); //toDo Verificar para deprecar

    @Query(value = "SELECT p.id, p.numberPolicy, g.planId FROM Policy p \n" +
            "JOIN GeneralRequest g on g.id = p.generalRequestId \n" +
            "JOIN Person pr on pr.id = g.personId \n" +
            "JOIN JuridicalPerson jp on jp.id = pr.juridicalPersonId \n" +
            "WHERE jp.businessTypeIdc = (SELECT referenceId FROM Classifier \n" +
            "WHERE classifierTypeId = (SELECT id FROM ClassifierType WHERE referenceId = :classifierTyPeIdc) AND referenceId = :businessGroupIdc AND status = :status) \n" +
            "AND g.status = :status AND pr.status = :status AND jp.status = :status AND p.status = :status", nativeQuery = true)
    List<Object[]> getAllGELPolicies (@Param("businessGroupIdc") long businessGroupIdc,
                                          @Param("classifierTyPeIdc") long classifierTypeIdc, @Param("status") int status);

    @Query(value =" SELECT CONCAT(a.mes,'-',a.years) as monthYear"+
            " ,a.paymentDate"+
            " ,a.requestStatus"+
            " ,iif(b.issuanceDate is null,DATEDIFF(HOUR,a.paymentDate,getdate())/24.0,DATEDIFF(HOUR,a.paymentDate,b.issuanceDate)/24.0)  as dayActivation"+
            " ,a.branch_office_id"+
            " ,a.branch_office"+
            " ,a.zones"+
            " ,a.agency_id"+
            " ,a.agencyName"+
            " ,sellerId"+
            " ,sellerName"+
            " ,1 as CantVentas"+
            " ,a.mes"+
            " ,a.years"+
            " ,a.corte_mes"+
            " FROM ( "+
            " select g.personId"+
            " ,g.id"+
            " ,r.paymentDate"+
            " , DATEADD(mm,DATEDIFF(mm,0,paymentDate),4) as corte_mes"+
            " ,MONTH(r.paymentDate) as mes"+
            " ,YEAR(r.paymentDate) as years"+
            " ,r.createdAt"+
            " ,r.sellerId"+
            " ,r.sellerName"+
            " ,iif(s.agency_id is not null,s.agency_id,r.agencyId)as agency_id"+
            " ,UPPER(r.agencyName) as agencyName"+
            " ,iif(s.branch_office_id is null,0,s.branch_office_id) as branch_office_id "+
            " ,iif(s.branch_office is not null,s.branch_office,r.salePlace) as branch_office"+
            " ,s.zones_id"+
            " ,CONCAT(s.zones,' - ',bo.abreviation) as zones"+
            " ,r.voucherNumber"+
            " ,requestStatus.description as requestStatus"+
            " ,r.totalAmount"+
            " from GeneralRequest g inner join Payment pa"+
            " on g.id=pa.generalRequestId"+
            " inner join PaymentPlan ppl on pa.id=ppl.paymentId"+
            " inner join [Transaction] t on ppl.id=t.paymentPlanId"+
            " inner join Receipt r on t.id=r.transactionId"+
            " inner join bankBranchOffice bo on r.salePlace=bo.DESCRIPTION"+
            " inner join ("+
            " select cl.referenceId, cl.description"+
            " 	from ClassifierType ct inner join Classifier cl"+
            " 	on ct.id= cl.classifierTypeId and cl.status=1 and ct.status=1 where ct.referenceId=17"+
            " )requestStatus on g.requestStatusIdc=requestStatus.referenceId"+
            " left join ("+
            "  SELECT a.branchOfficeId as branch_office_id, a.DESCRIPTION as branch_office,c.id as zones_id , c.DESCRIPTION as zones"+
            "  ,b.agencyId as agency_id,b.DESCRIPTION as agency "+
            "  FROM bankBranchOffice	a inner join bankAgency b on a.branchOfficeId=b.branchOfficeId and a.STATUS=1 "+
            " 	inner join bankZones c on b.zonesId=c.id and c.STATUS=1"+
            " )s on r.agencyId=s.AGENCY_ID"+
            " )a left JOIN( "+
            "     select *"+
            "     from Policy"+
            "   where policyStatusIdc<>5 "+
            "   )b on a.id=b.generalRequestId "+
            "  where a.paymentDate between :startDate and :toDate AND requestStatus in ('ACEPTADA','PENDIENTE')"+
            "   order by a.branch_office,a.years,a.mes asc" ,nativeQuery = true)
    List<Object[]>getAsciiVentas(@Param("startDate") Date startDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT p FROM PolicyJpaEntity p \n" +
            "WHERE p.generalRequestId = :requestId AND p.status = :status")
    List<PolicyJpaEntity> findByGeneralRequestId(@Param("requestId") Long requestId, @Param("status") Integer status);

    @Query(value = "SELECT p FROM PolicyJpaEntity p \n" +
            "WHERE p.generalRequestId = :requestId AND p.status = :status ORDER BY p.id DESC")
    List<PolicyJpaEntity> findByGeneralRequestIdReturnOneData(@Param("requestId") Long requestId,
                                                              @Param("status") Integer status);


    default String getFindAllByFiltersSelectQuery(Integer status) {
        return  "SELECT new com.scfg.core.domain.dto.RequestPolicyDetailDto( \n" +
                "p.id, np.identificationNumber, np.name, np.lastName, np.motherLastName, \n" +
                "pr.name, pl.name, p.numberPolicy, p.policyStatusIdc ,p.toDate ,p.fromDate) \n" +
                this.getFindAllByFiltersBaseQuery(status);
    }

    default String getFindAllByFiltersCountQuery(Integer status) {
        return "SELECT COUNT (p) \n" +
                this.getFindAllByFiltersBaseQuery(status);
    }

    default String getFindAllByFiltersBaseQuery(Integer status) {
        return "FROM PolicyJpaEntity p \n" +
                "INNER JOIN GeneralRequestJpaEntity gr on p.generalRequestId = gr.id \n" +
                "INNER JOIN PlanJpaEntity  pl on gr.planId = pl.id \n" +
                "INNER JOIN ProductJpaEntity pr on p.productId = pr.id \n" +
                "INNER JOIN PersonJpaEntity pe on gr.personId = pe.id  \n" +
                "INNER JOIN NaturalPersonJpaEntity np on np.id = pe.naturalPerson.id \n" +
                "WHERE p.status = " + status + " AND p.policyStatusIdc <> 0 AND gr.status = " + status +
                " AND pl.status = " + status + " \n" +
                "AND pr.status = " + status + " AND pe.status = " + status + " AND np.status = " + status + " \n";
    }

    @Query( "SELECT p " +
            "FROM PolicyJpaEntity p " +
            "INNER JOIN  GeneralRequestJpaEntity gr on gr.id = p.generalRequestId " +
            "LEFT JOIN PlanJpaEntity pl on pl.id = gr.planId " +
            "WHERE gr.creditNumber =:operationNumber " +
            "AND pl.agreementCode = :agreementCode " +
            "AND pl.status = :status " +
            "AND p.status =:status " +
            "AND gr.status =:status " +
            "AND p.policyStatusIdc = :policyStatusIdc " +
            "AND gr.requestStatusIdc <> :requestStatusIdc " +
            "ORDER BY gr.id DESC, p.id DESC")
    Optional<PolicyJpaEntity> findByOperationNumber(
            @Param("operationNumber") String operationNumber,
            @Param("agreementCode") Integer agreementCode,
            @Param("policyStatusIdc") Integer policyStatusIdc,
            @Param("requestStatusIdc") Integer requestStatusIdc,
            @Param("status") Integer status
    );

    @Query(value = "SELECT p FROM PolicyJpaEntity p \n" +
            "WHERE p.id = :policyId AND p.status = :status")
    PolicyJpaEntity findByPolicyId(@Param("policyId") Long policyId, @Param("status") Integer status);
    @Query(value = "SELECT p FROM PolicyJpaEntity p \n" +
            "WHERE p.id = :policyId AND p.status = :status")
    Optional<PolicyJpaEntity> findOptionalByPolicyId(@Param("policyId") Long policyId, @Param("status") Integer status);
}
