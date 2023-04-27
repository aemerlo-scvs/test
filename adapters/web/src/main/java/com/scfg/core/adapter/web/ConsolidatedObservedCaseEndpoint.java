package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ConsolidatedObservedCaseEndpoint {


    public final static String CONSOLIDATED_OBSERVED_CASE_BASE_ROUTE = "consolidatedObservedCase";

    public final static String POLICY_TYPE_ID_PARAM = "{policyTypeReferenceId}";


    public final static String MANAGER_CONSOLIDATED_OBSERVED_CASE_BY_POLICY_TYPE = POLICY_TYPE_ID_PARAM + "/" + "policyType";

    ResponseEntity<PersistenceResponse> registerConsolidatedObservedCase(
            long policyTypeReferenceId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long userId,
            long policyTypeId,
            long reportTypeId,
            long overwrite,
            List<Object> consolidatedObservedCaseList);


    ResponseEntity getConsolidatedObservedCasesFiltered(
            long policyTypeReferenceId/*, long policyTypeId*/,
            long monthId,
            long yearId,
            long insurancePolicyHolderId);
}
