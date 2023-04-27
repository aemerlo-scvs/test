package com.scfg.core.application.port.in;


import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.ReportRequestPolicyDTO;
import com.scfg.core.domain.dto.VCMAManagerDTO;
import com.scfg.core.domain.dto.VCMAReportDTO;

import java.util.List;

public interface SantaCruzVCMAReport {
    Boolean insertManagers(List<VCMAManagerDTO> data) throws Exception;
}
