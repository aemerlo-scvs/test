package com.scfg.core.adapter.persistence.coverage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CoverageRepository extends JpaRepository<CoverageJpaEntity, Long> {

    default String getFindAllCoverageNameByGeneralRequestIdQuery(long requestId, int status) {
        return "SELECT IIF(COUNT(pi2.id) = 3, 'COBERTURA TOTAL', STRING_AGG(cv.name, ' | ')) AS coverages \n" +
                "FROM CoveragePolicyItem cpi \n" +
                "JOIN CoverageProductPlan cpl ON cpl.id = cpi.coverageProductPlanId \n" +
                "JOIN CoverageProduct cp ON cp.id = cpl.coverageProductId \n" +
                "JOIN Coverage cv ON cv.id = cp.coverageId  \n" +
                "JOIN PolicyItem pi2 ON pi2.id = cpi.policyItemId \n" +
                "WHERE pi2.generalRequestId = " + requestId + " AND cpi.status = " + status + " AND cpl.status = " + status + " AND \n" +
                "cp.status = " + status + " AND cv.status = " + status + " AND pi2.status = " + status + " \n" +
                "GROUP BY pi2.id";
    }
    @Query("select v from CoverageJpaEntity v " +
            "where v.productId= :productId")
    List<CoverageJpaEntity> findAllCoverageByProductId(@Param("productId")Long productId);
}
