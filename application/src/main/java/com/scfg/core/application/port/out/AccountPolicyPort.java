package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.vin.AccountPolicy;

public interface AccountPolicyPort {

    AccountPolicy findByPolicyId(Long policyId);
    AccountPolicy findByAccountIdAndPolicyId(Long accountId, Long policyId);
    AccountPolicy saveOrUpdate(AccountPolicy accountPolicy);

}
