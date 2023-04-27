package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.smvs.ParametersFromDTO;
import com.scfg.core.domain.smvs.SMVSReportDTO;
import com.scfg.core.domain.smvs.TempCajerosDto;

import java.util.Date;
import java.util.List;

public interface SMVSUseReport {
 PageableDTO getAllSMVSRequestByPage(ParametersFromDTO filters);

 FileDocumentDTO getReportSMVSFileExcel(Date startDate, Date toDate, Integer statusRequest);

 FileDocumentDTOInf ReportSUMSReportCommercials(ReportRequestPolicyDTO reportRequestPolicyDTO);
 FileDocumentDTOInf ReportSUMSReportCommercialsNew(ReportRequestPolicyDTO reportRequestPolicyDTO);

 PersistenceResponse savecajeros(List<TempCajerosDto>tempCajerosDtoList);

 List<DetailsLoadCashiers1> getDetailLoadCashiers();

 FileDocumentDTO getFormatLoadFileSales();


}
