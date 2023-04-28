package com.scfg.core.application.service;

import com.scfg.core.application.port.in.AnnexeTypeUseCase;
import com.scfg.core.application.port.out.AnnexeTypePort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.AnnexeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnexeTypeService implements AnnexeTypeUseCase {

    private final AnnexeTypePort annexeTypePort;

    @Override
    public AnnexeType getByProductIdAndInternalCode(Long productId, Long internalCode) {

        OperationException.throwExceptionIfNumberInvalid("Id producto", productId);
        OperationException.throwExceptionIfNumberInvalid("Código interno tipo anexo", internalCode);

        return annexeTypePort.findByProductIdAndInternalCode(productId, internalCode);
    }

}
