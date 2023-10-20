package com.scfg.core.application.port.in;

import com.scfg.core.common.exception.NotFileWriteReadException;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.report.ReportDTO;
import com.scfg.core.domain.report.ReportExternalDTO;
import net.sf.jasperreports.engine.JasperReport;

import java.util.List;
import java.util.Map;

public interface ReportServiceUseGeneric {
    FileDocumentDTO generatePdf(ReportDTO var1) throws Exception;

    FileDocumentDTO generatePdf(ReportExternalDTO var1) throws Exception;

    FileDocumentDTO generatePdf(String mainReport, Map<String, String> subreports, List<Object> beans, Map<String, Object> reportParameters) throws Exception;
    FileDocumentDTO generatePdf(String mainReport, Boolean subreports, List<Object> beans, Map<String, Object> reportParameters) throws Exception;
    byte[] generatePdfByte(String mainReport, Boolean subreports, List<Object> beans, Map<String, Object> reportParameters,Map<String, JasperReport>reportsCompiled) throws NotFileWriteReadException;
}
