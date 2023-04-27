package com.scfg.core.adapter.persistence.branch;

import com.scfg.core.application.port.out.BranchPort;
import com.scfg.core.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import com.scfg.core.domain.Branch;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class BranchPersistenceAdapter implements BranchPort {

    private final BranchRepository branchRepository;

    @Override
    public List<Branch> getAllBranh() {
        Object list = branchRepository.findAll();
        return (List<Branch>) list;
    }
}
