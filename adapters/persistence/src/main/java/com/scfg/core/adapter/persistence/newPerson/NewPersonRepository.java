package com.scfg.core.adapter.persistence.newPerson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewPersonRepository extends JpaRepository<NewPersonJpaEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM NewPersonJpaEntity n WHERE n.identificationNumber = ?1")
    boolean findByIdentificationNumber(@Param("identificationNumber") String identificationNumber);
}
