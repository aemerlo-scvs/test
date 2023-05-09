package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Branch;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;

import java.util.List;

public interface BranchUseCase {
    List<Branch> getAllBranchs();
    PersistenceResponse registerBranch(Branch branch);
    PersistenceResponse updateBranch(Branch branch) ;
    PersistenceResponse deleteBranch(Long  id) ;
    List<Branch> getfilterParamenter(FilterParamenter paramenter);
    //f
}
