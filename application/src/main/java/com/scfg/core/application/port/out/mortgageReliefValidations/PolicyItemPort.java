package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.PolicyItem;

public interface PolicyItemPort {

    PolicyItem saveOrUpdate(PolicyItem policyItem);

    PolicyItem getPolicyItemByGeneralRequestId(Long generalRequestId);

    PolicyItem findById(Long policyItemId);
}
