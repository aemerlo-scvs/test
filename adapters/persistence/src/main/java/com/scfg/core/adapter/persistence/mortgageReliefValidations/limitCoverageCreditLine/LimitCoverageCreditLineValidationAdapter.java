package com.scfg.core.adapter.persistence.mortgageReliefValidations.limitCoverageCreditLine;

import com.scfg.core.application.port.out.mortgageReliefValidations.LimitCoverageCreditLineValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class LimitCoverageCreditLineValidationAdapter implements LimitCoverageCreditLineValidationPort {

    @Override
    public ValidationResponseDTO validateLimitCoverageForCreditLineDHN(String clientCi, Long creditOperationNumber,
                                                                       List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnList) {
        List<MonthlyDisbursementDhnDTO> list = monthlyDisbursementDhnList.stream()
                .filter(monthlyDisbursementDhnDTO -> monthlyDisbursementDhnDTO.getNRO_DOCUMENTO().equals(clientCi)
                && monthlyDisbursementDhnDTO.getNRO_OPERACION().equals(creditOperationNumber))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            MonthlyDisbursementDhnDTO item = list.get(0);
            if (item.getLINEA_CREDITO().equals("SI") && !item.getTIPO_COBERTURA().equals(CREDIT_CARD)) {
                return new ValidationResponseDTO(false, VR_LIMIT_COVERAGE_FOR_CREDIT_LINE);
            }
            return new ValidationResponseDTO(true, null);
        }
        return new ValidationResponseDTO(false, VR_INSURANCE_NOT_FOUND);
    }
}
