package com.scfg.core.adapter.persistence.policyItemEconomic;

import com.scfg.core.adapter.persistence.account.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyItemEconomicRepository extends JpaRepository<PolicyItemEconomicJpaEntity, Long> {

    @Query("SELECT pie \n" +
            "FROM PolicyItemEconomicJpaEntity pie \n" +
            "WHERE pie.policyItemId = :policyItemId AND pie.movementTypeIdc = :movementTypeIdc AND pie.status = :status \n" +
            "ORDER BY pie.id DESC")
    List<PolicyItemEconomicJpaEntity> findAllByPolicyItemIdAndMovementTypeIdc(@Param("policyItemId") Long policyItemId,
                                                                              @Param("movementTypeIdc") Integer movementTypeIdc,
                                                                              @Param("status") Integer status);

    @Query("SELECT pie \n" +
            "FROM PolicyItemEconomicJpaEntity pie \n" +
            "WHERE pie.policyItemId = :policyItemId AND pie.movementTypeIdc = :movementTypeIdc \n" +
            "ORDER BY pie.id DESC")
    List<PolicyItemEconomicJpaEntity> findAllByPolicyItemIdAndMovementTypeIdc(@Param("policyItemId") Long policyItemId,
                                                                              @Param("movementTypeIdc") Integer movementTypeIdc);

}
