package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ObservedCase;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;

import java.util.List;

public interface ObservedCasePort {


    List<ObservedCase> getAllObservedCases();

    ObservedCase getObservedCaseById(long observedCaseId);

    ObservedCase getObservedCaseByClientIdAndCreditOperationIdAndMortgageReliefItemIds(
            Long clientId, Long creditOperationId,
            List<Long> mortgageReliefItemIds);

    List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    PersistenceResponse save(ObservedCase observedCase);

    PersistenceResponse update(ObservedCase observedCase);

    PersistenceResponse delete(ObservedCase observedCase);

    PersistenceResponse saveAll(List<ObservedCase> observedCases);


}
