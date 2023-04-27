package com.scfg.core.adapter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrokerSettlementCalculationsEndpoint {


    public final static String BROKER_SETTLEMENT_CALCULATIONS_BASE_ROUTE = "brokerSettlementCalculations";

    public final static String POLICY_TYPE_ID_PARAM = "{policyTypeReferenceId}";


    public final static String MANAGER_CALCULATIONS_BY_POLICY_TYPE = POLICY_TYPE_ID_PARAM + "/" + "policyType";


    //PersistenceResponse saveCalculationsForRegulatedPolicy(BrokerSettlementCalculationsDhlDTO brokerSettlementCalculationsDhlDTO);
    ResponseEntity registerBrokerCalculations(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            long overwrite,
            List<Object> brokerSettlementCalculations);

    ResponseEntity getBrokerSettlementCalculationsFiltered(long policyTypeReferenceId/*, long policyTypeId*/, long monthId, long yearId, long insurancePolicyHolderId);
}
