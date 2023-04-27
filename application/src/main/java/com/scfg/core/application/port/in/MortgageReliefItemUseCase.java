package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;

import java.util.List;

public interface MortgageReliefItemUseCase {

    List<MortgageReliefItem> getMortgageReliefItemByIDs(
            long monthId, long yearId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId);
}
