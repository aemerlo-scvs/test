package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ConsolidatedObservedCaseDhnDTO;

import java.util.List;

public interface ConsolidatedObservedCasePort {


    PersistenceResponse registerConsolidatedObservedCaseForRegulatedPolicy(
            List<ConsolidatedObservedCaseDhlDTO> consolidatedObservedCaseDhlDTOList, long overwrite);
    PersistenceResponse registerConsolidatedObservedCaseForNotRegulatedPolicy(
            List<ConsolidatedObservedCaseDhnDTO> consolidatedObservedCaseDhnDTOList, long overwrite);

    List<ConsolidatedObservedCaseDhlDTO> getConsolidatedObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<ConsolidatedObservedCaseDhnDTO> getConsolidatedObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
