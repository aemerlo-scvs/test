package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ValidateInsuredMortgageReliefUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.util.mortgageRelief.ValidateInsuredsResponse;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.ObservedCase;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;
import com.scfg.core.domain.liquidationMortgageRelief.MonthlyDisbursement;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersMethods.getPeriod;

@UseCase
@RequiredArgsConstructor
public class ValidateInsuredMortgageReliefService implements ValidateInsuredMortgageReliefUseCase {

    private List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS;
    private List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnDTOS;

    private List<MonthlyDisbursementDhlDTO> pastMonthlyDisbursementDhlDTOS;
    private List<MonthlyDisbursementDhnDTO> pastMonthlyDisbursementDhnDTOS;

    private List<ManualCertificateDhlDTO> manualCertificateDhlDTOS;
    private List<ManualCertificateDhnDTO> manualCertificateDhnDTOS;

    private List<LastObservedCaseDhlDTO> lastObservedCaseDhlDTOS;
    private List<LastObservedCaseDhnDTO> lastObservedCaseDhnDTOS;

    private List<SubscriptionReportDTO> subscriptionReportDTOS;

    private List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS;
    private List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS;

    private List<SinisterDhlDTO> sinisterDhlDTOS;
    private List<SinisterDhnDTO> sinisterDhnDTOS;


    private final MonthlyDisbursementPort monthlyDisbursementPort;
    private final PastMonthlyDisbursementPort pastMonthlyDisbursementPort;
    private final ManualCertificatePort manualCertificatePort;
    private final LastObservedCasePort lastObservedCasePort;
    private final SubscriptionReportPort subscriptionReportPort;
    private final SubscriptionTrackingPort subscriptionTrackingPort;
    private final SinisterPort sinisterPort;

    private final ClassifierPort classifierPort;
    private final ClientPort clientPort;

    private final ValidationInsuredMortgageReliefPort validationInsuredMortgageReliefPort;

    // Ports Validations
    private final AgeOutOfRangeValidationPort ageOutOfRangeValidationPort;
    private final BorrowersHierarchyIrregularityValidationPort borrowersHierarchyIrregularityValidationPort;
    private final DifferenceAmountDisbursementValidationPort differenceAmountDisbursementValidationPort;
    private final DifferenceCreditTermValidationPort differenceCreditTermValidationPort;
    private final DifferentExtraPremiumValidationPort differentExtraPremiumValidationPort;
    private final DjsMaximumTimeLimitValidationPort djsMaximumTimeLimitValidationPort;
    private final DuplicateOperationsValidationPort duplicateOperationsValidationPort;
    private final InsuranceNotFoundValidationPort insuranceNotFoundValidationPort;
    private final InsurancePendingApprovalValidationPort insurancePendingApprovalValidationPort;
    private final InsuranceRejectedValidationPort insuranceRejectedValidationPort;
    private final LimitCoverageCreditLineValidationPort limitCoverageCreditLineValidationPort;
    private final NewDisbursementsReportedOutPeriodValidationPort newDisbursementsReportedOutPeriodValidationPort;
    private final ReportedGuarantorsValidationPort reportedGuarantorsValidationPort;
    private final ReportedSinisterValidationPort reportedSinisterValidationPort;
    private final CumulusIncreaseValidationPort cumulusIncreaseValidationPort;
    private final ZeroAmountValidationPort zeroAmountValidationPort;

    // Observed case
    private final ObservedCasePort observedCasePort;
    private final MortgageReliefItemPort mortgageReliefItemPort;

    private Long mortgageReliefItemId = 0l;
    private List<ObservedCase> observedCases = new ArrayList<>();

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ValidateInsuredsResponse validateInsuredsForRegulatedPolicy(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long usersId) {

        ValidateInsuredsResponse validationResult = new ValidateInsuredsResponse();

        Classifier currentMonthCls = classifierPort.getClassifierById(monthId);
        Classifier currentYearCls = classifierPort.getClassifierById(yearId);
        Classifier reportTypeConsolidatedObservedCaseDhl = classifierPort
                .getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType);

        // build in memory mortgage relief item object
        MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                .policyTypeIdc(policyTypeId)
                .monthIdc(monthId)
                .yearIdc(yearId)
                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                .reportTypeIdc(reportTypeConsolidatedObservedCaseDhl.getId())
                .usersId(usersId)
                .build();

        // save mortgage relief item object
        mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        Map<String, Integer> prevPeriod = getPeriod(currentMonthCls.getOrder(), currentYearCls.getOrder(), false);

        Classifier prevMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                ClassifierTypeEnum.Month, prevPeriod.get(KEY_MONTH).intValue());

        Classifier prevYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                ClassifierTypeEnum.Year, prevPeriod.get(KEY_YEAR).intValue());

        // single load all Reports for validation aplicate of insureds
        singleLoadReports(policyTypeReferenceId, monthId, yearId, prevMonthCls.getId(), prevYearCls.getId(), insurancePolicyHolderId);


        // group validation: new disbursement report out period
        ValidationResponseDTO rvNewDisbursementsReportedOutPeriod = newDisbursementsReportedOutPeriodValidationPort.validateDisbursementsReportedInPeriodAllowedDHL(
                currentMonthCls.getOrder(), currentYearCls.getOrder(), monthlyDisbursementDhlDTOS, pastMonthlyDisbursementDhlDTOS
        );


        if (!rvNewDisbursementsReportedOutPeriod.isCaseInOrder()) {
            List<MonthlyDisbursementDhlDTO> newMonthlyDisbursementsObserved = (List<MonthlyDisbursementDhlDTO>) rvNewDisbursementsReportedOutPeriod.getData();


            observedCases = newMonthlyDisbursementsObserved.stream()
                    .map(monthlyDisbursementDhl -> {

                        Double clientAccumulatedAmount = clientPort.getClientByCI(monthlyDisbursementDhl.getNRO_DOCUMENTO())
                                .getAccumulatedAmountDhl();

                        validationResult.increasePreeliminaryObservedCase();

                        return ObservedCase.builder()
                                .accumulated(clientAccumulatedAmount)
                                .currentMonthComments(rvNewDisbursementsReportedOutPeriod.getExclusionDescription())
                                .currentMonthDisbursement(monthlyDisbursementDhl.getMONTO_DESEMBOLSADO())
                                .previousMonthDisbursement(0L)
                                .clientId(monthlyDisbursementDhl.getID_CLIENTE())
                                .creditOperationId(monthlyDisbursementDhl.getID_OPERACION_CREDITICIA())
                                .mortgageReliefItemId(mortgageReliefItemId)
                                .build();
                    })
                    .collect(Collectors.toList());

            // filter observed case for new disbursement reporte out period
            monthlyDisbursementDhlDTOS = monthlyDisbursementDhlDTOS.stream()
                    .filter(monthlyDisbursementDhlDTO -> newMonthlyDisbursementsObserved.stream()
                            .noneMatch(newMonthlyDisbursementObserved -> monthlyDisbursementDhlDTO.equals(newMonthlyDisbursementObserved)))
                    .collect(Collectors.toList());
            //observedCases.clear();

        }

        monthlyDisbursementDhlDTOS.forEach(monthlyDisbursementDhl -> {

            List<ValidationResponseDTO> validationRulesError = new ArrayList<>();
            // client
            String clientDocNumber = monthlyDisbursementDhl.getNRO_DOCUMENTO();
            LocalDate clientBirthdate = monthlyDisbursementDhl.getFECHA_NACIMIENTO();

            // credit operation
            Long creditOperationNumber = monthlyDisbursementDhl.getNRO_OPERACION();
            LocalDate disbursementDate = monthlyDisbursementDhl.getFECHA_DESEMBOLSO();
            String borrowRole = monthlyDisbursementDhl.getASEGURADO();
            Double premiumValue = monthlyDisbursementDhl.getMONTO_PRIMA();
            //Double extraPremiumRate =

            ValidationResponseDTO rvAgeOutOfRange = ageOutOfRangeValidationPort.validateAgeOutOfRange(clientBirthdate, disbursementDate);
            validationRulesError.add(rvAgeOutOfRange);

            ValidationResponseDTO rvBorrowersHierarchyIrregularity = borrowersHierarchyIrregularityValidationPort.validateIrregularityInHierarchyDHL(creditOperationNumber, insurancePolicyHolderId);
            validationRulesError.add(rvBorrowersHierarchyIrregularity);

            ValidationResponseDTO rvDifferenceAmountDisbursement = differenceAmountDisbursementValidationPort.validateEqualityInAmountsDisbursementAndSubscribedDHL(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDifferenceAmountDisbursement);
            //ValidationResponseDTO rvDifferentExtraPremium = differentExtraPremiumValidationPort.differentExtraPremiumDHL(creditOperationNumber, clientDocNumber,);

            ValidationResponseDTO rvDifferenceCreditTerm = differenceCreditTermValidationPort.validateCreditTermInRangeDHL(clientDocNumber, creditOperationNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDifferenceCreditTerm);

            ValidationResponseDTO rvDjsMaximumTimeLimit = djsMaximumTimeLimitValidationPort.djsMaximumTimeLimitDHL(creditOperationNumber, clientDocNumber, disbursementDate, insurancePolicyHolderId);
            validationRulesError.add(rvDjsMaximumTimeLimit);

            ValidationResponseDTO rvDuplicateOperations = duplicateOperationsValidationPort.validateDuplicateOperationsDHL(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDuplicateOperations);

            ValidationResponseDTO rvInsuranceNotFound = insuranceNotFoundValidationPort.insuranceNotFoundDHL(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsuranceNotFound);

            ValidationResponseDTO rvInsurancePendingApproval = insurancePendingApprovalValidationPort.insurancePendingApprovalDHL(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsurancePendingApproval);

            ValidationResponseDTO rvInsuranceRejected = insuranceRejectedValidationPort.insuranceRejectedDHL(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsuranceRejected);

            ValidationResponseDTO rvReportedGuarantors = reportedGuarantorsValidationPort.reportedGuarantors(disbursementDate, borrowRole);
            validationRulesError.add(rvReportedGuarantors);

            ValidationResponseDTO rvReportedSinister = reportedSinisterValidationPort.reportedSinisterDHL(clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvReportedSinister);

            ValidationResponseDTO rvCumulusIncrease = cumulusIncreaseValidationPort.cumulusIncreaseDHL(clientDocNumber, currentMonthCls.getOrder(), currentYearCls.getOrder());
            validationRulesError.add(rvCumulusIncrease);

            ValidationResponseDTO rvZeroAmount = zeroAmountValidationPort.zeroAmount(premiumValue);
            validationRulesError.add(rvZeroAmount);

            // filter validations only observed case
            validationRulesError = validationRulesError.stream()
                    .filter(validationResponseDTO -> !validationResponseDTO.isCaseInOrder())
                    .collect(Collectors.toList());

            Integer monthlyDisbursementCurrentCase = OBSERVED_MONTHLY_DISBURSEMENT;
            if (validationRulesError.isEmpty()) { // case in order

                monthlyDisbursementCurrentCase = IN_ORDER_MONTHLY_DISBURSEMENT;

                validationResult.increaseInsuredInOrder();
            } else { // case for observation
                ValidationResponseDTO firstErrorValidationRule = validationRulesError.stream()
                        .findFirst()
                        .get();

                Double clientAccumulatedAmount = clientPort.getClientByCI(monthlyDisbursementDhl.getNRO_DOCUMENTO())
                        .getAccumulatedAmountDhl();

                ObservedCase observedCase = ObservedCase.builder()
                        .accumulated(clientAccumulatedAmount)
                        .currentMonthComments(firstErrorValidationRule.getExclusionDescription())
                        .currentMonthDisbursement(monthlyDisbursementDhl.getMONTO_DESEMBOLSADO())
                        .previousMonthDisbursement(0L)
                        .clientId(monthlyDisbursementDhl.getID_CLIENTE())
                        .creditOperationId(monthlyDisbursementDhl.getID_OPERACION_CREDITICIA())
                        .mortgageReliefItemId(mortgageReliefItemId)
                        .build();

                observedCases.add(observedCase);

                validationResult.increasePreeliminaryObservedCase();
            }

            // Refresh case status of monthly disbursement ACA HAY ERROR
            monthlyDisbursementPort.updateCaseStatus(MonthlyDisbursement.builder()
                    .clientDocumentNumber(monthlyDisbursementDhl.getNRO_DOCUMENTO())
                    .creditOperationNumber(monthlyDisbursementDhl.getNRO_OPERACION())
                    .caseStatus(monthlyDisbursementCurrentCase)
                    .build());

            // monthlyDisbursementDhl.setCASE_IN_ORDEN(monthlyDisbursementCurrentCase);

        });
        observedCasePort.saveAll(observedCases);

        validationResult.setMessage(MESSAGE_VALIDATIONS_INSUREDS);

        observedCases.clear();

        return validationResult;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ValidateInsuredsResponse validateInsuredsForUnregulatedPolicy(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long usersId) {
        ValidateInsuredsResponse validationResult = new ValidateInsuredsResponse();


        Classifier currentMonthCls = classifierPort.getClassifierById(monthId);
        Classifier currentYearCls = classifierPort.getClassifierById(yearId);
        Classifier reportTypeConsolidatedObservedCaseDhn = classifierPort
                .getClassifierByReferences(ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType);

        // build in memory mortgage relief item object
        MortgageReliefItem mortgageReliefItem = MortgageReliefItem.builder()
                .policyTypeIdc(policyTypeId)
                .monthIdc(monthId)
                .yearIdc(yearId)
                .insurancePolicyHolderIdc(insurancePolicyHolderId)
                .reportTypeIdc(reportTypeConsolidatedObservedCaseDhn.getId())
                .usersId(usersId)
                .build();

        // save mortgage relief item object
        mortgageReliefItemId = mortgageReliefItemPort.save(mortgageReliefItem)
                .getId();

        Map<String, Integer> prevPeriod = getPeriod(currentMonthCls.getOrder(), currentYearCls.getOrder(), false);

        Classifier prevMonthCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                ClassifierTypeEnum.Month, prevPeriod.get(KEY_MONTH).intValue());

        Classifier prevYearCls = classifierPort.getClassifierByReferenceTypeCodeAndOrder(
                ClassifierTypeEnum.Year, prevPeriod.get(KEY_YEAR).intValue());

        // single load all Reports for validation aplicate of insureds
        singleLoadReports(policyTypeReferenceId, monthId, yearId, prevMonthCls.getId(), prevYearCls.getId(), insurancePolicyHolderId);


        // group validation: new disbursement report out period
        ValidationResponseDTO rvNewDisbursementsReportedOutPeriod = newDisbursementsReportedOutPeriodValidationPort.validateDisbursementsReportedInPeriodAllowedDHN(
                currentMonthCls.getOrder(), currentYearCls.getOrder(), monthlyDisbursementDhnDTOS, pastMonthlyDisbursementDhnDTOS
        );


        if (!rvNewDisbursementsReportedOutPeriod.isCaseInOrder()) {
            List<MonthlyDisbursementDhnDTO> newMonthlyDisbursementsObserved = (List<MonthlyDisbursementDhnDTO>) rvNewDisbursementsReportedOutPeriod.getData();


            observedCases = newMonthlyDisbursementsObserved.stream()
                    .map(monthlyDisbursementDhn -> {

                        Double clientAccumulatedAmount = clientPort.getClientByCI(monthlyDisbursementDhn.getNRO_DOCUMENTO())
                                .getAccumulatedAmountDhl();

                        validationResult.increasePreeliminaryObservedCase();

                        return ObservedCase.builder()
                                .accumulated(clientAccumulatedAmount) // TODO: pending assign value
                                .currentMonthComments(rvNewDisbursementsReportedOutPeriod.getExclusionDescription())
                                .currentMonthDisbursement(monthlyDisbursementDhn.getMONTO_DESEMBOLSADO())
                                .previousMonthDisbursement(0L)
                                .clientId(monthlyDisbursementDhn.getID_CLIENTE())
                                .creditOperationId(monthlyDisbursementDhn.getID_OPERACION_CREDITICIA())
                                .mortgageReliefItemId(mortgageReliefItemId)
                                .build();
                    })
                    .collect(Collectors.toList());

            // filter observed case for new disbursement reporte out period
            monthlyDisbursementDhnDTOS = monthlyDisbursementDhnDTOS.stream()
                    .filter(monthlyDisbursementDhlDTO -> newMonthlyDisbursementsObserved.stream()
                            .noneMatch(newMonthlyDisbursementObserved -> monthlyDisbursementDhlDTO.equals(newMonthlyDisbursementObserved)))
                    .collect(Collectors.toList());
            //observedCases.clear();

        }

        monthlyDisbursementDhnDTOS.forEach(monthlyDisbursementDhn -> {

            List<ValidationResponseDTO> validationRulesError = new ArrayList<>();
            // client
            String clientDocNumber = monthlyDisbursementDhn.getNRO_DOCUMENTO();
            LocalDate clientBirthdate = monthlyDisbursementDhn.getFECHA_NACIMIENTO();

            // credit operation
            Long creditOperationNumber = monthlyDisbursementDhn.getNRO_OPERACION();
            LocalDate disbursementDate = monthlyDisbursementDhn.getFECHA_DESEMBOLSO();
            String borrowRole = monthlyDisbursementDhn.getASEGURADO();
            Double premiumValue = monthlyDisbursementDhn.getPRIMA_BS();
            Double extraPremiumRate = monthlyDisbursementDhn.getTASA_EXTRAPRIMA();
            String coverageType = monthlyDisbursementDhn.getTIPO_COBERTURA();

            ValidationResponseDTO rvAgeOutOfRange = ageOutOfRangeValidationPort.validateAgeOutOfRange(clientBirthdate, disbursementDate);
            validationRulesError.add(rvAgeOutOfRange);

            ValidationResponseDTO rvBorrowersHierarchyIrregularity = borrowersHierarchyIrregularityValidationPort.validateIrregularityInHierarchyDHN(creditOperationNumber, insurancePolicyHolderId);
            validationRulesError.add(rvBorrowersHierarchyIrregularity);

            ValidationResponseDTO rvDifferenceAmountDisbursement = differenceAmountDisbursementValidationPort.validateEqualityInAmountsDisbursementAndSubscribedDHN(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDifferenceAmountDisbursement);

            ValidationResponseDTO rvDifferentExtraPremium = differentExtraPremiumValidationPort.differentExtraPremiumDHN(creditOperationNumber, clientDocNumber, extraPremiumRate, insurancePolicyHolderId, coverageType);
            validationRulesError.add(rvDifferentExtraPremium);

            ValidationResponseDTO rvDifferenceCreditTerm = differenceCreditTermValidationPort.validateCreditTermInRangeDHN(clientDocNumber, creditOperationNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDifferenceCreditTerm);

            ValidationResponseDTO rvDjsMaximumTimeLimit = djsMaximumTimeLimitValidationPort.djsMaximumTimeLimitDHN(creditOperationNumber, clientDocNumber, disbursementDate, insurancePolicyHolderId);
            validationRulesError.add(rvDjsMaximumTimeLimit);

            ValidationResponseDTO rvDuplicateOperations = duplicateOperationsValidationPort.validateDuplicateOperationsDHN(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvDuplicateOperations);

            ValidationResponseDTO rvInsuranceNotFound = insuranceNotFoundValidationPort.insuranceNotFoundDHN(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsuranceNotFound);

            ValidationResponseDTO rvInsurancePendingApproval = insurancePendingApprovalValidationPort.insurancePendingApprovalDHN(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsurancePendingApproval);

            ValidationResponseDTO rvInsuranceRejected = insuranceRejectedValidationPort.insuranceRejectedDHN(creditOperationNumber, clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvInsuranceRejected);

            ValidationResponseDTO rvLimitCoverageCreditLine = limitCoverageCreditLineValidationPort.validateLimitCoverageForCreditLineDHN(clientDocNumber, creditOperationNumber, monthlyDisbursementDhnDTOS);
            validationRulesError.add(rvLimitCoverageCreditLine);

            ValidationResponseDTO rvReportedGuarantors = reportedGuarantorsValidationPort.reportedGuarantors(disbursementDate, borrowRole);
            validationRulesError.add(rvReportedGuarantors);

            ValidationResponseDTO rvReportedSinister = reportedSinisterValidationPort.reportedSinisterDHN(clientDocNumber, insurancePolicyHolderId);
            validationRulesError.add(rvReportedSinister);

            ValidationResponseDTO rvCumulusIncrease = cumulusIncreaseValidationPort.cumulusIncreaseDHN(clientDocNumber, currentMonthCls.getOrder(), currentYearCls.getOrder());
            validationRulesError.add(rvCumulusIncrease);

            ValidationResponseDTO rvZeroAmount = zeroAmountValidationPort.zeroAmount(premiumValue);
            validationRulesError.add(rvZeroAmount);

            // filter validations only observed case
            validationRulesError = validationRulesError.stream()
                    .filter(validationResponseDTO -> !validationResponseDTO.isCaseInOrder())
                    .collect(Collectors.toList());

            Integer monthlyDisbursementCurrentCase = OBSERVED_MONTHLY_DISBURSEMENT;
            if (validationRulesError.isEmpty()) { // case in order

                monthlyDisbursementCurrentCase = IN_ORDER_MONTHLY_DISBURSEMENT;

                validationResult.increaseInsuredInOrder();
            } else { // case for observation
                ValidationResponseDTO firstErrorValidationRule = validationRulesError.stream()
                        .findFirst()
                        .get();

                Double clientAccumulatedAmount = clientPort.getClientByCI(monthlyDisbursementDhn.getNRO_DOCUMENTO())
                        .getAccumulatedAmountDhl();

                ObservedCase observedCase = ObservedCase.builder()
                        .accumulated(clientAccumulatedAmount)
                        .currentMonthComments(firstErrorValidationRule.getExclusionDescription())
                        .currentMonthDisbursement(monthlyDisbursementDhn.getMONTO_DESEMBOLSADO())
                        .previousMonthDisbursement(0L)
                        .clientId(monthlyDisbursementDhn.getID_CLIENTE())
                        .creditOperationId(monthlyDisbursementDhn.getID_OPERACION_CREDITICIA())
                        .mortgageReliefItemId(mortgageReliefItemId)
                        .build();

                observedCases.add(observedCase);

                validationResult.increasePreeliminaryObservedCase();
            }

            // Refresh case status of monthly disbursement
            monthlyDisbursementPort.updateCaseStatus(MonthlyDisbursement.builder()
                    .clientDocumentNumber(monthlyDisbursementDhn.getNRO_DOCUMENTO())
                    .creditOperationNumber(monthlyDisbursementDhn.getNRO_OPERACION())
                    .caseStatus(monthlyDisbursementCurrentCase)
                    .build());

            // monthlyDisbursementDhl.setCASE_IN_ORDEN(monthlyDisbursementCurrentCase);

        });
        observedCasePort.saveAll(observedCases);

        validationResult.setMessage(MESSAGE_VALIDATIONS_INSUREDS);

        observedCases.clear();

        return validationResult;
    }


    @Override
    public List<InsuredSummaryDTO> getInsuredsSummary(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId) {
        List<InsuredSummaryDTO> insuredSummaryDTOS = validationInsuredMortgageReliefPort.getInsuredsSummary(policyTypeId, monthId, yearId, insurancePolicyHolderId);
        // update accumulated amomunt for each client
        insuredSummaryDTOS.forEach(insuredSummary -> {
            clientPort.updateAccumulatedAmount(policyTypeReferenceId, insuredSummary.getNRO_DOCUMENTO(), insuredSummary.getMONTO_ACUMULADO());
        });
        return insuredSummaryDTOS;
    }

    @Override
    public boolean existsValidationInsureds(long monthId, long yearId, long insurancePolicyHolderId, long policyTypeId, long policyTypeCodeReference) {
        ClassifierEnum reportTypeSelected = policyTypeCodeReference == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()
                ? ClassifierEnum.ConsolidatedObservedCaseDHL_ReportType
                :ClassifierEnum.ConsolidatedObservedCaseDHN_ReportType;

        Classifier reportTypeConsolidatedObsCases = classifierPort.getClassifierByReferences(reportTypeSelected);

        return !mortgageReliefItemPort.getMortgageReliefItemsByIDs(
                monthId, yearId, reportTypeConsolidatedObsCases.getId(),
                policyTypeId, insurancePolicyHolderId)
                .isEmpty();
    }

    @Override
    public List<MonthlyDisbursementDhlDTO> getInsuredInOrderDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return validationInsuredMortgageReliefPort.getInsuredInOrderDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getInsuredInOrderDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return validationInsuredMortgageReliefPort.getInsuredInOrderDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<String> getInsuredInOrderDHLColumns() {
        List<String> columns = new ArrayList<>();
        columns.addAll(COLUMN_NAMES_INSURED_IN_ORDER_DHL);
        return columns;
    }

    @Override
    public List<String> getInsuredInOrderDHNColumns() {
        List<String> columns = new ArrayList<>();
        columns.addAll(COLUMN_NAMES_INSURED_IN_ORDER_DHN);
        return columns;
    }


    private void singleLoadReports(long policyTypeReferenceId, long monthId, long yearId,
                                   long prevMonthId, long prevYearId, long insurancePolicyHolderId) {
        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode()) {

            monthlyDisbursementDhlDTOS = monthlyDisbursementPort.getMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);


            pastMonthlyDisbursementDhlDTOS = pastMonthlyDisbursementPort.getPastMonthlyDisbursementDHLFiltered(prevMonthId, prevYearId, insurancePolicyHolderId);
            /*manualCertificateDhlDTOS = manualCertificatePort.getManualCertificateDHLFiltered(monthId, yearId, insurancePolicyHolderId);
            lastObservedCaseDhlDTOS = lastObservedCasePort.getLastObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
            subscriptionReportDTOS = subscriptionReportPort.getSubscriptionReportFiltered("LICITADA", 1, 1); // modiffied
            subscriptionTrackingDhlDTOS = subscriptionTrackingPort.getSubscriptionTrackingDHLFiltered(monthId, yearId, insurancePolicyHolderId);
            sinisterDhlDTOS = sinisterPort.getSinistersDHLFiltered(monthId, yearId, insurancePolicyHolderId);*/

        } else {

            monthlyDisbursementDhnDTOS = monthlyDisbursementPort.getMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);
            pastMonthlyDisbursementDhnDTOS = pastMonthlyDisbursementPort.getPastMonthlyDisbursementDHNFiltered(prevMonthId, prevYearId, insurancePolicyHolderId);
            /*manualCertificateDhnDTOS = manualCertificatePort.getManualCertificateDHNFiltered(monthId, yearId, insurancePolicyHolderId);
            lastObservedCaseDhnDTOS = lastObservedCasePort.getLastObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
            subscriptionReportDTOS = subscriptionReportPort.getSubscriptionReportFiltered("NO LICITADA", 1, 1); // modiffied
            subscriptionTrackingDhnDTOS = subscriptionTrackingPort.getSubscriptionTrackingDHNFiltered(monthId, yearId, insurancePolicyHolderId);
            sinisterDhnDTOS = sinisterPort.getSinistersDHNFiltered(monthId, yearId, insurancePolicyHolderId);*/

        }
    }


}
