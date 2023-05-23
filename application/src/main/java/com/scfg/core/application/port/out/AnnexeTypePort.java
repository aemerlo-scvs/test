package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.AnnexeType;

import java.util.List;

public interface AnnexeTypePort {

    List<AnnexeType> findAll();
    AnnexeType findByIdOrExcepcion(Long annexeTypeId);
    AnnexeType findByProductIdAndInternalCode(Long productId, Long internalCode);

}
