package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;

import java.util.List;


public interface CommercialManagementUseCase {


    PersistenceResponse save(CommercialManagement obj);
    PersistenceResponse update(CommercialManagement obj);
    CommercialManagement getById(Long id);
    List<CommercialManagementDTO> search(CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto);

}
