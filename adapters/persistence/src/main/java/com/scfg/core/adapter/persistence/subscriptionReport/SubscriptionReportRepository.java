package com.scfg.core.adapter.persistence.subscriptionReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface SubscriptionReportRepository extends JpaRepository<SubscriptionReportJpaEntity, Long> {

    Optional<SubscriptionReportJpaEntity> findByHdocumentNumberAndDoperationNumber(String ci, String operationNumber);

    List<SubscriptionReportJpaEntity> findAllByHdocumentNumberAndDoperationNumber(String ci, String operationNumber);
}
