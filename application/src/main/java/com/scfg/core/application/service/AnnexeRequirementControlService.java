package com.scfg.core.application.service;

import com.scfg.core.application.port.in.AnnexeRequirementControlUseCase;
import com.scfg.core.application.port.out.AnnexeRequirementControlPort;

import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnexeRequirementControlService implements AnnexeRequirementControlUseCase {

    private final AnnexeRequirementControlPort annexeRequirementControlPort;

    @Override
    public List<AnnexeRequirementDto> getAllByRequestAnnexeIdAndAnnexeTypeId(Long requestAnnexeId, Long annexeTypeId) {
        return annexeRequirementControlPort.findAllByRequestAnnexeIdAndAnnexeTypeId(requestAnnexeId);
    }
}
