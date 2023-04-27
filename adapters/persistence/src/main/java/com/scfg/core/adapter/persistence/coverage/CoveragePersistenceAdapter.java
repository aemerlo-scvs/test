package com.scfg.core.adapter.persistence.coverage;

import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
@PersistenceAdapter
@RequiredArgsConstructor
public class CoveragePersistenceAdapter implements CoveragePort {

    private final CoverageRepository coverageRepository;
    private final EntityManager em;

    @Override
    public List<Coverage> getAllCoverage() {
        List<CoverageJpaEntity> coverageJpaEntities = coverageRepository.findAll();
        return mapListToDomain(coverageJpaEntities);
    }

    @Override
    public String getAllCoverageNamesByGeneralRequestId(Long requestId) {
        String query = coverageRepository.getFindAllCoverageNameByGeneralRequestIdQuery(requestId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<Object> coverageNames = em.createNativeQuery(query).getResultList();
        em.close();
        return (coverageNames.size() > 0)? coverageNames.get(0).toString() : "";
    }

    private List<Coverage> mapListToDomain(List<CoverageJpaEntity> coverageJpaEntities) {
        List<Coverage> coverages = new ArrayList<>();
        coverageJpaEntities.forEach(o -> {
            coverages.add(mapToDomain(o));
        });
        return coverages;
    }

    private Coverage mapToDomain(CoverageJpaEntity coverageJpaEntity){
        Coverage coverage = Coverage.builder()
                .name(coverageJpaEntity.getName())
                .coverageTypeIdc(coverageJpaEntity.getCoverageTypeIdc())
                .code(coverageJpaEntity.getCode())
                .id(coverageJpaEntity.getId())
                .branchId(coverageJpaEntity.getBranchId())
                .createdAt(coverageJpaEntity.getCreatedAt())
                .lastModifiedAt(coverageJpaEntity.getLastModifiedAt())
                .build();
        return coverage;
    }
}
