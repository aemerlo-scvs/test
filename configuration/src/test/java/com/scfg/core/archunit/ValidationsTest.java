package com.scfg.core.archunit;

import com.scfg.core.application.port.out.MonthlyDisbursementPort;
import com.scfg.core.application.port.out.ValidationInsuredMortgageReliefPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.*;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ValidationsTest {

    @Autowired
    InsuranceNotFoundValidationPort insuranceNotFoundValidationPort;
    @Autowired
    AgeOutOfRangeValidationPort ageOutOfRangeValidationPort;
    @Autowired
    InsurancePendingApprovalValidationPort insurancePendingApprovalValidationPort;
    @Autowired
    InsuranceRejectedValidationPort insuranceRejectedValidationPort;
    @Autowired
    DifferentExtraPremiumValidationPort differentExtraPremiumValidationPort;
    @Autowired
    DjsMaximumTimeLimitValidationPort djsMaximumTimeLimitValidationPort;
    @Autowired
    DuplicateOperationsValidationPort duplicateOperationsValidationPort;
    @Autowired
    ZeroAmountValidationPort zeroAmountValidationPort;
    @Autowired
    ReportedSinisterValidationPort reportedSinisterValidationPort;
    @Autowired
    ReportedGuarantorsValidationPort reportedGuarantorsValidationPort;
    @Autowired
    BorrowersHierarchyIrregularityValidationPort borrowersHierarchyIrregularityValidationPort;
    @Autowired
    DifferenceAmountDisbursementValidationPort differenceAmountDisbursementValidationPort;
    @Autowired
    DifferenceCreditTermValidationPort differenceCreditTermValidationPort;
    @Autowired
    NewDisbursementsReportedOutPeriodValidationPort newDisbursementsReportedOutPeriodValidationPort;
    @Autowired
    CumulusIncreaseValidationPort cumulusIncreaseValidationPort;
    @Autowired
    MonthlyDisbursementPort monthlyDisbursementPort;

    @Autowired
    ValidationInsuredMortgageReliefPort validationInsuredMortgageReliefPort;

    @Test
    void insuranceNotFound() {
        ValidationResponseDTO validationResponseDTO = insuranceNotFoundValidationPort.insuranceNotFoundDHL(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = insuranceNotFoundValidationPort.insuranceNotFoundDHN(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void ageOutOfRangeTest() {
        LocalDate birthDate = LocalDate.parse("1999-04-01");
        LocalDate disbursementDate = LocalDate.parse("2021-05-01");
        ValidationResponseDTO validationResponseDTO = ageOutOfRangeValidationPort.validateAgeOutOfRange(birthDate, disbursementDate);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void insurancePendingApproval() {
        ValidationResponseDTO validationResponseDTO = insurancePendingApprovalValidationPort.insurancePendingApprovalDHL(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = insurancePendingApprovalValidationPort.insurancePendingApprovalDHN(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void insuranceRejected() {
        ValidationResponseDTO validationResponseDTO = insuranceRejectedValidationPort.insuranceRejectedDHL(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = insuranceRejectedValidationPort.insuranceRejectedDHN(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void differentExtraPremium() {
        ValidationResponseDTO validationResponseDTO = differentExtraPremiumValidationPort.differentExtraPremiumDHL(9133L, "2851214",
                0D, 1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = differentExtraPremiumValidationPort.differentExtraPremiumDHN(9133L, "2851214",
                0D, 1L, "TARJETAS DE CREDITO");
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void djsMaximumTimeLimit() {
        LocalDate date = LocalDate.parse("2021-04-01");
        ValidationResponseDTO validationResponseDTO = djsMaximumTimeLimitValidationPort.djsMaximumTimeLimitDHL(9133L, "2851214",
                date, 1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = djsMaximumTimeLimitValidationPort.djsMaximumTimeLimitDHN(9133L, "2851214",
                date, 1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void duplicateOperations() {
        ValidationResponseDTO validationResponseDTO = duplicateOperationsValidationPort.validateDuplicateOperationsDHL(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = duplicateOperationsValidationPort.validateDuplicateOperationsDHN(9133L, "2851214",
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void zeroAmount() {
        ValidationResponseDTO validationResponseDTO = zeroAmountValidationPort.zeroAmount(1866.99D);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void reportedSinister() {
        ValidationResponseDTO validationResponseDTO = reportedSinisterValidationPort.reportedSinisterDHL("2851214", 1L);
        assertFalse(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = reportedSinisterValidationPort.reportedSinisterDHN("2851214", 1L);
        assertFalse(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void reportedGuarantors() {
        LocalDate date = LocalDate.parse("2021-04-01");
        ValidationResponseDTO validationResponseDTO = reportedGuarantorsValidationPort.reportedGuarantors(date, "DEUDOR");
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = reportedGuarantorsValidationPort.reportedGuarantors(date, "GARANTE");
        assertFalse(validationResponseDTO.isCaseInOrder());
        date = LocalDate.parse("2019-04-01");
        validationResponseDTO = reportedGuarantorsValidationPort.reportedGuarantors(date, "GARANTE");
        assertTrue(validationResponseDTO.isCaseInOrder());
    }
    // MANUEL VALIDATION
    @Test
    void borrowersHierarchyIrregularity() {
        ValidationResponseDTO validationResponseDTO = borrowersHierarchyIrregularityValidationPort.validateIrregularityInHierarchyDHL(9133L,
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = borrowersHierarchyIrregularityValidationPort.validateIrregularityInHierarchyDHN(9133L,
                1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }
    // MANUEL VALIDATION
    @Test
    void differenceInDisbursementAmount() {
        ValidationResponseDTO validationResponseDTO = differenceAmountDisbursementValidationPort.
                validateEqualityInAmountsDisbursementAndSubscribedDHL(9133L, "2851214", 1L);
        assertFalse(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = differenceAmountDisbursementValidationPort.
                validateEqualityInAmountsDisbursementAndSubscribedDHN(9133L, "2851214", 1L);
        assertFalse(validationResponseDTO.isCaseInOrder());
    }
    // MANUEL VALIDATION
    @Test
    void differenceInCreditTerm() {
        ValidationResponseDTO validationResponseDTO = differenceCreditTermValidationPort.
                validateCreditTermInRangeDHL( "2851214", 9133L, 1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
        validationResponseDTO = differenceCreditTermValidationPort.
                validateCreditTermInRangeDHN( "2851214", 9133L, 1L);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }
    // MANUEL VALIDATION TODO
    @Test
    void newDisbursementsReportedOutOfPeriod() {
        List<MonthlyDisbursementDhlDTO> list = monthlyDisbursementPort.getMonthlyDisbursementDHLFiltered(17, 29, 1);
        ValidationResponseDTO validationResponseDTO = newDisbursementsReportedOutPeriodValidationPort.
                validateDisbursementsReportedInPeriodAllowedDHL( 1, 2021, list, list);
        assertFalse(validationResponseDTO.isCaseInOrder());
    }
    // MANUEL VALIDATION TODO
    @Test
    void cumulusIncrease() {
        ValidationResponseDTO validationResponseDTO = cumulusIncreaseValidationPort.
                cumulusIncreaseDHL( "2851214", 1, 2020);
        assertTrue(validationResponseDTO.isCaseInOrder());
    }

    @Test
    void getInsuredsSummary(){
        List<InsuredSummaryDTO> x = validationInsuredMortgageReliefPort.getInsuredsSummary(2,20,29,1);
        assertTrue(!x.isEmpty());
    }


    @Test
    void test() {
        assertEquals(true, true);
    }
}
