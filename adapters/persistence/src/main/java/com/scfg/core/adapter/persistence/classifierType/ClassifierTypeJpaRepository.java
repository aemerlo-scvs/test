package com.scfg.core.adapter.persistence.classifierType;

import com.scfg.core.domain.common.ClassifierType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassifierTypeJpaRepository extends JpaRepository<ClassifierTypeJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.common.ClassifierType(c.id, c.createdAt, c.lastModifiedAt, c.createdBy, c.lastModifiedBy, c.description, c.order, c.classifierTypeParentId, c.referenceId) " +
            "FROM ClassifierTypeJpaEntity c " +
            "WHERE c.status = 1")
    List<ClassifierType> customFindAll();

    @Query("SELECT DISTINCT c FROM ClassifierTypeJpaEntity c LEFT JOIN FETCH c.classifiers")
    List<ClassifierTypeJpaEntity> customFindAllDetail();

    ClassifierTypeJpaEntity findByReferenceId(long referenceId);



    @Modifying
    @Query("UPDATE ClassifierTypeJpaEntity " +
            "SET status = :status " +
            "WHERE id = :id")
    void deleteClassifierType(@Param("status") int status, @Param("id") long id);

    @Modifying
    @Query("UPDATE ClassifierJpaEntity " +
            "SET status = :status " +
            "WHERE classifierType.id = :id")
    void deleteClassifiers(@Param("status") int status, @Param("id") long id);

}
