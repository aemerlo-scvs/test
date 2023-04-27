package com.scfg.core.adapter.persistence.brokerSettlementCalculations;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrokerSettlementCalculationsRepository extends JpaRepository<BrokerSettlementCalculationsJpaEntity, Long> {


    // agregar clausulas SQL para la seleccion del reporte por defecto
    @Query("SELECT b " +
            "FROM BrokerSettlementCalculationsJpaEntity b " +
            "JOIN FETCH b.mortgageReliefItem mri "+
            "JOIN FETCH b.insuredCoverage cov "+
            "JOIN FETCH mri.user "+
            "WHERE b.mortgageReliefItem.activeRecord = 1 " +
            "AND b.mortgageReliefItem.loadMonth.id = :monthId AND b.mortgageReliefItem.loadYear.id = :yearId " +
            "AND b.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND b.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND b.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND b.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND b.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<BrokerSettlementCalculationsJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);



}
