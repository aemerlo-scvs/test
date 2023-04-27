package com.scfg.core.adapter.persistence.sequencePolicy;

import com.scfg.core.application.port.out.SequencePolicyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.SequencePolicy;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class SequencePolicyPersistenceAdapter implements SequencePolicyPort {

    private final SequencePolicyRepository sequencePolicyRepository;

    @Override
    public SequencePolicy getSequence(long policyId) {
        Optional<SequencePolicyJpaEntity> sequencePolicyJpaEntity = sequencePolicyRepository.findByPolicyId(policyId);
        return sequencePolicyJpaEntity.isPresent() ? mapToDomain(sequencePolicyJpaEntity.get()) : null;
    }

    @Override
    public long saveOrUpdate(SequencePolicy sequencePolicy) {
        SequencePolicyJpaEntity sequencePolicyJpaEntity = mapToJpaEntity(sequencePolicy);
        sequencePolicyJpaEntity = sequencePolicyRepository.save(sequencePolicyJpaEntity);
        return sequencePolicyJpaEntity.getId();
    }

    public static SequencePolicy mapToDomain (SequencePolicyJpaEntity sequencePolicyJpaEntity) {
        SequencePolicy sequencePolicy = SequencePolicy.builder()
                .policyId(sequencePolicyJpaEntity.getPolicyId())
                .requestNumber(sequencePolicyJpaEntity.getRequestNumber())
                .coverageCertificateNumber(sequencePolicyJpaEntity.getCertificateCoverageNumber())
                .id(sequencePolicyJpaEntity.getId())
                .build();

        return  sequencePolicy;
    }

    public static SequencePolicyJpaEntity mapToJpaEntity (SequencePolicy sequencePolicy) {
        SequencePolicyJpaEntity sequencePolicyJpaEntity = SequencePolicyJpaEntity.builder()
                .id(sequencePolicy.getId())
                .policyId(sequencePolicy.getPolicyId())
                .requestNumber(sequencePolicy.getRequestNumber())
                .createdAt(sequencePolicy.getCreatedAt())
                .lastModifiedAt(sequencePolicy.getLastModifiedAt())
                .certificateCoverageNumber(sequencePolicy.getCoverageCertificateNumber())
                .build();

        return sequencePolicyJpaEntity;
    }
}
