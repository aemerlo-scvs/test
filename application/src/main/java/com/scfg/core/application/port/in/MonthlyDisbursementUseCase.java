package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;

import java.util.List;
import java.util.Map;

public interface MonthlyDisbursementUseCase {
    PersistenceResponse saveMonthlyDisbursementsForRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite);

    PersistenceResponse saveMonthlyDisbursementsForNotRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhlDTOS, long overwrite);

    List<MonthlyDisbursementDhlDTO> getMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
