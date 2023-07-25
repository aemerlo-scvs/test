package com.scfg.core.adapter.persistence.coverageProductPlan;

import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.CoverageClf;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoverageProductPlanRepository extends JpaRepository<CoverageProductPlanJpaEntity, Long> {
    @Query(value = "SELECT  cp " +
            "FROM PlanJpaEntity pl " +
            "inner join CoverageProductPlanJpaEntity cp on pl.id=cp.planId " +
            "inner join CoverageProductJpaEntity cop on cp.coverageProductId=cop.id " +
            "inner join ProductJpaEntity pr on cop.productId=pr.id " +
            "where pr.id= :productId and pl.id= :planId")
    CoverageProductPlanJpaEntity findCoverageProductPlanByPlanIdAndProductId(@Param("productId") Long productId, @Param("planId") Long planId);


    @Query("SELECT new com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO(c.id, c.coverageTypeIdc, c.name, cpi.id, cpp.id, cpp.coverageProductId, cpp.parentCoverageProductId, cpi.policyItemId, " +
            "cpi.insuredCapital, cpp.insuredCapitalCoverage, cpi.additionalPremiumPerPercentage, cpi.additionalPremiumPerThousand, " +
            "cpi.comment, cpi.status, false, cpi.createdAt, cpi.lastModifiedAt, cpp.order, cpp.rate) \n" +
            "FROM CoverageProductJpaEntity cp \n" +
            "INNER JOIN CoverageJpaEntity c ON c.id = cp.coverageId \n" +
            "INNER JOIN ProductJpaEntity p ON p.id = cp.productId \n" +
            "INNER JOIN CoverageProductPlanJpaEntity cpp ON cpp.coverageProductId = cp.id \n" +
            "INNER JOIN PlanJpaEntity pl ON pl.id = cpp.planId \n" +
            "LEFT JOIN CoveragePolicyItemJpaEntity cpi ON cpp.id = cpi.coverageProductPlanId AND cpi.policyItemId = :policyItemId \n" +
            "WHERE c.status = 1 AND cp.status = 1 AND cpp.status = 1 AND pl.status = 1 AND p.apsCode = :apsCode AND pl.id = :planId")
    List<ClfProductPlanCoverageDTO> findAllCoverageProductPlanByProductIdAndPlanIdAndPolicyItemId(@Param("apsCode") String apsCode, @Param("planId") Long planId, @Param("policyItemId") Long policyItemId);


    @Query("SELECT new com.scfg.core.domain.dto.credicasas.CoverageClf(cv.name, cv.code, cv.coverageTypeIdc, cpl.id, cpl.insuredCapitalCoverage, cpl.coverageProductId, cpl.parentCoverageProductId, " +
            "cpl.planId, cpl.minimumEntryAge, cpl.entryAgeLimit, cpl.ageLimitStay, cpl.rate, cpl.order) \n" +
            "FROM CoverageProductPlanJpaEntity cpl \n" +
            "JOIN CoverageProductJpaEntity cp ON cp.id = cpl.coverageProductId \n" +
            "JOIN CoverageJpaEntity cv on cv.id = cp.coverageId \n" +
            "JOIN PlanJpaEntity pl ON pl.id = cpl.planId \n" +
            "JOIN GeneralRequestJpaEntity g on g.planId = pl.id \n" +
            "WHERE g.id = :requestId and cpl.status = :status and cp.status = :status and cv.status = :status and pl.status = :status \n" +
            "ORDER BY cpl.order ASC")
    List<CoverageClf> findAllByGeneralRequestClf(@Param("requestId") long requestId, @Param("status") int status);

    @Query("SELECT new com.scfg.core.domain.dto.CoverageDTO(cv.id, cv.coverageTypeIdc, cv.name, \n" +
            "cpl.coverageProductId, cpl.parentCoverageProductId, cpl.id, \n" +
            "cpl.insuredCapitalCoverage, cpl.rate, cpl.ageLimitStay, cpl.entryAgeLimit, \n" +
            "cpl.minimumEntryAge, cpl.order) \n" +
            "FROM CoverageJpaEntity cv \n" +
            "JOIN CoverageProductJpaEntity cp on cp.coverageId = cv.id \n" +
            "JOIN CoverageProductPlanJpaEntity cpl on cpl.coverageProductId = cp.id \n" +
            "JOIN PlanJpaEntity pl on pl.id = cpl.planId \n" +
            "where pl.bfsAgreementCode = :agreementCode AND pl.status = :status AND cv.status = :status \n" +
            "AND cpl.status = :status AND cp.status = :status")
    List<CoverageDTO> findAllByAgreementCodePlan(@Param("agreementCode") Integer agreementCode,
                                                 @Param("status") Integer status);

    @Query("SELECT new com.scfg.core.domain.dto.CoverageDTO(cv.id, cv.coverageTypeIdc, cv.name, \n" +
            "cpl.coverageProductId, cpl.parentCoverageProductId, cpl.id, \n" +
            "cpl.insuredCapitalCoverage, cpl.rate, cpl.ageLimitStay, cpl.entryAgeLimit, \n" +
            "cpl.minimumEntryAge, cpl.order) \n" +
            "FROM CoverageJpaEntity cv \n" +
            "JOIN CoverageProductJpaEntity cp on cp.coverageId = cv.id \n" +
            "JOIN CoverageProductPlanJpaEntity cpl on cpl.coverageProductId = cp.id \n" +
            "JOIN PlanJpaEntity pl on pl.id = cpl.planId \n" +
            "JOIN CoveragePolicyItemJpaEntity cvpl on cvpl.coverageProductPlanId = cpl.id \n" +
            "WHERE cvpl.policyItemId = :policyItemId AND pl.status = :status AND cv.status = :status \n" +
            "AND cpl.status = :status AND cp.status = :status")
    List<CoverageDTO> findAllByPolicyItemId(@Param("policyItemId") Long policyItemId,
                                                 @Param("status") Integer status);
}
