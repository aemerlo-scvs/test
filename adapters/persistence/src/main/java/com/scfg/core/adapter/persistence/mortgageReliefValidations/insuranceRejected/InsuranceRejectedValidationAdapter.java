package com.scfg.core.adapter.persistence.mortgageReliefValidations.insuranceRejected;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateRepository;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportRepository;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.InsuranceRejectedValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class InsuranceRejectedValidationAdapter implements InsuranceRejectedValidationPort {

    private final SubscriptionReportRepository subscriptionReportRepository;
    private final SubscriptionTrackingRepository subscriptionTrackingRepository;
    private final ManualCertificateRepository manualCertificateRepository;

    @Override
    public ValidationResponseDTO insuranceRejectedDHL(Long operationNumber, String ci, Long insurancePolicyHolder) {
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
            if (subscription.getNsubscriptionStatus().equals("RECHAZADO")) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            if (subscriptionTracking.getInsuranceRequest().getRequestStatus().getId().equals(HelpersConstants.REJECTED)) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            if (manualCertificate.getInsuranceRequest().getRequestStatus().getId().equals(HelpersConstants.REJECTED)) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
            return new ValidationResponseDTO(true, null);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }

    @Override
    public ValidationResponseDTO insuranceRejectedDHN(Long operationNumber, String ci, Long insurancePolicyHolder) {
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
            if (subscription.getNsubscriptionStatus().equals("RECHAZADO")) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
        }
        if (subscriptionTrackingJpaEntityList.size() > 0) {
            SubscriptionTrackingJpaEntity subscriptionTracking = subscriptionTrackingJpaEntityList.get(0);
            if (subscriptionTracking.getInsuranceRequest().getRequestStatus().getId().equals(HelpersConstants.REJECTED)) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
        }
        if (manualCertificateJpaEntityList.size() > 0) {
            ManualCertificateJpaEntity manualCertificate = manualCertificateJpaEntityList.get(0);
            if (manualCertificate.getInsuranceRequest().getRequestStatus().getId().equals(HelpersConstants.REJECTED)) {
                return new ValidationResponseDTO(false, VR_INSURANCE_REJECTED);
            }
            return new ValidationResponseDTO(true, null);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }
}
