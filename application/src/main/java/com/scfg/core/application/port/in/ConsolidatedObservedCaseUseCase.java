package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ConsolidatedObservedCaseUseCase  {
    PersistenceResponse saveConsolidatedObservedCaseForRegulatedPolicy(
            long monthId,
            long  yearId,
            long insurancePolicyHolderId,
            long usersId,
            long policyTypeId,
            long reportTypeId,
            List<ConsolidatedObservedCaseDhlDTO> consolidatedObservedCaseDhlDTOList,
            long overwrite);

    PersistenceResponse saveConsolidatedObservedCaseForNotRegulatedPolicy(
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long usersId,
            long policyTypeId,
            long reportTypeId,
            List<ConsolidatedObservedCaseDhnDTO> consolidatedObservedCaseDhnDTOList,
            long overwrite);

    List<ConsolidatedObservedCaseDhlDTO> getConsolidatedObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<ConsolidatedObservedCaseDhnDTO> getConsolidatedObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
