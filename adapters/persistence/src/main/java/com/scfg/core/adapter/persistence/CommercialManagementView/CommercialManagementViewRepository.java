package com.scfg.core.adapter.persistence.CommercialManagementView;

import com.scfg.core.domain.dto.CommercialManagementDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommercialManagementViewRepository extends JpaRepository<CommercialManagementViewJpaEntity, Long> {

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatusIdc = :status " +
            "AND c.managementSubStatusIdc = :subStatus " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByStatusAndSubStatus(@Param("status") Integer status, @Param("subStatus") Integer subStatus);

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatusIdc = :status " +
            "AND c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByStateAndDates(
            @Param("status") Integer status,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );



    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatusIdc = :status " +
            "AND c.managementSubStatusIdc = :subStatus " +
            "AND c.endOfCoverage BETWEEN  :fromDate AND :toDate " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getAllByAllFilters(
            @Param("status") Integer status,
            @Param("subStatus") Integer subStatus,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );

    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatusIdc = :status " +
            "ORDER BY c.endOfCoverage")
            List<CommercialManagementDTO> getAllByStatus(@Param("status") Integer status);
    @Query("SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)" +
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.managementStatusIdc = :status AND c.identificationNumber = :identificationNumber " +
            "ORDER BY c.endOfCoverage")
            List<CommercialManagementDTO> getAllByStatusAndIdentificationNumber(@Param("status") Integer status, @Param("identificationNumber") String identificationNumber);

    @Query(value = "SELECT TOP 1 PERCENT c.*" +
            "FROM vv_virh_commercialManagementView c "+
            "WHERE c.number like :number " +
            "ORDER BY c.endOfCoverage", nativeQuery = true)
    List<CommercialManagementViewJpaEntity> getByPhoneNumber(@Param("number") String number);
    @Query(value = "SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)"+
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.identificationNumber like :identificationNumber " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getByIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Query(value = "SELECT DISTINCT new com.scfg.core.domain.dto.CommercialManagementDTO(c.policyId, c.numberPolicy, c.productName, c.productInitials, " +
            "c.insured, c.identificationNumber, c.policyStatus, c.managementStatus, c.managementSubStatus, c.managementStatusIdc, c.managementSubStatusIdc , c.userName, c.userId, c.coverages, " +
            "c.number, c.email, c.planId, c.planName, c.dateDifference , c.endOfCoverage, c.issuanceDate, c.fromDate, c.code, c.URL, c.commercialManagementId)"+
            "FROM CommercialManagementViewJpaEntity c "+
            "WHERE c.number like :number and c.identificationNumber like :identificationNumber " +
            "ORDER BY c.endOfCoverage")
    List<CommercialManagementDTO> getByPhoneNumberAndIdentificationNumber(@Param("number") String number, @Param("identificationNumber") String identificationNumber);





}
