package com.scfg.core.adapter.persistence.annexe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AnnexeRepository extends JpaRepository<AnnexeJpaEntity, Long>{

    @Query("SELECT MAX(a.annexeNumber) FROM AnnexeJpaEntity a " +
            "WHERE a.policyId =:policyId ")
    Long getAnnexeNumber(@Param("policyId") Long policyId);

    @Query("SELECT p FROM AnnexeJpaEntity p " +
            "WHERE p.requestAnnexeId = :requestAnnexeId AND p.status = :status AND p.annexeTypeIdc = :annexeTypeId " +
            "ORDER BY p.id desc")
    List<AnnexeJpaEntity> getOptionalByAnnexeTypeIdAndRequestAnnexeId(
            @Param("annexeTypeId") Integer annexeTypeId,
            @Param("requestAnnexeId") Long requestAnnexeId,
            @Param("status") Integer status);

    @Query(value = "SELECT p FROM AnnexeJpaEntity p " +
            "WHERE p.id = :annexeId AND p.status = :status")
    Optional<AnnexeJpaEntity> findOptionalById(@Param("annexeId") Long annexeId, @Param("status") Integer status);

    @Query("SELECT\n" +
            "CASE WHEN (t.id IS NOT NULL AND ra.statusIdc = :requestAnnexeStatusPaid ) THEN 'PAGADO' ELSE 'NO PAGADO' END\n" +
            "FROM AnnexeJpaEntity a\n" +
            "INNER JOIN PaymentJpaEntity py ON py.annexeId = a.id\n" +
            "INNER JOIN AnnexeRequestJpaEntity ra ON ra.id = a.requestAnnexeId\n" +
            "INNER JOIN PaymentPlanJpaEntity pp on pp.paymentId = py.id\n" +
            "LEFT JOIN TransactionJpaEntity t on t.paymentPlanId = pp.id\n" +
            "WHERE ra.id = :requestAnnexeId AND py.total <= 0\n" +
            "AND ra.status = :status AND a.status = :status AND py.status = :status\n" +
            "AND pp.status = :status\n" +
            "ORDER BY ra.id DESC")
    List<Object> findPaymentDescByRequestAnnexeId(@Param("requestAnnexeId") Long requestAnnexeId,
                                                  @Param("requestAnnexeStatusPaid") Integer requestAnnexeStatusPaid,
                                                  @Param("status") Integer status);
}
