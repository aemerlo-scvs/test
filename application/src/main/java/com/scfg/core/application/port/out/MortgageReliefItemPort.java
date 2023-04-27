package com.scfg.core.application.port.out;

import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;

import java.util.List;

public interface MortgageReliefItemPort {

    List<MortgageReliefItem> getMortgageReliefItemsByIDs(
            long monthId, long yearId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId);

    void disableLastInformation(long monthId, long yearId, long reportTypeId, long policyTypeId, long usersId, long insurancePolicyHolderId);

    void callSpRollbackRelatedEntities(
            long rollbackCreditOperation, long rollbackInsuranceRequest,
            long rollbackClient, long rollbackMortgageReliefItem);

    MortgageReliefItem save(MortgageReliefItem mortgageReliefItem);

    void deleteForPeriod(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId);

    void deleteAllByPolicyTypeAndReportTypeAndInsuranceHolder(long policyTypeId,  long reportTypeId, long insurancePolicyHolderId);
}
