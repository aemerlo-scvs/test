package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhnDTO;

import java.util.List;
import java.util.Map;

public interface SubscriptionTrackingUseCase  {
    PersistenceResponse saveSubscriptionsTrackingForRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS, long overwrite);

    PersistenceResponse saveSubscriptionsTrackingForNotRegulatedPolicy(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId, long usersId, List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS, long overwrite);

    List<SubscriptionTrackingDhlDTO> getSubscriptionTrackingDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<SubscriptionTrackingDhnDTO> getSubscriptionTrackingDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
