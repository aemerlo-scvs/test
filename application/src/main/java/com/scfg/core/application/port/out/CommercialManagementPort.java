package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;

import java.util.List;
import java.util.UUID;

public interface CommercialManagementPort {
    PersistenceResponse save(CommercialManagement obj, boolean returnEntity);

    PersistenceResponse update(CommercialManagement obj);

    CommercialManagement findById(Long id);

    CommercialManagement findByCommercialManagementId(UUID id);

    Boolean saveAll(List<CommercialManagement> commercialManagementList);
    boolean existsComercialManagementId(String comercialManagementId);
}
