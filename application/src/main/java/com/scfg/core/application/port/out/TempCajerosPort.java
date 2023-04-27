package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.smvs.TempCajerosDto;

import java.util.List;

public interface TempCajerosPort {
    PersistenceResponse save(List<TempCajerosDto> tempCajerosDto);
    String getAsignacionCajerosPorCadaMes();
}
