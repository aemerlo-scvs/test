package com.scfg.core.adapter.persistence.sequencePolicy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SequencePolicyRepository extends JpaRepository<SequencePolicyJpaEntity, Long> {

    Optional<SequencePolicyJpaEntity> findByPolicyId(long policyId);
}
