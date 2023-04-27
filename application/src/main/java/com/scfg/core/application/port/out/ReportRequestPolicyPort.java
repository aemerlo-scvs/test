package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.*;

import javax.mail.MessagingException;
import java.util.List;

public interface ReportRequestPolicyPort {
    FileDocumentDTOInf getReport(ReportRequestPolicyDTO reportRequestPolicyDTO);
    FileDocumentDTOInf getReports();
    List<VCMAReportDTO> getReportDates(ReportRequestPolicyDTO requestPolicyDTO);
    List<ResultPolicyDtO> getConsolidado();
    FileDocumentDTOInf getExcelSinGestoresConsolidados();
    int updatepolicymanager();
    boolean sendReportforMail(FileDocumentDTOMail fileDocumentDTOMail) throws MessagingException;

}
