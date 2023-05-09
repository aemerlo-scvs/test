package com.scfg.core.adapter.persistence.policyItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyItemRepository extends JpaRepository<PolicyItemJpaEntity,Long> {

    PolicyItemJpaEntity findByGeneralRequestId(Long generalRequestId);

    PolicyItemJpaEntity findByPolicyIdAndPersonId(Long policyId, Long personId);
}

