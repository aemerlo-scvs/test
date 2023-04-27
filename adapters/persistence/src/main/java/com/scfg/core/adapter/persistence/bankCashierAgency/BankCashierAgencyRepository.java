package com.scfg.core.adapter.persistence.bankCashierAgency;

import com.scfg.core.domain.dto.DetailsLoadCashiers;
import com.scfg.core.domain.dto.DetailsLoadCashiers1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankCashierAgencyRepository extends JpaRepository<BankCashierAgencyJpaEntity,Long> {

    @Query(value = "SELECT v.courtDate As courtDate,v.createdBy as createdBy, v.createdAt AS createdAt,COUNT(v.id) AS cant " +
            " FROM bankCashierAgency v " +
            " GROUP BY v.courtDate,v.createdBy, v.createdAt",nativeQuery = true)
    List<DetailsLoadCashiers1> findByDetailsLoadCashiers();
}
