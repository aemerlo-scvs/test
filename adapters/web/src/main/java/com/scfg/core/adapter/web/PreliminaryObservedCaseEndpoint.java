package com.scfg.core.adapter.web;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface PreliminaryObservedCaseEndpoint {


    public static final String PRELIMINARY_OBSERVED_CASE_TITLE = "Preeliminary Observed Case Resource";

    public final static String OBSERVED_CASES_BASE_ROUTE = "preliminaryObservedCases";

    public final static String POLICY_TYPE_REFERENCE_ID_PARAM = "{policyTypeReferenceId}";
    public final static String POLICY_TYPE = "policyType";

    public final static String MANAGER_PRELIMINARY_OBSERVED_CASES_BY_POLICY_TYPE =  POLICY_TYPE_REFERENCE_ID_PARAM + "/" + POLICY_TYPE;

    ResponseEntity getPreliminaryObservedCases(
            long policyTypeReferenceId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            HttpServletRequest request);
}
