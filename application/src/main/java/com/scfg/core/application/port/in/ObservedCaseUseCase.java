package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;

import java.util.List;

public interface ObservedCaseUseCase  {


    PersistenceResponse registerLastObservedCasesRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<LastObservedCaseDhlDTO> lastObservedCaseDhlDTOS,
            long overwrite);

    PersistenceResponse registerLastObservedCasesNotRegulatedPolicy(
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            List<LastObservedCaseDhnDTO> lastObservedCaseDhnDTOS,
            long overwrite);

    
    List<LastObservedCaseDhlDTO> getLastObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<LastObservedCaseDhnDTO> getLastObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
