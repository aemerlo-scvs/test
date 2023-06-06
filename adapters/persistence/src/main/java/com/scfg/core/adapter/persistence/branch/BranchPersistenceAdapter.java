package com.scfg.core.adapter.persistence.branch;

import com.scfg.core.application.port.out.BranchPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;
import com.scfg.core.domain.Branch;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class BranchPersistenceAdapter implements BranchPort {

    private final BranchRepository branchRepository;
    private ModelMapper modelMapper;

    @Override
    public PersistenceResponse save(Branch branch, boolean returnEntity) {
        BranchJpaEntity branchJpaEntity = mapToJpaEntity(branch);
        branchJpaEntity = branchRepository.save(branchJpaEntity);
        return new PersistenceResponse(
                Branch.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                branchJpaEntity
        );
    }

    @Override
    public PersistenceResponse update(Branch branch) {
        Optional<BranchJpaEntity> branchJpaEntity = branchRepository.findById(branch.getId());
        BranchJpaEntity branchJpaEntity1 = branchJpaEntity.isPresent() ? branchJpaEntity.get() : null;
        branchJpaEntity1.setName(branch.getName());
        branchJpaEntity1.setDescription(branch.getDescription());
        branchJpaEntity1.setLastModifiedAt(new Date());
        branchJpaEntity1.setStatus(branch.getStatus());
        branchJpaEntity1.setModalityIdc(branch.getModalityIdc());
        branchRepository.save(branchJpaEntity1);
        return new PersistenceResponse(
                Branch.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                branchJpaEntity1
        );
    }

    @Override
    public PersistenceResponse delete(Long id) {
        Optional<BranchJpaEntity> branchJpaEntity = branchRepository.findById(id);
        BranchJpaEntity branchJpaEntity1 = branchJpaEntity.isPresent() ? branchJpaEntity.get() : null;
        branchJpaEntity1.setLastModifiedAt(new Date());
        branchJpaEntity1.setStatus(0);
        branchRepository.save(branchJpaEntity1);
        return new PersistenceResponse(
                Branch.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                branchJpaEntity1
        );

    }
    public List<Branch> getAllBranch() {
        List<BranchJpaEntity> branchJpaEntities  = branchRepository.findAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return branchJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(branchJpaEntities, Branch.class) : new ArrayList<>();
    }

    @Override
    public List<Branch> getfilterParamenters(FilterParamenter paramenter) {
        List<BranchJpaEntity> list = branchRepository.findAll();
        List<Branch>list1=listMap(list);
        return list1;
    }

    private List<Branch> listMap(List<BranchJpaEntity> list) {
        List<Branch> branchList = new ArrayList();
        for (BranchJpaEntity obj : list) {
            branchList.add(mapperEntityToDto(obj));
        }
        return branchList;
    }

    private Branch mapperEntityToDto(BranchJpaEntity obj) {
        Branch branch=modelMapper.map(obj,Branch.class);
        return branch;
    }

    private BranchJpaEntity mapToJpaEntity(Branch branch) {
        return new ModelMapperConfig()
                .getStrictModelMapper()
                .map(branch, BranchJpaEntity.class);

    }
}
