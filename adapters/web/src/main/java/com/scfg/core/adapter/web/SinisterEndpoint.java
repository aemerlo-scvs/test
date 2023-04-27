package com.scfg.core.adapter.web;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SinisterEndpoint {


    public final static String SINISTER_BASE_ROUTE = "sinister";

    public final static String POLICY_TYPE_ID_PARAM = "{policyTypeReferenceId}";


    public final static String MANAGER_SINISTERS_BY_POLICY_TYPE = POLICY_TYPE_ID_PARAM + "/" + "policyType";


    //PersistenceResponse saveCalculationsForRegulatedPolicy(BrokerSettlementCalculationsDhlDTO brokerSettlementCalculationsDhlDTO);
    ResponseEntity registerSinisters(
            long policyTypeReferenceId,
            long policyTypeId,
            long monthId,
            long yearId,
            long insurancePolicyHolderId,
            long reportTypeId,
            long usersId,
            long overwrite,
            List<Object> sinisters);

    ResponseEntity getSinistersFiltered(long policyTypeReferenceId, long monthId, long yearId, long insurancePolicyHolderId);
}
