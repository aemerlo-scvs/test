package com.scfg.core.application.port.in;


import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;

import java.util.List;

public interface PreliminaryObservedCaseUseCase {

    List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<String> getPreliminaryObservedCasesColumns();
}
