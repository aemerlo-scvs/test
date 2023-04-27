package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;
import org.springframework.http.ResponseEntity;

public interface ClientEndpoint {

    public static final String CLIENT_TITLE = "Client Resource";

    public static final String CLIENT_BASE_ROUTE = "client";

    // Var for endpoints

    public static final String CLASSIFIER_REFERENCE_ID_PARAM = "{classifierReferenceId}/";
    public static final String GET_BY_PARENT_ID = "getByParentId/{parentId}";
    public static final String POLICY_TYPE_ID_PARAM = "{policyTypeId}";



    // Custom endpoints
    public static final String GET_REPORTS_BY_POLICY_TYPE_ID = "policyType"+ "/"+ POLICY_TYPE_ID_PARAM;

    ResponseEntity getAllClients();
    ResponseEntity<PersistenceResponse> saveClient(Client client);

}
