package com.scfg.core.adapter.persistence.annexeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnnexeTypeRepository extends JpaRepository<AnnexeTypeJpaEntity, Long> {

    @Query(value = "SELECT p FROM AnnexeTypeJpaEntity p " +
            "WHERE p.id = :annexeTypeId AND p.status = :status")
    Optional<AnnexeTypeJpaEntity> findOptionalById(@Param("annexeTypeId") Long annexeTypeId, @Param("status") Integer status);

    AnnexeTypeJpaEntity findByProductIdAndInternalCodeAndStatus(Long productId, Long internalCode, Integer status);
}
