package com.scfg.core.adapter.persistence.accountPolicy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountPolicyRepository extends JpaRepository<AccountPolicyJpaEntity,Long> {

    @Query("SELECT a " +
            "FROM AccountPolicyJpaEntity a \n" +
            "WHERE a.policyId = :policyId AND a.status = :statusIdc")
    AccountPolicyJpaEntity findByPolicyId(@Param("policyId") Long policyId,
                                                       @Param("statusIdc") Integer statusIdc);

    @Query("SELECT a " +
            "FROM AccountPolicyJpaEntity a \n" +
            "WHERE a.accountId = :accountId AND a.policyId = :policyId AND a.status = :statusIdc")
    AccountPolicyJpaEntity findByAccountIdAAndPolicyId(@Param("accountId") Long accountId,
                                                       @Param("policyId") Long policyId,
                                                       @Param("statusIdc") Integer statusIdc);
}
