package com.scfg.core.adapter.persistence.CommercialManagementLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommercialManagementLogRepository extends JpaRepository<CommercialManagementLogJpaEntity, Long> {
    @Query("SELECT c FROM CommercialManagementLogJpaEntity c " +
            "WHERE c.idCommercialManagement= :commercialManagementId AND c.status = :status " +
            "ORDER BY c.createdAt DESC")
    List<CommercialManagementLogJpaEntity> getAllByCommercialManagementId(@Param("commercialManagementId") Long commercialManagementId,
                                                                          @Param("status") Integer status);

}
