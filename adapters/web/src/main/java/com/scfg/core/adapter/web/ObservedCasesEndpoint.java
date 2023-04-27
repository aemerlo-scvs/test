package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ObservedCasesEndpoint {


    public final static String OBSERVED_CASES_BASE_ROUTE = "observedCases";

    public final static String POLICY_TYPE_REFERENCE_ID_PARAM = "{policyTypeReferenceId}";
    public final static String PAST_OBSERVED_CASES_TYPE = "past";
    public final static String POLICY_TYPE = "policyType";


    public final static String MANAGER_LAST_OBSERVED_CASES_BY_POLICY_TYPE = PAST_OBSERVED_CASES_TYPE + "/" + POLICY_TYPE_REFERENCE_ID_PARAM + "/" + POLICY_TYPE;


    //PersistenceResponse saveCalculationsForRegulatedPolicy(BrokerSettlementCalculationsDhlDTO brokerSettlementCalculationsDhlDTO);
    ResponseEntity<PersistenceResponse> registerLastObservedCases(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            long overwrite,
            List<Object> lastObservedCases);

    ResponseEntity getLastObservedCasesFiltered(
            long policyTypeReferenceId/*, long policyTypeId*/,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            HttpServletRequest request);
}
