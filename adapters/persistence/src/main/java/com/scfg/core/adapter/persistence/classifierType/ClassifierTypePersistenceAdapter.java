package com.scfg.core.adapter.persistence.classifierType;

import com.scfg.core.adapter.persistence.classifier.ClassifierPersistenceAdapter;
import com.scfg.core.application.port.out.ClassifierTypePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ClassifierTypePersistenceAdapter implements ClassifierTypePort {

    private final ClassifierTypeRepository classifierTypeRepository;
    private final ClassifierTypeJpaRepository classifierTypeJpaRepository;

    @Override
    public List<ClassifierType> findAll() {
        return classifierTypeJpaRepository.customFindAll();
    }

    @Override
    public List<ClassifierType> findAllDetail() {
        Object list = classifierTypeJpaRepository.customFindAllDetail();
        return (List<ClassifierType>) list;
    }

    @Override
    public Object findAllByPage(int page, int size) {
        Pageable newPage = PageRequest.of(page, size);
        Page<ClassifierTypeJpaEntity> list = classifierTypeJpaRepository.findAll(newPage);
        list.getContent().forEach(c -> c.setClassifiers(new ArrayList<>()));
        return list;
    }

    @Override
    public List<ClassifierType> getAllClassifiersTypes() {
        return classifierTypeJpaRepository.customFindAll();
    }

    @Override
    public ClassifierType getClassifierTypeById(long classifierTypeId) {
        ClassifierTypeJpaEntity classifierTypeFind = classifierTypeRepository.findById(classifierTypeId)
                .orElseThrow(() -> new NotDataFoundException("Classifier: " + classifierTypeId + " not found"));
        return mapToDomain(classifierTypeFind, false);
    }


    @Override
    public Boolean existsClassifierTypeByReferenceId(long classifierReferenceId) {
        ClassifierTypeJpaEntity classifierTypeFind = classifierTypeJpaRepository.findByReferenceId(classifierReferenceId);
        return classifierTypeFind != null;
    }

    @Override
    public ClassifierType getClassifierTypeByReferenceId(long referenceId) {
        return mapToDomain(classifierTypeJpaRepository.findByReferenceId(referenceId), false);
    }

    @Override
    public PersistenceResponse save(ClassifierType classifierType) {
        ClassifierTypeJpaEntity classifierTypeJpaEntity = mapToJpaEntityDeprecated(classifierType);
        try {
            classifierTypeJpaEntity = classifierTypeRepository.save(classifierTypeJpaEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return new PersistenceResponse(
                ClassifierType.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                classifierTypeJpaEntity
        );
    }

    @Override
    public PersistenceResponse update(ClassifierType classifierType) {

        ClassifierTypeJpaEntity classifierTypeJpaEntity = mapToJpaEntityDeprecated(classifierType);

        classifierTypeJpaRepository.save(classifierTypeJpaEntity);

        // partial modified
        return new PersistenceResponse(
                ClassifierType.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                null
        );
    }

    @Override
    public PersistenceResponse saveOrUpdate(ClassifierType classifierType) {
        Long isUpdating = classifierType.getId();
        ClassifierTypeJpaEntity classifierTypeJpaEntity = mapToJpaEntity(classifierType);
        if (classifierTypeJpaEntity.getClassifiers() != null) {
            classifierTypeJpaEntity.getClassifiers().forEach(c -> {
                if (c.getDescription() == null || c.getDescription().equals("")) {
                    c.setStatus(PersistenceStatusEnum.DELETED.getValue());
                }
                c.setClassifierType(classifierTypeJpaEntity);
            });
        }
        classifierTypeJpaRepository.save(classifierTypeJpaEntity);

        return new PersistenceResponse(
                ClassifierType.class.getSimpleName(),
                (isUpdating == null) ? ActionRequestEnum.CREATE : ActionRequestEnum.UPDATE,
                (isUpdating == null) ? classifierTypeJpaEntity : null
        );
    }

    @Override
    public PersistenceResponse delete(ClassifierType classifierTypeFind) {
//        ClassifierTypeJpaEntity classifierTypeJpaEntity = mapToJpaEntity(classifierTypeFind);
//        classifierTypeJpaEntity.setStatus(PersistenceStatusEnum
//                .DELETED
//                .getValue());
//        classifierTypeRepository.save(classifierTypeJpaEntity);
        classifierTypeJpaRepository.deleteClassifierType(PersistenceStatusEnum.DELETED.getValue(), classifierTypeFind.getId());
        classifierTypeJpaRepository.deleteClassifiers(PersistenceStatusEnum.DELETED.getValue(), classifierTypeFind.getId());

        return new PersistenceResponse(
                ClassifierType.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }


    //#region Mappers

    /***
     * Persist model in database
     * @param classifierType
     * @return
     */
    // Deprecated
    public static ClassifierTypeJpaEntity mapToJpaEntityDeprecated(ClassifierType classifierType) {
        ClassifierTypeJpaEntity classifierTypeJpaEntity = ClassifierTypeJpaEntity.builder()
                .id(classifierType.getId())
                .description(classifierType.getDescription())
                .referenceId(classifierType.getReferenceId())
                .classifierTypeParentId(classifierType.getClassifierTypeParentId())
                .order(classifierType.getOrder())
                .build();
        return classifierTypeJpaEntity;

    }


    public static ClassifierTypeJpaEntity mapToJpaEntity(ClassifierType classifierType) {
        ClassifierTypeJpaEntity classifierTypeJpaEntity = ClassifierTypeJpaEntity.builder()
                .id(classifierType.getId())
                .description(classifierType.getDescription())
                .referenceId(classifierType.getReferenceId())
                .classifierTypeParentId(classifierType.getClassifierTypeParentId())
                .order(classifierType.getOrder())
                .classifiers(classifierType.getClassifiers() != null
                        ? classifierType.getClassifiers()
                        .stream()
                        .map(c -> ClassifierPersistenceAdapter.mapToJpaEntity(c))
                        .collect(Collectors.toList())
                        : null)
                .build();
        return classifierTypeJpaEntity;
    }

    /***
     * Export domain for consume APIs
     * @param classifierTypeJpaEntity
     * @return
     */
    public static ClassifierType mapToDomain(ClassifierTypeJpaEntity classifierTypeJpaEntity, boolean withChildren) {

        ClassifierType classifierType = ClassifierType.builder()
                .id(classifierTypeJpaEntity.getId())
                .description(classifierTypeJpaEntity.getDescription())
                .classifierTypeParentId(classifierTypeJpaEntity.getClassifierTypeParentId())
                .referenceId(classifierTypeJpaEntity.getReferenceId())
                .createdAt(classifierTypeJpaEntity.getCreatedAt())
                .lastModifiedAt(classifierTypeJpaEntity.getLastModifiedAt())
                .classifiers(withChildren ?
                        classifierTypeJpaEntity.getClassifiers()
                                .stream()
                                .map(c -> ClassifierPersistenceAdapter.mapToDomain(c))
                                .collect(Collectors.toList())
                        : null)
                .build();

        return classifierType;

        /*Classifier result = new Classifier();
        result.setId(classifierTypeJpaEntity.getId());
        result.setAbbreviation(classifierTypeJpaEntity.getAbbreviation());
        result.setDescription(classifierTypeJpaEntity.getDescription());
        result.setOrder(classifierTypeJpaEntity.getOrder());
        result.setClassifierTypeId(classifierTypeJpaEntity.getClassifierTypeId());
        result.setClassifierParentId(classifierTypeJpaEntity.getClassifierParentId());

        result.setCreatedAt(classifierTypeJpaEntity.getCreatedAt());
        result.setLastModifiedAt(classifierTypeJpaEntity.getLastModifiedAt());

        // relations
        result.setClassifierType();*/


        //return result;
    }

    //#endregion

}
