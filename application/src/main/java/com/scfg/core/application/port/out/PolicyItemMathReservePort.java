package com.scfg.core.application.port.out;

import com.scfg.core.domain.PolicyItemMathReserve;

import java.util.List;

public interface PolicyItemMathReservePort {
    PolicyItemMathReserve saveOrUpdate(PolicyItemMathReserve policyItemMathReserve);
    List<PolicyItemMathReserve> saveOrUpdateAll(List<PolicyItemMathReserve> policyItemMathReserveList);
    List<PolicyItemMathReserve> findByPolicyItemId(Long policyItemId);
    PolicyItemMathReserve findByPolicyItemIdAndYear(Long policyItemId, Integer year);
}
