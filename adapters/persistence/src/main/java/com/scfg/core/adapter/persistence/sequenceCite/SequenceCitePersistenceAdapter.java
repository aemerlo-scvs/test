package com.scfg.core.adapter.persistence.sequenceCite;

import com.scfg.core.adapter.persistence.sequencePolicy.SequencePolicyJpaEntity;
import com.scfg.core.application.port.out.SequenceCitePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.SequenceCite;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class SequenceCitePersistenceAdapter implements SequenceCitePort {

    private final SequenceCiteRepository sequenceCiteRepository;

    @Override
    public SequenceCite getSequence(long companyIdc, String year) {
        SequenceCiteJpaEntity sequenceCiteJpaEntity = sequenceCiteRepository.findByCompanyIdcAndAndYear(companyIdc, year);
        return sequenceCiteJpaEntity != null ? mapToDomain(sequenceCiteJpaEntity) : null;
    }

    @Override
    public long saveOrUpdate(SequenceCite sequenceCite) {
        SequenceCiteJpaEntity sequenceCiteJpaEntity = mapToJpaEntity(sequenceCite);
        sequenceCiteJpaEntity = sequenceCiteRepository.save(sequenceCiteJpaEntity);
        return sequenceCiteJpaEntity.getId();
    }

    public static SequenceCite mapToDomain (SequenceCiteJpaEntity sequenceCiteJpaEntity) {
        SequenceCite sequenceCite = SequenceCite.builder()
                .id(sequenceCiteJpaEntity.getId())
                .citeNumber(sequenceCiteJpaEntity.getCiteNumber())
                .companyIdc(sequenceCiteJpaEntity.getCompanyIdc())
                .year(sequenceCiteJpaEntity.getYear())
                .build();
        return sequenceCite;
    }

    public static SequenceCiteJpaEntity mapToJpaEntity (SequenceCite sequenceCite) {
        SequenceCiteJpaEntity sequenceCiteJpaEntity = SequenceCiteJpaEntity.builder()
                .id(sequenceCite.getId())
                .citeNumber(sequenceCite.getCiteNumber())
                .companyIdc(sequenceCite.getCompanyIdc())
                .year(sequenceCite.getYear())
                .createdAt(sequenceCite.getCreatedAt())
                .lastModifiedAt(sequenceCite.getLastModifiedAt())
                .createdBy(sequenceCite.getCreatedBy())
                .lastModifiedBy(sequenceCite.getLastModifiedBy())
                .build();
        return sequenceCiteJpaEntity;
    }
}
