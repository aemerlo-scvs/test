package com.scfg.core.adapter.persistence.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonJpaEntity, Long> {

    // Este método funciona, solo que habria que colocar la notación transactional por las entidades relacionales.
    PersonJpaEntity findByNaturalPersonIdentificationNumberAndNaturalPersonDocumentType(String identificationNumber, Integer documentType);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.naturalPerson.identificationNumber = :identificationNumber AND p.naturalPerson.documentType = :documentType")
    PersonJpaEntity findByNaturalPersonIdentificationNumberAndDocumentType(@Param("identificationNumber") String identificationNumber,
                                                                           @Param("documentType") Integer documentType);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.naturalPerson.identificationNumber = :identificationNumber")
    PersonJpaEntity findByNaturalPersonIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.status = :status")
    List<PersonJpaEntity> findAll(@Param("status") Integer status);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.status = :status AND p.assignedGroupIdc = :assignedGroup AND p.juridicalPerson IS NOT NULL")
    List<PersonJpaEntity> findAllByAssignedGroupIdc(@Param("status") Integer status, @Param("assignedGroup") Integer assignedGroup);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.id = :personId AND p.status = :status")
    PersonJpaEntity customFindById(@Param("personId") long personId, @Param("status") Integer status);

    //#region Dynamic Query

    default String getFindAllByFiltersBaseQuery() {
        return "SELECT p " +
                "FROM PersonJpaEntity p " +
                "LEFT JOIN FETCH p.naturalPerson np " +
                "LEFT JOIN FETCH p.juridicalPerson jp " +
                "WHERE";
    }

    //#endregion

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.id IN (:lstPersonId) AND p.status = :status")
    List<PersonJpaEntity> findAllByListId(@Param("lstPersonId") List<Long> lstPersonId,@Param("status") Integer status);

    @Query("SELECT p " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH p.naturalPerson np " +
            "LEFT JOIN FETCH p.juridicalPerson jp " +
            "WHERE p.nit = :nit AND p.status = :status")
    PersonJpaEntity findByNitNumber(@Param("nit") Long nit, @Param("status") Integer status);

}
