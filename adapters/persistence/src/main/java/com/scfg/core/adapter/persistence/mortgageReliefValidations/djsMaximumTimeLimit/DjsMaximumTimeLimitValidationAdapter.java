package com.scfg.core.adapter.persistence.mortgageReliefValidations.djsMaximumTimeLimit;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateRepository;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportRepository;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.DjsMaximumTimeLimitValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;
import static java.time.temporal.ChronoUnit.DAYS;

@PersistenceAdapter
@RequiredArgsConstructor
public class DjsMaximumTimeLimitValidationAdapter implements DjsMaximumTimeLimitValidationPort {

    private final SubscriptionReportRepository subscriptionReportRepository;
    private final SubscriptionTrackingRepository subscriptionTrackingRepository;
    private final ManualCertificateRepository manualCertificateRepository;

    // TODO: Modify for evaluate Credit Line
    @Override
    public ValidationResponseDTO djsMaximumTimeLimitDHL(Long operationNumber, String documentNumber, LocalDate disbursementDate, Long insurancePolicyHolder) {
        List<SubscriptionReportJpaEntity> subscriptionReportJpaEntityList = subscriptionReportRepository
                .findAllByHdocumentNumberAndDoperationNumber(documentNumber, operationNumber + "");

        List<ManualCertificateJpaEntity> manualCertificateJpaEntityList = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        documentNumber,
                        operationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCodeType());

        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntityList = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        documentNumber,
                        operationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCodeType());

        if (subscriptionReportJpaEntityList.size() > 0) {
            SubscriptionReportJpaEntity subscription = subscriptionReportJpaEntityList.get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            String date = subscription.getCdjsFillDate();
            LocalDate djsFillDate = LocalDate.parse(date, formatter);
            long days = calculateDaysFromDjs(disbursementDate, djsFillDate);
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            long days = calculateDaysFromDjs(disbursementDate, subscriptionTracking.getInsuranceRequest().getDjsFillDate());
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            long days = calculateDaysFromDjs(disbursementDate, manualCertificate.getInsuranceRequest().getDjsFillDate());
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DJS_APPROVAL_MAXIMUM_TIME);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }

    @Override
    public ValidationResponseDTO djsMaximumTimeLimitDHN(Long operationNumber, String documentNumber, LocalDate disbursementDate, Long insurancePolicyHolder) {
        List<SubscriptionReportJpaEntity> subscriptionReportJpaEntityList = subscriptionReportRepository
                .findAllByHdocumentNumberAndDoperationNumber(documentNumber, operationNumber + "");

        List<ManualCertificateJpaEntity> manualCertificateJpaEntityList = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        documentNumber,
                        operationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCodeType());

        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntityList = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        documentNumber,
                        operationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCodeType());

        if (subscriptionReportJpaEntityList.size() > 0) {
            SubscriptionReportJpaEntity subscription = subscriptionReportJpaEntityList.get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            String date = subscription.getCdjsFillDate();
            LocalDate djsFillDate = LocalDate.parse(date, formatter);
            long days = calculateDaysFromDjs(disbursementDate, djsFillDate);
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            long days = calculateDaysFromDjs(disbursementDate, subscriptionTracking.getInsuranceRequest().getDjsFillDate());
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            long days = calculateDaysFromDjs(disbursementDate, manualCertificate.getInsuranceRequest().getDjsFillDate());
            if (days <= HelpersConstants.DJS_LIMIT_DATE && days != -10) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DJS_APPROVAL_MAXIMUM_TIME);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }

    public long calculateDaysFromDjs(LocalDate disbursementDate, LocalDate djsFillDate) {
        if ((disbursementDate != null) && (djsFillDate != null)) {
            long days = DAYS.between(disbursementDate, djsFillDate);
            return days;
        } else {
            return -10;
        }
    }
}
