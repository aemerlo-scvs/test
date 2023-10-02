package com.scfg.core.adapter.persistence.commecialManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CommercialManagementRepository extends JpaRepository<CommercialManagementJpaEntity, Long> {

    @Query("SELECT a FROM CommercialManagementJpaEntity a " +
            "WHERE a.id = :id AND a.status = :status")
    CommercialManagementJpaEntity findById(@Param("id") Long id, @Param("status") Integer status);
    @Query(value = " select case when count(c.id)> 0 then 1 else 0 end from CommercialManagement c where c.comercialManagementId= :comercialManagementId",nativeQuery = true )
    Integer existsByComercialManagementId(@Param("comercialManagementId") String comercialManagementId);

    @Query(value = "SELECT a.* FROM CommercialManagement a " +
            "WHERE a.comercialManagementId = :id AND a.status = :status", nativeQuery = true)
    CommercialManagementJpaEntity findByCommercialManagementId(@Param("id") UUID id, @Param("status") Integer status);

}
