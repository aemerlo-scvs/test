package com.scfg.core.adapter.persistence.coverage;

import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

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
    private ModelMapper modelMapper;

    @Override
    public PersistenceResponse saveOrUpdate(Coverage coverage) {
        CoverageJpaEntity coverageJpaEntity = ObjectMapperUtils.map(coverage, CoverageJpaEntity.class);
        coverageJpaEntity = coverageRepository.save(coverageJpaEntity);
        return new PersistenceResponse(
                CoveragePersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
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
        List<CoverageJpaEntity> coverageJpaEntities = coverageRepository.findAllCoverageByProductId(
                productId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return coverageJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(coverageJpaEntities, Coverage.class) : new ArrayList<>();
    }
    @Override
    public PersistenceResponse deleteByProductId(Long productId) {
        coverageRepository.deleteByProductId(productId);
        return new PersistenceResponse(
                Coverage.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
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

    private Coverage mapToDomain(CoverageJpaEntity obj) {
        Coverage coverage = modelMapper.map(obj, Coverage.class);
        return coverage;
    }

    private CoverageJpaEntity mapToJpaEntity(Coverage coverage) {
        return new ModelMapperConfig()
                .getStrictModelMapper()
                .map(coverage, CoverageJpaEntity.class);
    }

}
