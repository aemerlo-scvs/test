package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.CommercialReportDTO;
import com.scfg.core.domain.dto.credicasas.SuscriptionReportDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;

import java.util.List;

public interface CLFReportPort {
    List getReport(SearchReportParamDTO searchReportParamDTO);
    List getComercialReport(SearchReportParamDTO searchReportParamDTO);
}
