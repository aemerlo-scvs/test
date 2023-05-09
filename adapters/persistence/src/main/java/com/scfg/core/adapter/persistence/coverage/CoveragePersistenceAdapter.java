package com.scfg.core.adapter.persistence.coverage;

import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CoveragePersistenceAdapter implements CoveragePort {

    private final CoverageRepository coverageRepository;
    private EntityManager em;

    @Override
    public PersistenceResponse save(Coverage coverage, boolean returnEntity) {
        CoverageJpaEntity coverageJpaEntity = ObjectMapperUtils.map(coverage, CoverageJpaEntity.class); //mapDomainToEntity(coverage);
        coverageJpaEntity = coverageRepository.save(coverageJpaEntity);
        return new PersistenceResponse(
                CoveragePersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                coverageJpaEntity
        );

    }

//    private CoverageJpaEntity mapDomainToEntity(Coverage coverage) {
//        CoverageJpaEntity coverageJpaEntity = modelMapper.map(coverage, CoverageJpaEntity.class);
//        return coverageJpaEntity;
//    }

    @Override
    public PersistenceResponse update(Coverage coverage) {
        CoverageJpaEntity coverageJpaEntity = ObjectMapperUtils.map(coverage, CoverageJpaEntity.class);
        coverageJpaEntity = coverageRepository.save(coverageJpaEntity);
        return new PersistenceResponse(
                CoveragePersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                coverageJpaEntity
        );

    }

    @Override
    public PersistenceResponse delete(Long id) {
        Optional<CoverageJpaEntity> result = coverageRepository.findById(id);
        CoverageJpaEntity coverageJpaEntity = result.isPresent() ? result.get() : null;
        coverageJpaEntity.setStatus(0);
        coverageJpaEntity.setLastModifiedAt(new Date());
        coverageJpaEntity = coverageRepository.save(coverageJpaEntity);
        return new PersistenceResponse(
                CoveragePersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                coverageJpaEntity
        );
    }

    @Override
    public List<Coverage> getfilterParamenters(FilterParamenter parameter) {
        List<Coverage> list = getAllCoverage();
        return list;
    }

    @Override
    public List<Coverage> findAllCoverageByProductId(Long productId) {
        List<CoverageJpaEntity> coverageJpaEntities = coverageRepository.findAllCoverageByProductId(productId);
        return coverageJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(coverageJpaEntities, Coverage.class) : new ArrayList<>();
    }

    @Override
    public List<Coverage> getAllCoverage() {
        List<CoverageJpaEntity> coverageJpaEntities = coverageRepository.findAll();
        return coverageJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(coverageJpaEntities, Coverage.class) : new ArrayList<>();
    }

    @Override
    public String getAllCoverageNamesByGeneralRequestId(Long requestId) {
        String query = coverageRepository.getFindAllCoverageNameByGeneralRequestIdQuery(requestId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<Object> coverageNames = em.createNativeQuery(query).getResultList();
        em.close();
        return (coverageNames.size() > 0) ? coverageNames.get(0).toString() : "";
    }

//    private List<Coverage> mapListToDomain(List<CoverageJpaEntity> coverageJpaEntities) {
////        List<Coverage> coverages = new ArrayList<>();
////        coverageJpaEntities.forEach(o -> {
////            coverages.add(mapEntityToDomain(o));
////        });
//        //return coverages;
//
//        List<Coverage> coverageList = ObjectMapperUtils.mapAll(coverageJpaEntities, Coverage.class);
//        return coverageList;
//    }

//    private Coverage mapEntityToDomain(CoverageJpaEntity p) {
//        Coverage coverage = modelMapper.map(p, Coverage.class);
//        return coverage;
//    }
}
