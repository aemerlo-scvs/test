package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagementLog;

import java.util.List;

public interface CommercialManagementLogUseCase {
    PersistenceResponse save(CommercialManagementLog obj);
    List<CommercialManagementLog> getAllByCommercialManagmentId( Long idCommercialManagement);

}
