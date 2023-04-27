package com.scfg.core.application.port.in;

import com.scfg.core.common.util.mortgageRelief.ValidateInsuredsResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;

import java.util.List;

public interface ValidateInsuredMortgageReliefUseCase {

    // TODO: Modified return value from method
    ValidateInsuredsResponse validateInsuredsForRegulatedPolicy(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long usersId);

    ValidateInsuredsResponse validateInsuredsForUnregulatedPolicy(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long usersId);

    List<MonthlyDisbursementDhlDTO> getInsuredInOrderDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<MonthlyDisbursementDhnDTO> getInsuredInOrderDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<String> getInsuredInOrderDHLColumns();

    List<String> getInsuredInOrderDHNColumns();

    List<InsuredSummaryDTO> getInsuredsSummary(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId);

    boolean existsValidationInsureds(long monthId, long yearId, long insurancePolicyHolderId, long policyTypeId, long policyTypeCodeReference);

}
