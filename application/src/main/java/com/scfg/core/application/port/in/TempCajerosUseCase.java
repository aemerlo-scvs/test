package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.smvs.TempCajerosDto;

import java.util.List;

public interface TempCajerosUseCase {
    PersistenceResponse save(List<TempCajerosDto> tempCajerosDtoList);
}
