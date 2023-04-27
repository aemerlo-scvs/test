package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SubscriptionTrackingEndpoint {


    public final static String SUBSCRIPTION_TRACKING_BASE_ROUTE = "subscriptionTracking";

    public final static String POLICY_TYPE_ID_PARAM = "{policyTypeReferenceId}";


    public final static String REGISTER_SUBSCRIPTION_TRACKING_BY_POLICY_TYPE = POLICY_TYPE_ID_PARAM + "/" + "policyType";


    ResponseEntity<PersistenceResponse> registerSubscriptionTracking(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            long overwrite,
            List<Object> subscriptionsTracking);
}
