package com.scfg.core.application.service;

import com.scfg.core.application.port.in.BranchUseCase;
import com.scfg.core.application.port.out.BranchPort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Branch;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService implements BranchUseCase {
    private final BranchPort branchPort;
    @Override
    public List<Branch> getAllBranchs() {
        return branchPort.getAllBranch();
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public PersistenceResponse registerBranch(Branch branch) {
        return branchPort.save(branch, true);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public PersistenceResponse updateBranch(Branch branch)  {

        return branchPort.update(branch);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public PersistenceResponse deleteBranch(Long id) {
        return branchPort.delete(id);
    }



}
