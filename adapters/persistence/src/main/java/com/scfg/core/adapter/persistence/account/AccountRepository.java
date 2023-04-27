package com.scfg.core.adapter.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {

    @Query("SELECT a " +
            "FROM AccountJpaEntity a \n" +
            "WHERE a.personId = :personId AND a.status = :status \n" +
            "ORDER BY a.id DESC")
    List<AccountJpaEntity> findAllByPersonId(@Param("personId") Long personId, @Param("status") Integer status);

    @Query("SELECT a \n" +
            "FROM AccountJpaEntity a \n" +
            "LEFT JOIN AccountPolicyJpaEntity ap ON ap.accountId = a.id \n" +
            "WHERE a.personId = :personId AND ap.policyId = :policyId AND a.status = :status AND ap.status = :status \n" +
            "ORDER BY a.id DESC")
    List<AccountJpaEntity> findAllByPersonIdAndPolicyId(@Param("personId") Long personId, @Param("policyId") Long policyId,
                                                        @Param("status") Integer status);

}
