package com.scfg.core.adapter.persistence.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {

    @Query( "SELECT p FROM TransactionJpaEntity p " +
            "WHERE p.paymentPlanId=:paymentPlanId AND p.status=:status ORDER BY p.id DESC ")
    List<TransactionJpaEntity>findLAllByPaymentPlanId(@Param("paymentPlanId")Long paymentPlanId, @Param("status")Integer status);
}
