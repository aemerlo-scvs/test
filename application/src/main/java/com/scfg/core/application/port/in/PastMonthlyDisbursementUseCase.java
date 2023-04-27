package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;

import java.util.List;

public interface PastMonthlyDisbursementUseCase {

    PersistenceResponse migrateInformationToPastMonthlyDisbursementsForRegulatedPolicy(
            long monthId, long yearId,
            long insurancePolicyHolderId, long reportTypeId,
            long policyTypeId, long usersId, long overwrite);

    PersistenceResponse migrateInformationToPastMonthlyDisbursementsForNotRegulatedPolicy(
            long monthId, long yearId,
            long insurancePolicyHolderId, long reportTypeId,
            long policyTypeId, long usersId,long overwrite);

    List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
