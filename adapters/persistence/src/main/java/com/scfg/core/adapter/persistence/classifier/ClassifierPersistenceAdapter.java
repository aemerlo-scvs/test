package com.scfg.core.adapter.persistence.classifier;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.scfg.core.adapter.persistence.classifierType.ClassifierTypeJpaEntity;
import com.scfg.core.adapter.persistence.classifierType.ClassifierTypePersistenceAdapter;
import com.scfg.core.application.port.out.ClassifierPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.ClassifierNotFoundException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ClassifierPersistenceAdapter implements ClassifierPort {

    private final ClassifierRepository classifierRepository;


    @Override

    public List<Classifier> getAllClassifiers() {
        Object clasificadores = classifierRepository.findAll();
        return (List<Classifier>) clasificadores;
    }

    @Override
    public Classifier getClassifierByReferences(ClassifierEnum classifierEnum) {
        long referenceCode = classifierEnum.getReferenceCode(),
                referenceTypeCode = classifierEnum.getReferenceCodeType();
        ClassifierJpaEntity classifierFind = classifierRepository.findByReferencesIds(referenceCode, referenceTypeCode)
                .orElseThrow(() ->
                        new NotDataFoundException("Classifier: " + referenceCode + " Not found")
                );
        return mapToDomain(classifierFind);
    }

    @Override
    public Classifier getClassifierByReferencesIds(long classifierReferenceId, long classifierTypeReferenceId) {
        ClassifierJpaEntity classifierFind = classifierRepository.findByReferencesIds(classifierReferenceId, classifierTypeReferenceId)
                .orElseThrow(() ->
                        new NotDataFoundException("Classifier: " + classifierReferenceId + " Not found")
                );

        /*return HelpersMethods.mapper()
                .convertValue(classifierFind, new TypeReference<Classifier>() {
                });*/

        return mapToDomain(classifierFind);
    }

    @Override
    public Classifier getClassifierById(long classifierId) {
        ClassifierJpaEntity classifierFind = classifierRepository.findById(classifierId)
                .orElseThrow(() ->
                        new NotDataFoundException("Classifier: " + classifierId + " Not found")
                );
        return mapToDomain(classifierFind);
    }

    @Override
    public Classifier getClassifierByReferenceTypeCodeAndOrder(ClassifierTypeEnum classifierTypeEnum, Integer order) {
        ClassifierJpaEntity classifierFind = classifierRepository.findByReferenceTypeCodeAndOrder(classifierTypeEnum.getReferenceId(), order)
                .orElseThrow(() -> {
                            return new ClassifierNotFoundException("Classifier Ref: " + classifierTypeEnum.getName() + " Not found", classifierTypeEnum.getName());
                            //return new NotDataFoundException("Classifier Name: " + classifierName + " Not found -> " + classifierTypeEnum.getName());
                        }
                );
        return mapToDomain(classifierFind);


    }

    @Override
    public Classifier getClassifierByNameAndClassifier(String classifierName, long referenceCode, long referenceTypeCode) {
        ClassifierJpaEntity classifierFind = classifierRepository.findByDescriptionAndClassifier(classifierName, referenceCode, referenceTypeCode)
                .orElseThrow(() ->
                        new NotDataFoundException("Classifier Name: " + classifierName + " Not found")
                );
        return mapToDomain(classifierFind);
    }

    @Override
    public Classifier getClassifierByName(String classifierName, ClassifierTypeEnum classifierTypeEnum) {
        ClassifierJpaEntity classifierFind = classifierRepository.findByDescription(classifierName)
                .orElseThrow(() -> {
                            return new ClassifierNotFoundException("Classifier Name: " + classifierName + " Not found", classifierTypeEnum.getName());
                            //return new NotDataFoundException("Classifier Name: " + classifierName + " Not found -> " + classifierTypeEnum.getName());
                        }
                );
        return mapToDomain(classifierFind);
        //return classifierFind.isPresent()? mapToDomain(classifierFind.get()): null;
    }

    @Override
    public Classifier getClassifierByAbbreviation(String abrreviation, ClassifierTypeEnum classifierTypeEnum) {
        ClassifierJpaEntity classifierFind = classifierRepository.findByAbbreviation(abrreviation)
                .orElseThrow(() -> {
                            return new ClassifierNotFoundException("Classifier Abbrev Name: " + abrreviation + " Not found", classifierTypeEnum.getName());
                            //return new NotDataFoundException("Classifier Name: " + classifierName + " Not found", ClassifierTypeEnum.)
                        }
                );
        return mapToDomain(classifierFind);

    }

    // TODO: Casteo a la entidad de dominio
    @Override
    public List<Classifier> getAllClassifiersByClassifierTypeReferenceId(long classifierTypeReferenceId) {
        List<ClassifierJpaEntity> listAux = classifierRepository.findAllByClassifierTypeReferenceId(classifierTypeReferenceId);

        List<Classifier> list = HelpersMethods.mapper()
                .convertValue(listAux,
                        new TypeReference<List<Classifier>>() {
                        });
        return list;
    }

    @Override
    public List<Classifier> getAllWithDetailByClassifierTypeReferenceId(long referenceId) {
        List<ClassifierJpaEntity> list = classifierRepository.findAllWithDetailByReferenceId(referenceId);
        List<Classifier> classifiers = new ArrayList<>();
        list.forEach(x -> {
            classifiers.add(mapToDomain(x));
        });
        return classifiers;
    }

    @Override
    public List<Classifier> getAllReportsByPolicyType(long policyTypeId) {
        return ((List<Classifier>) (Object) classifierRepository.findAllByClassifierParentId(policyTypeId));
    }


    @Override
    public PersistenceResponse save(Classifier classifier, boolean returnEntity) {
        ClassifierJpaEntity classifierJpaEntity = mapToJpaEntity(classifier);
        try {
            classifierJpaEntity = classifierRepository.save(classifierJpaEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return new PersistenceResponse(
                Classifier.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                returnEntity ? mapToDomain(classifierJpaEntity) : null
        );
    }

    @Override
    public PersistenceResponse update(Classifier classifier) {
        ClassifierJpaEntity classifierJpaEntity = mapToJpaEntity(classifier);
        classifierJpaEntity = classifierRepository.save(classifierJpaEntity);
        // partial modified
        return new PersistenceResponse(
                Classifier.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                null
        );
    }

    @Override
    public PersistenceResponse delete(Classifier classifier) {
        ClassifierJpaEntity classifierJpaEntity = mapToJpaEntity(classifier);
        // status for deleted
        classifierJpaEntity.setStatus(PersistenceStatusEnum
                .DELETED
                .getValue());
        classifierRepository.save(classifierJpaEntity);

        return new PersistenceResponse(
                Classifier.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }


    // TODO: Modificar los mapeos que se manejan para las entidads

    /***
     * Persist model in database
     * @param classifier
     * @return
     */
    public static ClassifierJpaEntity mapToJpaEntity(Classifier classifier) {
        ClassifierJpaEntity result = new ClassifierJpaEntity();
        result.setId(classifier.getId());
        result.setAbbreviation(classifier.getAbbreviation());
        result.setDescription(classifier.getDescription());
        result.setOrder(classifier.getOrder());
        result.setReferenceId(classifier.getReferenceId());
        result.setClassifierParentId(classifier.getClassifierParentId());
        result.setReferenceId(classifier.getReferenceId());
        result.setEnabledDelete((classifier.getEnabledDelete() == null) ? null : classifier.getEnabledDelete());
        // for relations
        result.setClassifierType(ClassifierTypeJpaEntity.builder()
                .id(classifier.getClassifierTypeId())
                .build());

        /*result.setCreatedAt(classifier.getCreatedAt());
        result.setLastModifiedAt(classifier.getLastModifiedAt());*/
        return result;

    }


    /***
     * Export domain for consume APIs
     * @param classifierJpaEntity
     * @return
     */
    public static Classifier mapToDomain(ClassifierJpaEntity classifierJpaEntity) {
        Classifier result = new Classifier();
        result.setId(classifierJpaEntity.getId());
        result.setAbbreviation(classifierJpaEntity.getAbbreviation());
        result.setDescription(classifierJpaEntity.getDescription());
        result.setOrder(classifierJpaEntity.getOrder());
        result.setReferenceId(classifierJpaEntity.getReferenceId());
        //result.setClassifierTypeId(classifierJpaEntity.getClassifierTypeId());
        result.setClassifierParentId(classifierJpaEntity.getClassifierParentId());

        result.setCreatedAt(classifierJpaEntity.getCreatedAt());
        result.setLastModifiedAt(classifierJpaEntity.getLastModifiedAt());

        // Relationship
        result.setClassifierType(ClassifierTypePersistenceAdapter.mapToDomain(classifierJpaEntity.getClassifierType(), false));


        return result;
    }

}
