package com.scfg.core.adapter.persistence.clause;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClauseRepository extends JpaRepository<ClauseJpaEntity, Long> {
    @Query("SELECT c FROM ClauseJpaEntity c " +
            "WHERE c.productId= :productId AND c.status = :status")
    List <ClauseJpaEntity> findAllByProductId(@Param("productId")Long productId, @Param("status") Integer status);

    @Modifying
    @Query("UPDATE ClauseJpaEntity c SET c.status = 0 " +
            "WHERE c.productId= :productId")
    void deleteByProductId(@Param("productId")Long productId);
}
