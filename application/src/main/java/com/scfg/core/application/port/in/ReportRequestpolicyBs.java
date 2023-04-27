package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.*;

import javax.mail.MessagingException;
import java.util.List;

public interface ReportRequestpolicyBs {
    FileDocumentDTOInf getReport(ReportRequestPolicyDTO reportRequestPolicyDTO);
    FileDocumentDTOInf getReports();
    List<VCMAReportDTO> getReportDates(ReportRequestPolicyDTO requestPolicyDTO);
    FileDocumentDTOInf getExcelSinGestoresConsolidados();
    List<ResultPolicyDtO> getSinGestoresConsolidados();
    int updatepolicymanager();
    boolean sendReportforMail(FileDocumentDTOMail fileDocumentDTOMail) throws MessagingException;


}
