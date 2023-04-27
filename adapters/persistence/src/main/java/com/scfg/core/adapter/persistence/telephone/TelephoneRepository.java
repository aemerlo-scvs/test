package com.scfg.core.adapter.persistence.telephone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TelephoneRepository extends JpaRepository<TelephoneJpaEntity,Long> {

    @Query("SELECT t FROM TelephoneJpaEntity t " +
            "WHERE t.personId = :personId AND t.status = :status")
    List<TelephoneJpaEntity> findAllByPersonId(@Param("personId") Long personId, @Param("status") Integer status);
}
