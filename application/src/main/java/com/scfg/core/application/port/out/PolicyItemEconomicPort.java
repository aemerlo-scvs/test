package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.PolicyItemEconomic;

public interface PolicyItemEconomicPort {
    PolicyItemEconomic findLastByPolicyItemIdAndMovementTypeIdc(Long policyItemId, Integer movementTypeIdc);
    PolicyItemEconomic findLastByPolicyItemIdAndMovementTypeIdc(Long policyItemId, Integer movementTypeIdc, Integer status);
    PolicyItemEconomic saveOrUpdate(PolicyItemEconomic policyItemEconomic);
}
