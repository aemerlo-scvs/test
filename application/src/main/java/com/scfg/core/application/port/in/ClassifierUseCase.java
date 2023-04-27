package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;

import java.util.List;

public interface ClassifierUseCase {

    List<Classifier> getAllClassifiers();
    List<Classifier> getAllClassifiersByClassifierTypeReferenceId(long referenceTypeId);
    Classifier getClassifierByReferencesIds(long classifierReferenceId, long classifierTypeReferenceId);

    List<Classifier> getAllReportsByPolicyType(long policyTypeId);

    PersistenceResponse save(Classifier classifier);
    PersistenceResponse update(Classifier classifier);
    PersistenceResponse delete(long classifierId);
}

