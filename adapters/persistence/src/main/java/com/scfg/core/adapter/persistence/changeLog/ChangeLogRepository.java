package com.scfg.core.adapter.persistence.changeLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLogJpaEntity, Long> {

}
