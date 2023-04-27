package com.scfg.core.adapter.persistence.generalRequestAudit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralRequestAuditRepository extends JpaRepository<GeneralRequestAuditJpaEntity, Long> {
}
