package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CoveragePolicyItemUseCase;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.domain.CoveragePolicyItem;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfSaveCoverageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoveragePolicyItemService implements CoveragePolicyItemUseCase {

    private final CoveragePolicyItemPort coveragePolicyItemPort;
    private final GeneralRequestPort generalRequestPort;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Override
    public Boolean saveOrUpdateAll(ClfSaveCoverageDTO oSaveCoverage) {

        boolean response = false;

        if (!oSaveCoverage.getExclusionComment().isEmpty()) {
            GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(oSaveCoverage.getRequestId());
            generalRequest.setExclusionComment(oSaveCoverage.getExclusionComment());
            generalRequestPort.saveOrUpdate(generalRequest);
        }

        if (oSaveCoverage.getCoverageList().size() > 0) {
            Long policyItemId = oSaveCoverage.getCoverageList().get(0).getPolicyItemId();
            coveragePolicyItemPort.deleteAllByPolicyItemId(policyItemId);

            List<CoveragePolicyItem> coveragePolicyItemList = new ArrayList<>();
            oSaveCoverage.getCoverageList().forEach(c -> {
                coveragePolicyItemList.add(new CoveragePolicyItem(c));
            });
            coveragePolicyItemPort.saveOrUpdateAll(coveragePolicyItemList);
            response = true;
        }

        return response;
    }
}
