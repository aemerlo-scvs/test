package com.scfg.core.adapter.persistence.paymentPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlanJpaEntity, Long> {

    @Query(value = "SELECT p FROM PaymentPlanJpaEntity p " +
            "WHERE p.annexeId = :annexeId AND p.status = :status")
    Optional<PaymentPlanJpaEntity> findOptionalByAnnexeId(@Param("annexeId") Long annexeId, @Param("status") Integer status);
}
