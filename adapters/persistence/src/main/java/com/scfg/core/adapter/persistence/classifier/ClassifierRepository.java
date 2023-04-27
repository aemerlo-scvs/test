package com.scfg.core.adapter.persistence.classifier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassifierRepository extends JpaRepository<ClassifierJpaEntity, Long> {

    //List<ClasificadorJpaEntity> findAll();
    // Provisional

    @Query("SELECT cl " +
            "FROM  ClassifierJpaEntity cl " +
            "WHERE cl.referenceId = :classifierReferenceId and cl.classifierType.referenceId = :classifierTypeReferenceId ")
    Optional<ClassifierJpaEntity> findByReferencesIds(
            @Param("classifierReferenceId") long classifierReferenceId,
            @Param("classifierTypeReferenceId") long classifierTypeReferenceId);

    List<ClassifierJpaEntity> findAllByClassifierTypeId(long classifierTypeId);

    List<ClassifierJpaEntity> findAllByClassifierParentId(long classifierParentId);

    List<ClassifierJpaEntity> findAllByClassifierType_ReferenceIdOrderByOrderDesc(long referenceTypeId);

    // Custom Query
    @Query("SELECT cl " +
            "FROM ClassifierJpaEntity cl " +
            "WHERE cl.classifierType.referenceId = :classifierTypeReferenceId " +
            "AND ((cl.referenceId NOT IN (16,17) "+
            "AND cl.classifierType.referenceId = 2) OR cl.classifierType.referenceId <> 2)")
    List<ClassifierJpaEntity> findAllByClassifierTypeReferenceId(
            @Param("classifierTypeReferenceId") long classifierTypeReferenceId);

    @Query("SELECT cl " +
            "FROM ClassifierJpaEntity cl " +
            "JOIN FETCH cl.classifierType ct " +
            "WHERE cl.description = :description")
    Optional<ClassifierJpaEntity> findByDescription(@Param("description") String description);

    @Query("SELECT cl " +
            "FROM ClassifierJpaEntity cl " +
            "JOIN FETCH cl.classifierType ct " +
            "WHERE ct.referenceId = :referenceTypeCode AND cl.order = :order")
    Optional<ClassifierJpaEntity> findByReferenceTypeCodeAndOrder(
            @Param("referenceTypeCode") long referenceTypeCode,
            @Param("order") Integer order
    );

    Optional<ClassifierJpaEntity> findByAbbreviation(String abbreviation);

    @Query("SELECT cl " +
            "FROM ClassifierJpaEntity cl " +
            "WHERE LOWER(cl.description) LIKE LOWER(CONCAT('%', :description,'%')) " +
            "AND cl.referenceId = :refereneCode AND cl.classifierType.referenceId = :referenceTypeCode")
    Optional<ClassifierJpaEntity> findByDescriptionAndClassifier(
            @Param("description") String description,
            @Param("refereneCode") long refereneCode,
            @Param("referenceTypeCode") long referenceTypeCode);

    @Query("SELECT cl " +
            "FROM ClassifierJpaEntity cl " +
            "JOIN FETCH cl.classifierType ct " +
            "WHERE ct.referenceId = :referenceId")
    List<ClassifierJpaEntity> findAllWithDetailByReferenceId(@Param("referenceId") long referenceId);


    //ClassifierJpaEntity findById(long Id);
    //List<ClassifierJpaEntity> findAllByClassifierTypeId

}
