package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.vin.Account;

import java.util.List;

public interface AccountPort {

    List<Account> findAllByPersonId(Long personId);
    List<Account> findAllByNewPersonId(Long personId);
    Account findLastByPersonId(Long personId);
    Account findByPolicyId(Long policyId);
    Account findLastByPersonIdAndPolicyId(Long personId, Long policyId);
    Account saveOrUpdate(Account account);

    boolean saveOrUpdateAll(List<Account> accountList);
}
