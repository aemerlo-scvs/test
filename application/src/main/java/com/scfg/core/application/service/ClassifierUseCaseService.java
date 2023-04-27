package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ClassifierUseCase;
import com.scfg.core.application.port.out.ClassifierPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class ClassifierUseCaseService implements ClassifierUseCase {

    private final ClassifierPort classifierPort;

    @Override
    public List<Classifier> getAllClassifiers() {
        return classifierPort.getAllClassifiers();
    }

    @Override
    public List<Classifier> getAllClassifiersByClassifierTypeReferenceId(long referenceTypeId) {
        return classifierPort.getAllClassifiersByClassifierTypeReferenceId(referenceTypeId);
    }

    @Override
    public Classifier getClassifierByReferencesIds(long classifierReferenceId, long classifierTypeReferenceId) {
        return classifierPort.getClassifierByReferencesIds(classifierReferenceId, classifierTypeReferenceId);
    }

    @Override
    public List<Classifier> getAllReportsByPolicyType(long policyTypeId) {
        return classifierPort.getAllReportsByPolicyType(policyTypeId);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse save(Classifier classifier) {
        return classifierPort.save(classifier, true);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse update(Classifier classifier) {
        return classifierPort.update(classifier);
    }


    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    @Override
    public PersistenceResponse delete(long classifierId) {
        Classifier classifier = classifierPort.getClassifierById(classifierId);
        return classifierPort.delete(classifier);
    }
}
