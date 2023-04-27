package com.scfg.core.adapter.web;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;
import org.springframework.http.ResponseEntity;

public interface ClassifierTypeEndpoint {


    public static final String CLASSIFIER_TYPE_TITLE = "Classifier Type Resource";

    public static final String CLASSIFIER_TYPE_ID_PARAM = "{classifierTypeId}";
    public static final String CLASSIFIER_REFERENCE_ID_PARAM = "existsReferenceId/{classifierReferenceId}";
    public static final String CLASSIFIER_TYPE_BASE_ROUTE = "classifierType";
    String DETAIL =  "/detail";

    String PARAM_PAGE = "/{page}/{size}";

    ResponseEntity getAllClassifierTypes();
    ResponseEntity getAllClassifierTypesDetail();
    ResponseEntity getAllClassifierTypesByPage(int size, int page);
    ResponseEntity getClassifierTypeById(long classifierTypeId);
    ResponseEntity existsClassifierTypeByReferenceId(long classifierReferenceId);


    ResponseEntity<PersistenceResponse> saveClassifierType(ClassifierType classifierType);
    ResponseEntity<PersistenceResponse> updateClassifierType(ClassifierType classifierType);
    ResponseEntity<PersistenceResponse> deleteClassifierType(long classifierTypeId);

}
