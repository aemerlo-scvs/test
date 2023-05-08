package com.scfg.core.adapter.persistence.clause;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClauseRepository extends JpaRepository<ClauseJpaEntity, Long> {
    List <ClauseJpaEntity> findAllByProductId(Long productId);
}
