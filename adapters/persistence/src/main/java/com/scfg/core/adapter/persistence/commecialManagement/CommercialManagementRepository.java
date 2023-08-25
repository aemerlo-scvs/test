package com.scfg.core.adapter.persistence.commecialManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommercialManagementRepository extends JpaRepository<CommercialManagementJpaEntity, Long> {

    @Query("SELECT a FROM CommercialManagementJpaEntity a " +
            "WHERE a.id = :id AND a.status = :status")
    CommercialManagementJpaEntity findById(@Param("id") Long id, @Param("status") Integer status);
}
