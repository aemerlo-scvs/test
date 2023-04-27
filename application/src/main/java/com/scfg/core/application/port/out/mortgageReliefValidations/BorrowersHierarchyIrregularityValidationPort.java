package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface BorrowersHierarchyIrregularityValidationPort {

    ValidationResponseDTO validateIrregularityInHierarchyDHL(Long creditOperationNumber, Long insurancePolicyHolder);
    ValidationResponseDTO validateIrregularityInHierarchyDHN(Long creditOperationNumber, Long insurancePolicyHolder);
}
