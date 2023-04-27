package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.FabolousDTO;

import java.util.List;

public interface FabolousBranch {
    long save(List<FabolousDTO> fabolousDTOS);
}
