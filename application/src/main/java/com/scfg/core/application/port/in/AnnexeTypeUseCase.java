package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.AnnexeType;

public interface AnnexeTypeUseCase {
    AnnexeType getByProductIdAndInternalCode(Long productId, Long internalCode);
}
