package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ManualCertificateEndpoint {

    public final static String MANUAL_CERTIFICATE_BASE_ROUTE = "manualCertificate";

    public final static String POLICY_TYPE_REFERENCE_ID_PARAM = "{policyTypeReferenceId}";
    public final static String PAST_OBSERVED_CASES_TYPE = "past";
    public final static String POLICY_TYPE = "policyType";


    public final static String REGISTER_MANUAL_CERTIFICATE_BY_POLICY_TYPE = POLICY_TYPE_REFERENCE_ID_PARAM + "/" + POLICY_TYPE;


    //PersistenceResponse saveCalculationsForRegulatedPolicy(BrokerSettlementCalculationsDhlDTO brokerSettlementCalculationsDhlDTO);
    ResponseEntity<PersistenceResponse> registerManualCertificates(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            long overwrite,
            List<Object> manualCertificates);

}
