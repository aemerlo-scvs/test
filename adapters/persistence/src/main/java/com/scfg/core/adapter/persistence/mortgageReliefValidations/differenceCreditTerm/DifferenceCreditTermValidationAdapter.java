package com.scfg.core.adapter.persistence.mortgageReliefValidations.differenceCreditTerm;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateRepository;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportRepository;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.DifferenceCreditTermValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class DifferenceCreditTermValidationAdapter implements DifferenceCreditTermValidationPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;
    private final SubscriptionReportRepository subscriptionReportRepository;
    private final SubscriptionTrackingRepository subscriptionTrackingRepository;
    private final ManualCertificateRepository manualCertificateRepository;


    @Override
    public ValidationResponseDTO validateCreditTermInRangeDHL(String clientCi, Long creditOperationNumber, Long insurancePolicyHolder) {

        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        // Monthly Disbursement
        List<MonthlyDisbursementJpaEntity> monthlyDisbursements = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        creditOperationNumber,
                        clientCi,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType());

        // manual subscription
        List<SubscriptionTrackingJpaEntity> subscriptionTracking = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        clientCi,
                        creditOperationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCodeType());

        // manual certificate
        List<ManualCertificateJpaEntity> manualCertificates = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        clientCi,
                        creditOperationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCodeType());

        // validate information of system
        double actualCreditTermDaySubscribed = 0;

        boolean existsInManualSubscription = !subscriptionTracking.isEmpty();
        boolean existsInMonthlyDisbursements = !monthlyDisbursements.isEmpty();
        boolean existsInManualCertificates = !manualCertificates.isEmpty();

        if (existsInMonthlyDisbursements) {

            MonthlyDisbursementJpaEntity currentMonthlyDisbursement = monthlyDisbursements.get(0);
            double creditTermDaysReported = currentMonthlyDisbursement.getCreditTermDays();

            if (existsInManualSubscription) {
                SubscriptionTrackingJpaEntity currentManualSubscription = subscriptionTracking.get(0);
                actualCreditTermDaySubscribed = currentManualSubscription.getCreditTermMonths() * 30;

            } else if (existsInManualCertificates) { // manual certificate
                ManualCertificateJpaEntity currentManualCertificate = manualCertificates.get(0);
                actualCreditTermDaySubscribed = currentManualCertificate.getCrediTermDays();

            } else {
                // not subscribed (aplicate in [InsuranceNotFound]
                validationResponseDTO.setCaseInOrder(false)
                        .setExclusionDescription(VR_INSURANCE_NOT_FOUND);
            }
            if (creditTermDaysReported < actualCreditTermDaySubscribed) {
                validationResponseDTO.setCaseInOrder(false)
                        .setExclusionDescription(VR_DIFFERENT_CREDIT_TERM_CREDIT_OPERATION);
            }
        } else {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS);
        }
        return validationResponseDTO;

    }

    @Override
    public ValidationResponseDTO validateCreditTermInRangeDHN(String clientCi, Long creditOperationNumber, Long insurancePolicyHolder) {
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        // Monthly Disbursment
        List<MonthlyDisbursementJpaEntity> monthlyDisbursements = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        creditOperationNumber,
                        clientCi,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType());

        // manual subscription
        List<SubscriptionTrackingJpaEntity> subscriptionTracking = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        clientCi,
                        creditOperationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCodeType());

        // manual certificate
        List<ManualCertificateJpaEntity> manualCertificates = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        clientCi,
                        creditOperationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCodeType());

        // validate information of system
        double actualCreditTermDaySubscribed = 0;

        boolean existsInManualSubscription = !subscriptionTracking.isEmpty();
        boolean existsInMonthlyDisbursements = !monthlyDisbursements.isEmpty();
        boolean existsInManualCertificates = !manualCertificates.isEmpty();

        if (existsInMonthlyDisbursements) {

            MonthlyDisbursementJpaEntity currentMonthlyDisbursement = monthlyDisbursements.get(0);
            double creditTermDaysReported = currentMonthlyDisbursement.getCreditTermDays();

            if (existsInManualSubscription) {
                SubscriptionTrackingJpaEntity currentManualSubscription = subscriptionTracking.get(0);
                actualCreditTermDaySubscribed = currentManualSubscription.getCreditTermMonths() * 30;

            } else if (existsInManualCertificates) { // manual certificate
                ManualCertificateJpaEntity currentManualCertificate = manualCertificates.get(0);
                actualCreditTermDaySubscribed = currentManualCertificate.getCrediTermDays();

            } else {
                // not subscribed (use in [InsuranceNotFound]
                validationResponseDTO.setCaseInOrder(false)
                        .setExclusionDescription(VR_INSURANCE_NOT_FOUND);
            }
            if (creditTermDaysReported < actualCreditTermDaySubscribed) {
                validationResponseDTO.setCaseInOrder(false)
                        .setExclusionDescription(VR_DIFFERENT_CREDIT_TERM_CREDIT_OPERATION);
            }
        } else {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(CREDIT_OPERATION_NOT_FOUND_IN_MONTHLY_DISBURSEMENTS);
        }
        return validationResponseDTO;
    }
}
