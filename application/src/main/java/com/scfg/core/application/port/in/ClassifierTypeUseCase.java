package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;

import java.util.List;

public interface ClassifierTypeUseCase {

    List<ClassifierType> getAll();

    List<ClassifierType> getAllDetail();

    Object getAllByPage(int page, int size);

    ClassifierType getClassifierTypeById(long classifierTypeId);

    Boolean existsClassifierTypeByReferenceId(long classifierReferenceId);

    PersistenceResponse save(ClassifierType classifierType);

    PersistenceResponse update(ClassifierType classifierType);

    PersistenceResponse delete(long classifierTypeId);
}
