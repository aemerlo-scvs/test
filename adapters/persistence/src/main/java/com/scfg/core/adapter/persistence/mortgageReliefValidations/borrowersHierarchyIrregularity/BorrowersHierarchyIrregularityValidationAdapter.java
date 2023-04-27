package com.scfg.core.adapter.persistence.mortgageReliefValidations.borrowersHierarchyIrregularity;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierRepository;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.BorrowersHierarchyIrregularityValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.util.CustomForeach;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class BorrowersHierarchyIrregularityValidationAdapter implements BorrowersHierarchyIrregularityValidationPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;
    private final ClassifierRepository classifierRepository;

    /*// Get from db
    private List<ClassifierEnum> borrowerTypes = new ArrayList<>(
            Arrays.asList(ClassifierEnum.Holder_BorrowerType,
                    ClassifierEnum.CosignerOne_BorrowerType,
                    ClassifierEnum.CosignerTwo_BorrowerType,
                    ClassifierEnum.CosignerThree_BorrowerType,
                    ClassifierEnum.CosignerFour_BorrowerType)
    );*/

    private boolean validateHierarchyInProcess = false;
    private boolean validateResult = true;

    @Override
    public ValidationResponseDTO validateIrregularityInHierarchyDHL(Long creditOperationNumber, Long insurancePolicyHolder) {

        validateHierarchyInProcess = false;
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        List<ClassifierJpaEntity> borrrowersType = classifierRepository
                .findAllByClassifierType_ReferenceIdOrderByOrderDesc(ClassifierTypeEnum.BorrowerType.getReferenceId());

        List<MonthlyDisbursementJpaEntity> monthlyDisbursements = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        creditOperationNumber,
                        "",
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType());

        List<String> borrowerCoverageMonthlyDisbursements = monthlyDisbursements.stream()
                .map(monthlyDisbursement -> monthlyDisbursement.getBorrowCoverage().toUpperCase())
                .collect(Collectors.toList());


        CustomForeach.forEach(borrrowersType.stream(), (borrowerType, breaker) -> {
            int index = borrowerCoverageMonthlyDisbursements.indexOf(borrowerType.getDescription()
                    .toUpperCase());
            if (index >= 0 && !validateHierarchyInProcess) {
                validateHierarchyInProcess = true;
            } else { // element not found
                if (validateHierarchyInProcess) {
                    validateResult = false;
                    breaker.stop();
                }
            }
        });
        if (!validateResult) {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(VR_IRREGULARITY_IN_HIERARCHY_BORROWERS);
        }
        return validationResponseDTO;

    }

    @Override
    public ValidationResponseDTO validateIrregularityInHierarchyDHN(Long creditOperationNumber, Long insurancePolicyHolder) {
        validateHierarchyInProcess = false;
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        List<ClassifierJpaEntity> borrowersType = classifierRepository
                .findAllByClassifierType_ReferenceIdOrderByOrderDesc(ClassifierTypeEnum.BorrowerType.getReferenceId());

        List<MonthlyDisbursementJpaEntity> monthlyDisbursements = monthlyDisbursementRepository
                .findAllByCreditOperation_OperationNumberAndClient_DocumentNumber(
                        creditOperationNumber,
                        "",
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType());

        List<String> borrowerCoverageMonthlyDisbursements = monthlyDisbursements.stream()
                .map(monthlyDisbursement -> monthlyDisbursement.getBorrowCoverage().toUpperCase())
                .collect(Collectors.toList());


        CustomForeach.forEach(borrowersType.stream(), (borrowerType, breaker) -> {
            int index = borrowerCoverageMonthlyDisbursements.indexOf(borrowerType.getDescription()
                    .toUpperCase());
            if (index >= 0 && !validateHierarchyInProcess) {
                validateHierarchyInProcess = true;
            } else { // element not found
                if (validateHierarchyInProcess) {
                    validateResult = false;
                    breaker.stop();
                }
            }
        });
        if (!validateResult) {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(VR_IRREGULARITY_IN_HIERARCHY_BORROWERS);
        }
        return validationResponseDTO;
    }

}
