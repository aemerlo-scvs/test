package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;


public interface CommercialManagementUseCase {


    PersistenceResponse save(CommercialManagement obj);
    PersistenceResponse update(CommercialManagement obj);
    PersistenceResponse getAll();
    PersistenceResponse getAllByFilters(CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto );
    PersistenceResponse findByPolicyId(Long policyId);

}
