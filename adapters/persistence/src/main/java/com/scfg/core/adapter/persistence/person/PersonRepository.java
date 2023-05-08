package com.scfg.core.adapter.persistence.person;

import com.scfg.core.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE np.identificationNumber = :identificationNumber AND np.documentType = :documentType")
    Person findByNaturalPersonIdentificationNumberAndDocumentType(@Param("identificationNumber") String identificationNumber,
                                                                  @Param("documentType") Integer documentType);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE np.identificationNumber = :identificationNumber")
    Person findByNaturalPersonIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE p.status = :status")
    List<Person> findAll(@Param("status") Integer status);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE p.status = :status AND p.assignedGroupIdc = :assignedGroup AND jp.id IS NOT NULL")
    List<Person> findAllByAssignedGroupIdc(@Param("status") Integer status, @Param("assignedGroup") Integer assignedGroup);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE p.id = :personId AND p.status = :status")
    Person customFindById(@Param("personId") long personId, @Param("status") Integer status);

    //#region Dynamic Query

    default String getFindAllByFiltersBaseQuery() {
        return "SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
                "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
                "FROM PersonJpaEntity p " +
                "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
                "ON np.person.id = p.id " +
                "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
                "ON jp.person.id = p.id " +
                "WHERE";
    }

    //#endregion

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE p.id IN (:lstPersonId) AND p.status = :status")
    List<Person> findAllByListId(@Param("lstPersonId") List<Long> lstPersonId,@Param("status") Integer status);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "WHERE jp.nit = :nit AND p.status = :status")
    Person findByNitNumber(@Param("nit") Long nit, @Param("status") Integer status);

    @Query("SELECT new com.scfg.core.domain.person.Person(p.id, p.nationalityIdc, p.residenceIdc, p.activityIdc, p.reference, p.telephone," +
            "p.email, p.holder, p.insured, p.createdAt, p.lastModifiedAt, p.createdBy, p.lastModifiedBy, np, jp) " +
            "FROM PersonJpaEntity p " +
            "LEFT JOIN FETCH NaturalPersonJpaEntity np " +
            "ON np.person.id = p.id " +
            "LEFT JOIN FETCH JuridicalPersonJpaEntity jp " +
            "ON jp.person.id = p.id " +
            "INNER JOIN PolicyItemJpaEntity pi on pi.personId = p.id " +
            "INNER JOIN PolicyJpaEntity po on po.id = pi.policyId " +
            "WHERE po.id = :policyId AND p.status = :status")
    Person findByPolicyId(@Param("policyId") Long policyId, @Param("status") Integer status);
}
