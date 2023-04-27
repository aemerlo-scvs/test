package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;

import java.util.List;


public interface ValidationInsuredMortgageReliefPort {

    List<InsuredSummaryDTO> getInsuredsSummary(long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhlDTO> getInsuredInOrderDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getInsuredInOrderDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
