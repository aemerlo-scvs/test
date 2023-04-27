package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.CoveragePolicyItem;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Plan;

import java.util.List;

public interface CoveragePolicyItemPort {

   CoveragePolicyItem save(CoveragePolicyItem coveragePolicyItem);

   List<CoveragePolicyItem> saveOrUpdateAll(List<CoveragePolicyItem> coveragePolicyItemList);

   List<CoveragePolicyItem> findByPolicyItemId(Long policyItemId);
   void deleteAllByPolicyItemId(long policyItemId);

   List<CoveragePolicyItem> findAllByPersonId(Long personId);

   List<CoveragePolicyItem> findAllByPersonIdGEL(Long personId, List<Plan> planList);

}
