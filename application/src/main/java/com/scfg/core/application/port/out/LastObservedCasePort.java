package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;

import java.util.List;

public interface LastObservedCasePort {


    PersistenceResponse registerLastObservedCasesRegulatedPolicy(List<LastObservedCaseDhlDTO> lastObservedCaseDhlDTOS, long overwrite);
    PersistenceResponse registerLastObservedCasesnotRegulatedPolicy(List<LastObservedCaseDhnDTO> lastObservedCaseDhnDTOS, long overwrite);


    List<LastObservedCaseDhlDTO> getLastObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<LastObservedCaseDhnDTO> getLastObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
