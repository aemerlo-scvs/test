package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.VCMAReportDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportPort {
    List<VCMAReportDTO> getReportCommercials(Date dateTo, Date dateFrom);
    Map<String,Object> getRanking(Date startDate, Date toDate);
}
