package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.VCMAManagerDTO;

import java.util.List;

public interface SantaCruzVCMAReportPort {

    boolean insert(List<VCMAManagerDTO> data) throws Exception;
}
