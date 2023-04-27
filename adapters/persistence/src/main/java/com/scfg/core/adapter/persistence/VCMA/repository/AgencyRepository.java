package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<AgencyJpaEntity, Integer> {
    //Agency findByAGENCY_ID(BigInteger id);

    @Query(value = "SELECT age " +
            "FROM AgencyJpaEntity age " +
            "WHERE age.DESCRIPTION = :description",
            countQuery = "SELECT COUNT(age.AGENCY_ID) " +
                    "FROM AgencyJpaEntity age " +
                    "WHERE age.DESCRIPTION = :description")
    Page<AgencyJpaEntity> findByDESCRIPTION(@Param("description") String description, Pageable pageable);

    @Query("SELECT COALESCE(MAX(agn.AGENCY_ID),0) " +
            "FROM AgencyJpaEntity agn ")
    Long findMaxAgencyId();

    //Agency findByAGENCY_IDAndDESCRIPTION(BigInteger id, String name);
    List<AgencyJpaEntity> findAll();
}
