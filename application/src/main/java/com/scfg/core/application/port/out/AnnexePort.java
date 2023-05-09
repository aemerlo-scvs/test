package com.scfg.core.application.port.out;


import com.scfg.core.domain.dto.RequestAnnexeCancelaltionDto;
import com.scfg.core.domain.dto.vin.AnnexeDTO;

import java.util.List;

public interface AnnexePort {
    Long saveOrUpdate(RequestAnnexeCancelaltionDto annexe, Long requestAnnexeId);
    List<AnnexeDTO> getAllAnnexeByAnnexeTypeAndRequestAnnexeId(Long annexeType, Long requestAnnexeId);
    AnnexeDTO findAnnexeByIdOrThrowExcepcion(Long annexeId);
}
