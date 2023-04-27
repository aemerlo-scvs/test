package com.scfg.core.adapter.persistence.mortgageReliefValidations.duplicateOperation;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.DuplicateOperationsValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class DuplicateOperationValidationAdapter implements DuplicateOperationsValidationPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;

    @Override
    public ValidationResponseDTO validateDuplicateOperationsDHL(Long operationNumber, String ci, Long insurancePolicyHolder) {
        List<MonthlyDisbursementJpaEntity> validationList = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        operationNumber,
                        ci,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType());

        if (validationList.size() > 1) {
            return new ValidationResponseDTO(false, VR_DUPLICATE_CREDIT_OPERATIONS);
        }
        if (validationList.size() <= 0) {
            return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
        }
        return new ValidationResponseDTO(true, null);
    }

    @Override
    public ValidationResponseDTO validateDuplicateOperationsDHN(Long operationNumber, String ci, Long insurancePolicyHolder) {
        List<MonthlyDisbursementJpaEntity> validationList = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        operationNumber,
                        ci,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType());

        if (validationList.size() > 1) {
            return new ValidationResponseDTO(false, VR_DUPLICATE_CREDIT_OPERATIONS);
        }
        if (validationList.size() <= 0) {
            return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
        }
        return new ValidationResponseDTO(true, null);
    }
}
