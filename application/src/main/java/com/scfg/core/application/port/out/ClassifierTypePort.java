package com.scfg.core.application.port.out;

import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;

import java.util.List;

public interface ClassifierTypePort {

    List<ClassifierType> findAll();

    List<ClassifierType> findAllDetail();

    Object findAllByPage(int page, int size);

    List<ClassifierType> getAllClassifiersTypes(); //Ya no se usa, revisar para borrar

    ClassifierType getClassifierTypeById(long classifierTypeId);
    ClassifierType getClassifierTypeByReferenceId(long referenceId);

    Boolean existsClassifierTypeByReferenceId(long classifierReferenceId);

    PersistenceResponse save(ClassifierType classifierType);

    PersistenceResponse update(ClassifierType classifierType);

    PersistenceResponse saveOrUpdate(ClassifierType classifierType);

    PersistenceResponse delete(ClassifierType classifierTypeFind);
}
