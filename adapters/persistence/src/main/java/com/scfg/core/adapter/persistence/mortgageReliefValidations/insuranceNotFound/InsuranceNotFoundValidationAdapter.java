package com.scfg.core.adapter.persistence.mortgageReliefValidations.insuranceNotFound;

import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateRepository;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionReport.SubscriptionReportRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.InsuranceNotFoundValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class InsuranceNotFoundValidationAdapter implements InsuranceNotFoundValidationPort {

    private final SubscriptionReportRepository subscriptionReportRepository;
    private final ManualCertificateRepository manualCertificateRepository;

    @Override
    public ValidationResponseDTO insuranceNotFoundDHL(Long operationNumber, String ci, Long insurancePolicyHolder) {
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

        if (subscriptionReportJpaEntityList.size() <= 0) {
            if (manualCertificateJpaEntityList.size() <= 0) {
                return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
            }
        }
        return new ValidationResponseDTO(true, null);
    }

    @Override
    public ValidationResponseDTO insuranceNotFoundDHN(Long operationNumber, String ci, Long insurancePolicyHolder) {
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

        if (subscriptionReportJpaEntityList.size() <= 0) {
            if (manualCertificateJpaEntityList.size() <= 0) {
                return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
            }
        }
        return new ValidationResponseDTO(true, null);
    }
}
