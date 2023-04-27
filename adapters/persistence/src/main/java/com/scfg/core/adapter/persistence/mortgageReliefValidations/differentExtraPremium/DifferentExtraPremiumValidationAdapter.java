package com.scfg.core.adapter.persistence.mortgageReliefValidations.differentExtraPremium;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateRepository;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportRepository;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.DifferentExtraPremiumValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class DifferentExtraPremiumValidationAdapter implements DifferentExtraPremiumValidationPort {

    private final SubscriptionReportRepository subscriptionReportRepository;
    private final SubscriptionTrackingRepository subscriptionTrackingRepository;
    private final ManualCertificateRepository manualCertificateRepository;

    // Not apply
    @Override
    public ValidationResponseDTO differentExtraPremiumDHL(Long operationNumber, String ci, Double extraPremiumRate, Long insurancePolicyHolder) {
        List<SubscriptionReportJpaEntity> subscriptionReportJpaEntityList = subscriptionReportRepository
                .findAllByHdocumentNumberAndDoperationNumber(ci, operationNumber + "");

        List<ManualCertificateJpaEntity> manualCertificateJpaEntityList = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        ci,
                        operationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHL_ReportType.getReferenceCodeType());

        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntityList = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        ci,
                        operationNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHL_ReportType.getReferenceCodeType());

        if (subscriptionReportJpaEntityList.size() > 0) {
            SubscriptionReportJpaEntity subscription = subscriptionReportJpaEntityList.get(0);
            float extraPremiumAux = subscription.getOextraPremiumRate() / 100;
            if (extraPremiumAux == extraPremiumRate.floatValue()) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DIFFERENT_EXTRAPREMIUM_RATE);
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            if (subscriptionTracking.getRateExtrapremium().equals(extraPremiumRate)) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            if (manualCertificate.getRateExtrapremium().equals(extraPremiumRate)) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DIFFERENT_EXTRAPREMIUM_RATE);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }

    @Override
    public ValidationResponseDTO differentExtraPremiumDHN(Long operationNumber, String ci, Double extraPremiumRate, Long insurancePolicyHolder,
                                                          String coverageType) {
        if (coverageType.equals(CREDIT_CARD)) {
            return new ValidationResponseDTO(true, null);
        }
        List<SubscriptionReportJpaEntity> subscriptionReportJpaEntityList = subscriptionReportRepository
                .findAllByHdocumentNumberAndDoperationNumber(ci, operationNumber + "");

        List<ManualCertificateJpaEntity> manualCertificateJpaEntityList = manualCertificateRepository
                .findAllByClient_DocumentNumberAndCreditOperationNumber(
                        ci,
                        operationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.ManualCertificateDHN_ReportType.getReferenceCodeType());

        List<SubscriptionTrackingJpaEntity> subscriptionTrackingJpaEntityList = subscriptionTrackingRepository
                .findAllByClient_DocumentNumberAndOperationNumber(
                        ci,
                        operationNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.SubscriptionTrackingDHN_ReportType.getReferenceCodeType());

        if (subscriptionReportJpaEntityList.size() > 0) {
            SubscriptionReportJpaEntity subscription = subscriptionReportJpaEntityList.get(0);
            float extraPremiumAux = subscription.getOextraPremiumRate() / 100;
            if (extraPremiumAux == extraPremiumRate.floatValue()) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DIFFERENT_EXTRAPREMIUM_RATE);
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            if (subscriptionTracking.getRateExtrapremium().equals(extraPremiumRate)) {
                return new ValidationResponseDTO(true, null);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            if (manualCertificate.getRateExtrapremium().equals(extraPremiumRate)) {
                return new ValidationResponseDTO(true, null);
            }
            return new ValidationResponseDTO(false, VR_DIFFERENT_EXTRAPREMIUM_RATE);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }
}
