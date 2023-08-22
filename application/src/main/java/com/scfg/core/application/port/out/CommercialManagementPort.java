package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;

public interface CommercialManagementPort {
    PersistenceResponse save(CommercialManagement obj, boolean returnEntity);

    PersistenceResponse update(CommercialManagement obj);



}
