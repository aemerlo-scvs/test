package com.scfg.core.adapter.persistence.mortgageReliefitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MortgageReliefItemRepository extends JpaRepository<MortgageReliefItemJpaEntity, Long> {

    //Optional<MortgageReliefItemJpaEntity> findByLoadYearAndLoadMonthAndAndReportTypeAndPolicyTypeAndInsurancePolicyHolderAndUser(long loadYearId, long loadMonthId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId, long usersId);

    @Procedure(name = "rollbackRelatedEntities")
    void callSpRollbackRelatedEntities(@Param("rollbackCreditOperation") long rollbackCreditOperation,
                                       @Param("rollbackInsuranceRequest") long rollbackInsuranceRequest,
                                       @Param("rollbackClient") long rollbackClient,
                                       @Param("rollbackMortgageReliefItem") long rollbackMortgageReliefItem);


    @Modifying
    @Query("UPDATE MortgageReliefItemJpaEntity " +
            "SET activeRecord = 0 " +
            "WHERE loadMonth.id = :monthId " +
            "AND loadYear.id = :yearId " +
            //"AND user.id = :usersId " +
            "AND reportType.id = :reportTypeId " +
            "AND policyType.id = :policyTypeId " +
            "AND insurancePolicyHolder.id = :insurancePolicyHolderId")
    void disableLastInformation(
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("reportTypeId") long reportTypeId,
            @Param("policyTypeId") long policyTypeId,
            //@Param("usersId") long usersId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId);

    @Modifying
    @Query("DELETE FROM MortgageReliefItemJpaEntity mon " +
            "WHERE mon.loadMonth.id = :monthId " +
            "AND mon.loadYear.id = :yearId "+
            "AND mon.insurancePolicyHolder.id = :insurancePolicyHolderId " +
            "AND mon.policyType.id = :policyTypeId " +
            "AND mon.reportType.id = :reportTypeId ")
    void deleteForFilterCriteria(
            @Param("monthId") long monthId,
            @Param("yearId") long yearId,
            @Param("insurancePolicyHolderId") long insurancePolicyHolderId,
            @Param("reportTypeId") long reportTypeId,
            @Param("policyTypeId") long policyTypeId);

    /*@Modifying
    @Query("DELETE FROM MortgageReliefItemJpaEntity m " +
            "WHERE m.policyType.id = :policyTypeId ")
    void deleteAllByPolicyType(@Param("policyTypeId") long policyTypeId);*/
    void deleteByPolicyType_IdAndReportType_IdAndInsurancePolicyHolder_IdAndLoadMonth_IdAndLoadYear_Id(long policyTypeId, long reportTypeId, long insurancePolicyHolderId, long monthId, long yearId);


    void deleteByPolicyType_IdAndReportType_IdAndAndInsurancePolicyHolder_Id(long policyTypeId, long reportTypeId, long insurancePolicyHolderId);

    List<MortgageReliefItemJpaEntity> findByLoadYear_IdAndLoadMonth_IdAndAndReportType_IdAndPolicyType_IdAndInsurancePolicyHolder_Id(
            long loadYearId, long loadMonthId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId);
}
