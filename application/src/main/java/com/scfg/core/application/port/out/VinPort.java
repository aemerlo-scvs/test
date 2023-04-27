package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;

import java.util.List;

public interface VinPort {
    List<Object> getProductionReport(VinReportFilterDTO filterDTO);
    List<Object> getCommercialReport(VinReportFilterDTO filterDTO);
}
