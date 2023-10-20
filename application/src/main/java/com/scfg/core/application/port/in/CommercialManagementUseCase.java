package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;

import java.util.List;


public interface CommercialManagementUseCase {


    PersistenceResponse save(CommercialManagement obj);

    PersistenceResponse update(CommercialManagement obj);

    PersistenceResponse updateSomeFields(CommercialManagement obj);

    PersistenceResponse updateStatusAndSubstatus(CommercialManagement obj);

    List<CommercialManagementDTO> search(CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto);

    CommercialManagement findById(Long id);

    Boolean saveAll(List<CommercialManagement> commercialManagementList);

    Boolean existsComercialManagementId(String comercialManagementId);

}
