package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.domain.dto.ResultPolicyDtO;
import com.scfg.core.domain.dto.VCMAReportDTO;

import java.util.Date;
import java.util.List;

public interface ReportRequestPolicyRepository {
    List<VCMAReportDTO> GetAllDataForDates(Date start, Date finish);
    List<VCMAReportDTO> GetAllDataAcumulado(Date start, Date finish);
    List<ResultPolicyDtO>SetConsolidarPolizas();
    int UpdatePolizas();
}
