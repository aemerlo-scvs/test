package com.scfg.core.application.port.in;

import com.scfg.core.domain.Branch;

import java.util.List;

public interface BranchUseCase {
    List<Branch> getAllBranchs();
}
