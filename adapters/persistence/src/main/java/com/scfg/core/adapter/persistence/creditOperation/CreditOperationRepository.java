package com.scfg.core.adapter.persistence.creditOperation;

import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreditOperationRepository extends JpaRepository<CreditOperationJpaEntity, Long> {

    @Query(value = "SELECT ope " +
            "FROM CreditOperationJpaEntity ope " +
            "LEFT JOIN FETCH ope.insuranceRequest req " +
            "LEFT JOIN FETCH ope.currency cur " +
            "WHERE ope.operationNumber = :operationNumber",
            countQuery = "SELECT COUNT(ope.id) " +
                    "FROM CreditOperationJpaEntity ope " +
                    "LEFT JOIN ope.insuranceRequest req " +
                    "LEFT JOIN ope.currency cur " +
                    "WHERE ope.operationNumber = :operationNumber")
    Page<CreditOperationJpaEntity> findByOperationNumber(
            @Param("operationNumber") long operationNumber,
            Pageable pageable);
}
