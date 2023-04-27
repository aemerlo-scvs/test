package com.scfg.core.application.service;

import com.scfg.core.application.port.in.BranchUseCase;
import com.scfg.core.application.port.out.BranchPort;
import com.scfg.core.domain.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchSerive implements BranchUseCase {
    private final BranchPort branchPort;
    @Override
    public List<Branch> getAllBranchs() {
        return branchPort.getAllBranh();
    }
}
