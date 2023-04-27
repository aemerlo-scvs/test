package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;

import java.util.List;

public interface PastMonthlyDisbursementPort {
    PersistenceResponse savePastMonthlyDisbursementsForRegulatedPolicy(List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite);

    PersistenceResponse savaPastMonthlyDisbursementsForNotRegulatedPolicy(List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhlDTOS, long overwrite);

    List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);


}
