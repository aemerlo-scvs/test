package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Branch;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;

import java.util.List;

public interface BranchPort {
    List<Branch> getAllBranh();
    PersistenceResponse save(Branch branch, boolean returnEntity);

    PersistenceResponse update(Branch branch);

    PersistenceResponse delete(Long  id);


    List<Branch> getfilterParamenters(FilterParamenter paramenter);
}
