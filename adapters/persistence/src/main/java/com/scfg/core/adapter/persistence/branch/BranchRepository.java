package com.scfg.core.adapter.persistence.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchRepository extends JpaRepository<BranchJpaEntity, Long> {
    @Query("SELECT b FROM BranchJpaEntity b " +
            "WHERE b.status = :status")
    List<BranchJpaEntity> findAll(@Param("status") Integer status);
}
