package com.scfg.core.adapter.persistence.accountPolicy;

import com.scfg.core.application.port.out.AccountPolicyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.dto.vin.AccountPolicy;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPolicyPersistenceAdapter implements AccountPolicyPort {

    private final AccountPolicyRepository accountPolicyRepository;

    @Override
    public AccountPolicy findByPolicyId(Long policyId) {
        AccountPolicyJpaEntity accountPolicyJpaEntity = accountPolicyRepository.findByPolicyId(policyId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(accountPolicyJpaEntity);
    }

    @Override
    public AccountPolicy findByAccountIdAndPolicyId(Long accountId, Long policyId) {
        AccountPolicyJpaEntity accountPolicyJpaEntity = accountPolicyRepository.findByAccountIdAAndPolicyId(accountId,
                policyId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(accountPolicyJpaEntity);
    }

    @Override
    public AccountPolicy saveOrUpdate(AccountPolicy accountPolicy) {
        AccountPolicyJpaEntity accountPolicyJpaEntity = mapToJpaEntity(accountPolicy);
        accountPolicyJpaEntity = accountPolicyRepository.save(accountPolicyJpaEntity);
        return mapToDomain(accountPolicyJpaEntity);
    }


    //#region Mappers
    private AccountPolicy mapToDomain(AccountPolicyJpaEntity accountPolicyJpaEntity) {
        return new ModelMapper().map(accountPolicyJpaEntity,AccountPolicy.class);
    }

    private AccountPolicyJpaEntity mapToJpaEntity(AccountPolicy accountPolicy) {
        return new ModelMapper().map(accountPolicy,AccountPolicyJpaEntity.class);
    }
    //#endregion
}
