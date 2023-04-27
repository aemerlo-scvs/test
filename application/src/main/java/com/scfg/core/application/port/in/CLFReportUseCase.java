package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.ClfProcessRequestDTO;
import com.scfg.core.domain.dto.credicasas.SuscriptionReportDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;

import java.util.List;

public interface CLFReportUseCase {
    ClfProcessRequestDTO getReportData(SearchReportParamDTO searchReportParamDTO);
    ClfProcessRequestDTO getComercialReportData(SearchReportParamDTO searchReportParamDTO);
}
