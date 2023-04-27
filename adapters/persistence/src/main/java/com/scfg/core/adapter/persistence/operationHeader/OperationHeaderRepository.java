package com.scfg.core.adapter.persistence.operationHeader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationHeaderRepository extends JpaRepository<OperationHeaderJpaEntity, Long> {

    @Query("SELECT oh \n" +
            "FROM OperationHeaderJpaEntity oh \n" +
            "LEFT JOIN PolicyItemJpaEntity po ON po.id = oh.policyItemId \n" +
            "LEFT JOIN GeneralRequestJpaEntity gr ON gr.id = po.generalRequestId \n" +
            "WHERE gr.id = :generalRequestId \n" +
            "ORDER BY oh.createdAt DESC")
    List<OperationHeaderJpaEntity> findAllByGeneralRequestId(@Param("generalRequestId") Long generalRequestId);
}
