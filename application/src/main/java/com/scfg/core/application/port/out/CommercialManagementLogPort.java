package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagementLog;

import java.util.List;

public interface CommercialManagementLogPort {
    PersistenceResponse save(CommercialManagementLog obj, boolean returnEntity);
    List<CommercialManagementLog> getAllByCommercialManagementId(Long idCommercialManagement);

}
