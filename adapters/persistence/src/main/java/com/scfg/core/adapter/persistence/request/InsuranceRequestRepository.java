package com.scfg.core.adapter.persistence.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InsuranceRequestRepository extends JpaRepository<InsuranceRequestJpaEntity, Long> {

    /*@Procedure(value = "sp_X", p)
    InsuranceRequestJpaEntity sp_X();*/


    @Query(value = "SELECT req " +
            "FROM InsuranceRequestJpaEntity req " +
            "LEFT JOIN FETCH req.requestStatus reqSt " +
            "LEFT JOIN FETCH req.currency cur " +
            "LEFT JOIN FETCH cur.classifierType clTyp " +
            "WHERE req.requestNumber = :requestNumber",
            countQuery = "SELECT COUNT(req.id) " +
                    "FROM InsuranceRequestJpaEntity req " +
                    "LEFT JOIN  req.requestStatus reqSt " +
                    "LEFT JOIN  req.currency cur " +
                    "LEFT JOIN  cur.classifierType clTyp " +
                    "WHERE req.requestNumber = :requestNumber")
    Page<InsuranceRequestJpaEntity> findByRequestNumber(
            @Param("requestNumber") String requestNumber,
            Pageable pageable);
}
