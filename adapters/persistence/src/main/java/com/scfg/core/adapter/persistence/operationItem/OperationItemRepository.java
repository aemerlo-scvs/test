package com.scfg.core.adapter.persistence.operationItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationItemRepository extends JpaRepository<OperationItemJpaEntity, Long> {
    OperationItemJpaEntity findFirstByOperationHeaderIdAndMonthIdcAndYearIdc(long operationHeaderId, int monthIdc, int yearIdc);
}
