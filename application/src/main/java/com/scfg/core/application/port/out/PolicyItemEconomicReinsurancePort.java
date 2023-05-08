package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.PolicyItemEconomicReinsurance;

import java.util.List;

public interface PolicyItemEconomicReinsurancePort {
    List<PolicyItemEconomicReinsurance> findAllByPolicyItemEconomicId(Long policyItemEconomicId);

    PolicyItemEconomicReinsurance saveOrUpdate(PolicyItemEconomicReinsurance policyItemEconomicReinsurance);
    void saveOrUpdateAll(List<PolicyItemEconomicReinsurance> policyItemEconomicReinsuranceList);
    void setStatusAllByPolicyItemEconomicId(Integer stauts, Long policyItemEconomicId);
}
