package com.scfg.core.adapter.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {

    @Query("SELECT p " +
            "FROM PaymentJpaEntity p " +
            "WHERE p.generalRequestId = :generalRequestId AND p.status = :status " +
            "ORDER BY p.id DESC")
    List<PaymentJpaEntity> findAllByGeneralRequestId(@Param("generalRequestId") Long generalRequestId, @Param("status") Integer status);

}
