package com.scfg.core.adapter.web;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ValidateInsuredMortgageReliefEndpoint {


    public static final String VALIDATE_INSURED_MORTGAGE_RELIEF_TITLE = "Validate Insured Mortgage Relief Resource";

    public final static String VALIDATE_INSURED_MORTGAGE_RELIEF_BASE_ROUTE = "validateInsuredMortgageRelief";

    public final static String POLICY_TYPE_REFERENCE_ID_PARAM = "{policyTypeReferenceId}";
    public final static String POLICY_TYPE = "policyType";
    public final static String INSUREDS = "insureds";

    public final static String VALIDATE_INSURED_MORTGAGE_RELIEF_BY_POLICY_TYPE = "/" + POLICY_TYPE_REFERENCE_ID_PARAM + "/" + POLICY_TYPE;
    public final static String GET_INSUREDS_SUMMARY_BY_POLICY_TYPE = INSUREDS + "/" + POLICY_TYPE_REFERENCE_ID_PARAM + "/" + POLICY_TYPE;

    ResponseEntity validateInsureds(long policyTypeReferenceId, long policyTypeId, long monthId, long yearId, long insurancePolicyHolderId, long usersId);

    ResponseEntity getInsuredsSummary(long policyTypeReferenceId,
                                      long policyTypeId,
                                      long monthId,
                                      long yearId,
                                      long insurancePolicyHolderId);

    ResponseEntity getInsuredsInOrder(
            long policyTypeReferenceId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            HttpServletRequest request);
}
