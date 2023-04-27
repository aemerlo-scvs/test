package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ClassifierTypeUseCase;
import com.scfg.core.application.port.out.ClassifierTypePort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class ClassifierTypeService implements ClassifierTypeUseCase {

    private final ClassifierTypePort classifierTypePort;

    @Override
    public List<ClassifierType> getAll() {
        return classifierTypePort.findAll();
    }

    @Override
    public List<ClassifierType> getAllDetail() {
        return classifierTypePort.findAllDetail();
    }

    @Override
    public Object getAllByPage(int page, int size) {
        return classifierTypePort.findAllByPage(page, size);
    }

    @Override
    @Transactional
    public ClassifierType getClassifierTypeById(long classifierTypeId) {
        return classifierTypePort.getClassifierTypeById(classifierTypeId);
    }

    @Override
    @Transactional
    public Boolean existsClassifierTypeByReferenceId(long classifierReferenceId) {
        return classifierTypePort.existsClassifierTypeByReferenceId(classifierReferenceId);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse save(ClassifierType classifierType) {
        return classifierTypePort.saveOrUpdate(classifierType);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})

    @Override
    public PersistenceResponse update(ClassifierType classifierType) {
        return classifierTypePort.saveOrUpdate(classifierType);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})

    @Override
    public PersistenceResponse delete(long classifierTypeId) {
        ClassifierType classifierTypeFind = classifierTypePort.getClassifierTypeById(classifierTypeId);
        return classifierTypePort.delete(classifierTypeFind);
    }
}
