package com.scfg.core.adapter.persistence.commercialManagementVIewWppSender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommercialManagementViewWppSenderRepository extends JpaRepository<CommercialManagementViewWppSenderJpaEntity, Long> {

    @Query("SELECT a FROM CommercialManagementViewWppSenderJpaEntity a " +
            "ORDER BY a.prioritySender ASC")
    @Override
    List<CommercialManagementViewWppSenderJpaEntity> findAll();
}
