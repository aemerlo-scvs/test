package com.scfg.core.adapter.persistence.account;

import com.scfg.core.application.port.out.AccountPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.dto.vin.Account;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPort {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAllByPersonId(Long personId) {
        List<AccountJpaEntity> list = accountRepository.findAllByPersonId(personId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, Account.class))
                .collect(Collectors.toList());
    }

    @Override
    public Account findLastByPersonId(Long personId) {
        List<AccountJpaEntity> list = accountRepository.findAllByPersonId(personId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        if (list.isEmpty()) {
            return null;
        }

        return mapToDomain(list.get(0));
    }

    @Override
    public Account findByPolicyId(Long policyId) {
        AccountJpaEntity account = accountRepository.findByPolicyId(policyId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(account);
    }

    @Override
    public Account findLastByPersonIdAndPolicyId(Long personId, Long policyId) {
        List<AccountJpaEntity> list = accountRepository.findAllByPersonIdAndPolicyId(personId, policyId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        if (list.isEmpty()) {
            return null;
        }

        return mapToDomain(list.get(0));
    }

    @Override
    public Account saveOrUpdate(Account account) {
        AccountJpaEntity accountJpa = mapToJpaEntity(account);
        accountJpa = accountRepository.save(accountJpa);
        return mapToDomain(accountJpa);
    }

    //#region Mappers

    private Account mapToDomain(AccountJpaEntity accountJpa) {
        return new ModelMapperConfig().getStrictModelMapper().map(accountJpa, Account.class);
    }

    private AccountJpaEntity mapToJpaEntity(Account account) {
        return new ModelMapperConfig().getStrictModelMapper().map(account, AccountJpaEntity.class);
    }

    //#endregion

}
