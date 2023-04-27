package com.scfg.core.application.service;

import com.scfg.core.application.port.in.MortgageReliefItemUseCase;
import com.scfg.core.application.port.out.MortgageReliefItemPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class MortgageReliefItemService implements MortgageReliefItemUseCase {

    private final MortgageReliefItemPort mortgageReliefItemPort;

    @Override
    public List<MortgageReliefItem> getMortgageReliefItemByIDs(long monthId, long yearId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId) {
        return mortgageReliefItemPort.getMortgageReliefItemsByIDs(monthId, yearId, reportTypeId, policyTypeId, insurancePolicyHolderId);
    }
}
