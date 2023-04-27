package com.scfg.core.adapter.persistence.policyItemMathReserve;

import com.scfg.core.application.port.out.PolicyItemMathReservePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.PolicyItemMathReserve;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyItemMathReserveAdapter implements PolicyItemMathReservePort {

    private final PolicyItemMathReserveRepository policyItemMathReserveRepository;

    @Override
    public PolicyItemMathReserve saveOrUpdate(PolicyItemMathReserve policyItemMathReserve) {
        PolicyItemMathReserveJpaEntity policyItemMathReserveJpaEntity = mapToJpaEntity(policyItemMathReserve);
        policyItemMathReserveJpaEntity = policyItemMathReserveRepository.save(policyItemMathReserveJpaEntity);
        return mapToDomain(policyItemMathReserveJpaEntity);
    }

    @Override
    public List<PolicyItemMathReserve> saveOrUpdateAll(List<PolicyItemMathReserve> policyItemMathReserveList) {
        List<PolicyItemMathReserveJpaEntity> policyItemMathReserveJpaEntityList = new ArrayList<>();
        for (PolicyItemMathReserve pol: policyItemMathReserveList) {
            PolicyItemMathReserveJpaEntity a = mapToJpaEntity(pol);
            policyItemMathReserveJpaEntityList.add(a);
        }
        policyItemMathReserveJpaEntityList = policyItemMathReserveRepository.saveAll(policyItemMathReserveJpaEntityList);

        List<PolicyItemMathReserve> policyItemMathReserves = new ArrayList<>();
        for (PolicyItemMathReserveJpaEntity pol: policyItemMathReserveJpaEntityList) {
            PolicyItemMathReserve a = mapToDomain(pol);
            policyItemMathReserves.add(a);
        }

        return policyItemMathReserves;
    }

    @Override
    public List<PolicyItemMathReserve> findByPolicyItemId(Long policyItemId) {
        List<PolicyItemMathReserveJpaEntity> policyItemMathReserveJpaEntityList =
                policyItemMathReserveRepository.findAllByPolicyItemId(policyItemId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<PolicyItemMathReserve> policyItemMathReserves = new ArrayList<>();
        if (policyItemMathReserveJpaEntityList == null || policyItemMathReserveJpaEntityList.isEmpty()) {
            return policyItemMathReserves;
        }
        for (PolicyItemMathReserveJpaEntity pol: policyItemMathReserveJpaEntityList) {
            PolicyItemMathReserve a = mapToDomain(pol);
            policyItemMathReserves.add(a);
        }
        return policyItemMathReserves;
    }

    //#region Mappers

    private PolicyItemMathReserve mapToDomain(PolicyItemMathReserveJpaEntity policyItemMathReserveJpaEntity) {
        return new ModelMapper().map(policyItemMathReserveJpaEntity,PolicyItemMathReserve.class);
    }

    private PolicyItemMathReserveJpaEntity mapToJpaEntity(PolicyItemMathReserve policyItemMathReserve) {
        return  new ModelMapper().map(policyItemMathReserve,PolicyItemMathReserveJpaEntity.class);
    }

    //#endregion
}
