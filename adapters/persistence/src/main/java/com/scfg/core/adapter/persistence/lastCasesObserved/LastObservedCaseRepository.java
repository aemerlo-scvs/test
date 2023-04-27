package com.scfg.core.adapter.persistence.lastCasesObserved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LastObservedCaseRepository extends JpaRepository<LastObservedCaseJpaEntity, Long> {


    @Query("SELECT loc " +
            "FROM LastObservedCaseJpaEntity loc " +
            "JOIN FETCH loc.observationType "+
            "JOIN FETCH loc.creditOperation ope "+
            "JOIN FETCH ope.insuranceRequest req  "+
            "JOIN FETCH loc.client cl "+
            "JOIN FETCH loc.mortgageReliefItem mri "+
            "JOIN FETCH mri.user "+ // Sin uso
            "WHERE loc.mortgageReliefItem.activeRecord = 1 " +
            "AND loc.mortgageReliefItem.loadMonth.id = :monthId AND loc.mortgageReliefItem.loadYear.id = :yearId " +
            "AND loc.mortgageReliefItem.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND loc.mortgageReliefItem.policyType.referenceId = :policyReferenceCode AND loc.mortgageReliefItem.policyType.classifierType.referenceId = :policyTypeReferenceCode " +
            "AND loc.mortgageReliefItem.reportType.referenceId = :reportReferenceCode AND loc.mortgageReliefItem.reportType.classifierType.referenceId = :reportTypeReferenceCode")
    List<LastObservedCaseJpaEntity> findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
            @Param("policyReferenceCode") long policyReferenceCode,
            @Param("policyTypeReferenceCode") long policyTypeReferenceCode,
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportReferenceCode") long reportReferenceCode,
            @Param("reportTypeReferenceCode") long reportTypeReferenceCode);

    //List<ClasificadorJpaEntity> findAll();
    // Provisional
    //ClassifierJpaEntity findById(long classifierId);

    //ClassifierJpaEntity findById(long Id);
    //List<ClassifierJpaEntity> findAllByClassifierTypeId

}
