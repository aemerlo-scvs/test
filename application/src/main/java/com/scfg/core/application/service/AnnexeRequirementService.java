package com.scfg.core.application.service;

import com.scfg.core.application.port.in.AnnexeRequirementUseCase;
import com.scfg.core.application.port.out.AnnexeRequirementPort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.AnnexeRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnexeRequirementService implements AnnexeRequirementUseCase {
    private final AnnexeRequirementPort annexeRequirementPort;

    @Override
    public List<AnnexeRequirement> getAllByAnnexeTypeId(Long annexeTypeId) {
        if (annexeTypeId <= 0) {
            throw new OperationException("Id tipo de anexo, no valido");
        }

        return annexeRequirementPort.findAllByAnnexeTypeId(annexeTypeId);
    }
}
