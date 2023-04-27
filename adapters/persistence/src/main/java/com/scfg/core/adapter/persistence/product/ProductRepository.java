package com.scfg.core.adapter.persistence.product;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.domain.common.ObjectDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductJpaEntity, Long> {

    ProductJpaEntity findByAgreementCode(int agreementCode);

    @Query("SELECT pl " +
            "FROM ProductJpaEntity pr " +
            "INNER JOIN CoverageProductJpaEntity cp ON cp.productId = pr.id " +
            "INNER JOIN CoverageProductPlanJpaEntity cpp ON cpp.coverageProductId = cp.id " +
            "INNER JOIN PlanJpaEntity pl ON pl.id = cpp.planId " +
            "WHERE pr.agreementCode = :agreementCode AND pr.status = :status AND pl.status = :status")
    List<PlanJpaEntity> findAllPlansByAgreementCode(@Param("agreementCode") int agreementCode, @Param("status") int status);

    @Query(value = "SELECT top 1 pr.*FROM [Plan] pl " +
            "inner join CoverageProductPlan cp on pl.id=cp.planId " +
            "inner join CoverageProduct cop on cp.coverageProductId=cop.id " +
            "inner join Product pr on cop.productId=pr.id " +
            "where pl.id = :id and pl.status=1", nativeQuery = true)
    ProductJpaEntity findProductByPlanId(@Param("id") Long id);

    @Query("SELECT pr " +
            "FROM ProductJpaEntity pr " +
            "INNER JOIN PolicyJpaEntity p ON p.productId = pr.id " +
            "WHERE p.id = :policyId AND pr.status = :status AND p.status = :status")
    List<ProductJpaEntity> findAllProductsByPolicyId(@Param("policyId") Long policyId, @Param("status") int status);

    @Query(value = "SELECT new com.scfg.core.domain.common.ObjectDTO(p.id, p.name) from ProductJpaEntity p " +
            "JOIN BranchJpaEntity br " +
            "ON br.id = p.branchId " +
            "WHERE p.status = 1 and br.id = :branchId" )
    List<ObjectDTO> getAllProductsByBranchId(@Param("branchId") long branchId);

}
