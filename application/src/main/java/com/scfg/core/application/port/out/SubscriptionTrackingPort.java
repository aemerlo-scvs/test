package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhnDTO;

import java.util.List;

public interface SubscriptionTrackingPort {
    PersistenceResponse registerSubscriptionsTrackingForRegulatedPolicy(List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS, long overwrite);

    PersistenceResponse registerSubscriptionsTrackingForNotRegulatedPolicy(List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS, long overwrite);

    List<SubscriptionTrackingDhnDTO> getSubscriptionTrackingDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId);

    List<SubscriptionTrackingDhlDTO> getSubscriptionTrackingDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId);
}
