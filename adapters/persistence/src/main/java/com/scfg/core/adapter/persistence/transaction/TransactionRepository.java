package com.scfg.core.adapter.persistence.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {
}
