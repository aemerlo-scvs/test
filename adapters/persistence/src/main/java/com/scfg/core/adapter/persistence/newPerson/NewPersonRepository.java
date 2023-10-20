package com.scfg.core.adapter.persistence.newPerson;

import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewPersonRepository extends JpaRepository<NewPersonJpaEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM NewPersonJpaEntity n WHERE n.identificationNumber = ?1")
    boolean findByIdentificationNumber(@Param("identificationNumber") String identificationNumber);

    @Query("SELECT np " +
            "FROM NewPersonJpaEntity np " +
            "WHERE np.id = :newPersonId AND np.status = :status")
    NewPersonJpaEntity customFindById(@Param("newPersonId") long newPersonId, @Param("status") Integer status);
}
