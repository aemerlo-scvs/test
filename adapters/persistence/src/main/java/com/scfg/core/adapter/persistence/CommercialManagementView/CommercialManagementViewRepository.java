package com.scfg.core.adapter.persistence.CommercialManagementView;

import com.scfg.core.adapter.persistence.commecialManagement.CommercialManagementJpaEntity;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.smvs.ContactCenterRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommercialManagementViewRepository extends JpaRepository<CommercialManagementJpaEntity, Long> {

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, " +
            "c.insured, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatus LIKE :status " +
            "AND c.managementSubStatus LIKE :subStatus " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByStatusAndSubStatus(@Param("status") String status, @Param("subStatus") String subStatus);

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, " +
            "c.insured, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, " +
            "c.insured, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatus LIKE :status " +
            "AND c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByStateAndDates(
            @Param("status") String status,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );



    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, " +
            "c.insured, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatus LIKE :status " +
            "AND c.managementSubStatus LIKE :subStatus " +
            "AND c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByAllFilters(
            @Param("status") String status,
            @Param("subStatus") String subStatus,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, " +
            "c.insured, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatus LIKE :status " +
            "ORDER BY c.endOfCoverage")
            List<CommercialManagementDTO> getAllByStatus(@Param("status") String status);



}
