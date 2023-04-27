package com.scfg.core.application.port.out;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;

import java.util.List;

public interface ClassifierPort {


    List<Classifier> getAllClassifiers();

    Classifier getClassifierByReferences(ClassifierEnum classifierEnum);
    Classifier getClassifierByReferencesIds(long classifierReferenceId, long classifierTypeReferenceId);

    Classifier getClassifierById(long classifierId);

    Classifier getClassifierByReferenceTypeCodeAndOrder(ClassifierTypeEnum classifierTypeEnum, Integer order);


    Classifier getClassifierByName(String classifierName, ClassifierTypeEnum classifierTypeEnum);

    Classifier getClassifierByAbbreviation(String abrreviation, ClassifierTypeEnum classifierTypeEnum);

    Classifier getClassifierByNameAndClassifier(String classifierName, long referenceCode, long referenceTypeCode);


    List<Classifier> getAllClassifiersByClassifierTypeReferenceId(long classifierTypeReferenceId);

    List<Classifier> getAllWithDetailByClassifierTypeReferenceId(long referenceId);

    // Custom methods
    List<Classifier> getAllReportsByPolicyType(long policyTypeId);

    PersistenceResponse save(Classifier classifier, boolean returnEntity);

    PersistenceResponse update(Classifier classifier);

    PersistenceResponse delete(Classifier classifier);

}
