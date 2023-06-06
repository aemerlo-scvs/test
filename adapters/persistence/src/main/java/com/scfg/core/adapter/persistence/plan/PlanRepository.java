package com.scfg.core.adapter.persistence.plan;

import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
import com.scfg.core.domain.smvs.PlanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<PlanJpaEntity, Long> {

    Optional<PlanJpaEntity> findById(long planId);

    @Query("SELECT DISTINCT p " +
            "FROM PlanJpaEntity p " +
            "INNER JOIN GeneralRequestJpaEntity gr " +
            "ON gr.planId = p.id " +
            "INNER JOIN PersonJpaEntity pe " +
            "ON pe.id = gr.personId " +
            "WHERE pe.assignedGroupIdc = :assignedGroupIdc AND p.status = :status")
    List<PlanJpaEntity> findAllByFinancialGroupId(@Param("assignedGroupIdc") Integer assignedGroupIdc, @Param("status") Integer status);

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.credicasas.ClfPlanDTO(cp.productId, p.id, p.name) \n" +
            "FROM PlanJpaEntity p \n" +
            "INNER JOIN CoverageProductPlanJpaEntity cpp ON cpp.planId  = p.id \n" +
            "INNER JOIN CoverageProductJpaEntity cp ON cp.id = cpp.coverageProductId \n" +
            "WHERE p.status = :status")
    List<ClfPlanDTO> clfFindAll(@Param("status") Integer status);

    @Query("SELECT p FROM PlanJpaEntity p "+
            "WHERE p.productId = :productId")
    List<PlanJpaEntity> findAllByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE PlanJpaEntity p SET p.status = 0 " +
            "WHERE p.productId= :productId")
    void deleteByProductId(@Param("productId")Long productId);

    @Query("SELECT new com.scfg.core.domain.dto.credicasas.PlanInformation(pl.id, pl.name, pl.productId, pr.branchId) " +
            "FROM GeneralRequestJpaEntity r\n " +
            "INNER JOIN PlanJpaEntity pl ON pl.id = r.planId\n " +
            "INNER JOIN ProductJpaEntity pr ON pr.id = pl.productId\n " +
            "WHERE r.id IN (:requestListId) AND r.status = :status AND pl.status = :status AND pr.status = :status")
    List<PlanInformation> findAllByRequestId(@Param("requestListId") List<Long> requestListId, @Param("status") Integer status);

    @Query(value = "SELECT pl FROM PlanJpaEntity pl \n" +
            "INNER JOIN ProductJpaEntity po \n" +
            "ON po.id = pl.productId \n" +
            "WHERE po.agreementCode = :agreementCodeProduct AND pl.bfsAgreementCode = :agreementCodePlan \n" +
            "AND pl.status = :status AND po.status = :status")
    PlanJpaEntity findByAgreementCodePlanAndAgreementCodeProduct(@Param("agreementCodePlan") Integer agreementCodePlan,
                                                                 @Param("agreementCodeProduct") Integer agreementCodeProduct,
                                                                 @Param("status") Integer status);


    @Query("SELECT pl FROM PlanJpaEntity pl \n" +
            "WHERE pl.bfsAgreementCode = :bfsAgreementCode AND pl.status = :status")
    PlanJpaEntity findByAgreementCode(@Param("bfsAgreementCode") Integer bfsAgreementCode, @Param("status") Integer status);
}
