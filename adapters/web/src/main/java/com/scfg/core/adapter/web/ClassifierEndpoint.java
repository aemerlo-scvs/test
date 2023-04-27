package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import org.springframework.http.ResponseEntity;

public interface ClassifierEndpoint {

    public static final String CLASSIFIER_TITLE = "Classifier Resource";


    public static final String CLASSIFIER_TYPE_REFERENCE_ID_PARAM = "{classifierTypeReferenceId}";
    public static final String CLASSIFIER_BASE_ROUTE = "classifier";
    public static final String RESOURCE_CLASSIFIER_TYPE = "classifierType"+ "/"+ CLASSIFIER_TYPE_REFERENCE_ID_PARAM;

    // Var for endpoints

    public static final String CLASSIFIER_REFERENCE_ID_PARAM = "{classifierReferenceId}/";
    public static final String GET_CLASSIFIER_BY_REFERENCES_PARAMS = CLASSIFIER_REFERENCE_ID_PARAM + "/"+RESOURCE_CLASSIFIER_TYPE ;
    public static final String GET_BY_PARENT_ID = "parentId/{parentId}";
    public static final String POLICY_TYPE_ID_PARAM = "{policyTypeId}";



    // Custom endpoints
    public static final String GET_REPORTS_BY_POLICY_TYPE_ID = "policyType"+ "/"+ POLICY_TYPE_ID_PARAM;

    ResponseEntity getAllClassifiers();
    ResponseEntity getAllClassifiersByClassifierTypeReferenceId(long classifierTypeReferenceId);


    ResponseEntity getAllClassifiersByParentId(long parentId);
    ResponseEntity getAllReportsByPolicyType(long policyTypeId);
    ResponseEntity getClassifierByReferencesIds(long classifierReferenceId, long classifierTypeReferenceId);

    ResponseEntity<PersistenceResponse> saveClassifier(Classifier classifier);
    ResponseEntity<PersistenceResponse> updateClassifier(Classifier classifier);
    ResponseEntity<PersistenceResponse> deleteClassifier(long classifierId);


}
