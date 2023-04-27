package com.scfg.core.adapter.persistence.fabolousAccount.fabolousBeneficiary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FabolousBeneficiaryRepository extends JpaRepository<FabolousBeneficiaryJpaEntity, Long> {
    @Query("SELECT B FROM FabolousBeneficiaryJpaEntity B " +
            "WHERE B.status = 1")
    List<FabolousBeneficiaryJpaEntity> getAll();

    @Modifying
    @Query("update FabolousBeneficiaryJpaEntity u set u.status = :status where u.id = :id")
    void deleteBeneficiaryByReportId_DEPRECATED(@Param(value = "id") long id, @Param(value = "status") int status);
}
