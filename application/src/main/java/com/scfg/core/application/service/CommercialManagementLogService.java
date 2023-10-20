package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CommercialManagementLogUseCase;
import com.scfg.core.application.port.out.CommercialManagementLogPort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagementLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommercialManagementLogService implements CommercialManagementLogUseCase {


    private final CommercialManagementLogPort port;

    @Override
    public PersistenceResponse save(CommercialManagementLog obj) {
        PersistenceResponse response = port.save(obj, true);
        return response;
    }

    @Override
    public List<CommercialManagementLog> getAllByCommercialManagmentId(Long idCommercialManagement) {
        return port.getAllByCommercialManagementId(idCommercialManagement);

    }
}
