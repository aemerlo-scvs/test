package com.scfg.core.application.service;

import com.scfg.core.application.port.in.DocumentTemplateUse;
import com.scfg.core.application.port.in.ReportServiceUseGeneric;
import com.scfg.core.common.exception.NotFileWriteReadException;

import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.common.DocumentTemplate;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.report.EmptyReportBean;
import com.scfg.core.domain.report.JasperReportUtil;
import com.scfg.core.domain.report.ReportDTO;
import com.scfg.core.domain.report.ReportExternalDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceGeneric implements ReportServiceUseGeneric {

    private Map<String, JasperReport> reportsCompiled;
    private final DocumentTemplateUse templateUse;
    private static final String defaultFileName = "file_document.pdf";

    @Override
    public FileDocumentDTO generatePdf(ReportDTO reportDto) throws Exception {
        try {
            ByteArrayOutputStream outputStream = JasperReportUtil.generate(this.reportsCompiled, reportDto.getSubReportsDto());
            // FileDocumentType fileDocumentType = this.fileDocumentTypeModuleService.getFileDocumentTypeById(this.fileDocumentTypeConfiguration.defaultType());
            FileDocumentDTO fileDocumentDto = new FileDocumentDTO();
            fileDocumentDto.setName(reportDto.getFileName());
            fileDocumentDto.setContent(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
            fileDocumentDto.setMime(HelpersConstants.PDF);
            fileDocumentDto.setTypeId(5);
            return fileDocumentDto;
        } catch (Exception var5) {
            throw new NotFileWriteReadException("ReportServiceImpl generatePdf", var5);
        }
    }

    @Override
    public FileDocumentDTO generatePdf(ReportExternalDTO reportExternalDto) throws Exception {
        try {
            ByteArrayOutputStream outputStream = JasperReportUtil.generateExternal(this.reportsCompiled, reportExternalDto.getSubReports());
            FileDocumentDTO fileDocumentDto = new FileDocumentDTO();
            fileDocumentDto.setName(reportExternalDto.getFileName());
            fileDocumentDto.setContent(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
            fileDocumentDto.setMime(HelpersConstants.PDF);
            fileDocumentDto.setTypeId(5);
            return fileDocumentDto;
        } catch (Exception var5) {
            throw new NotFileWriteReadException("ReportServiceImpl generateExternalPdf", var5);
        }
    }

    @Override
    public FileDocumentDTO generatePdf(String mainReport, Map<String, String> subreports, List<Object> beans, Map<String, Object> reportParameters) throws Exception {
        loadReports(mainReport);
        if (beans == null) {
            beans = new ArrayList();
            ((List) beans).add(new EmptyReportBean());
        }

        ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
        JRDataSource ds = new JRBeanCollectionDataSource((Collection) beans);
        if (subreports != null && !subreports.isEmpty()) {
            if (reportParameters == null) {
                reportParameters = new HashMap();
            }

            Iterator var7 = subreports.keySet().iterator();

            while (var7.hasNext()) {
                String keySubr = (String) var7.next();
                ((Map) reportParameters).put(keySubr, this.reportsCompiled.get(subreports.get(keySubr)));
            }
        }

        try {
            JasperReportUtil.generatePdf(this.reportsCompiled, mainReport, (Map) reportParameters, ds, reportOutputStream);
            FileDocumentDTO fd = new FileDocumentDTO();
            fd.setContent(Base64.getEncoder().encodeToString(reportOutputStream.toByteArray()));
            fd.setMime(HelpersConstants.PDF);
            fd.setName(defaultFileName);
            fd.setTypeId(5);
            return fd;
        } catch (Exception var9) {
            throw new NotFileWriteReadException("ReportServiceImpl generatePdf", var9);
        }
    }

    @Override
    public FileDocumentDTO generatePdf(String mainReport, Boolean subreports, List<Object> beans, Map<String, Object> reportParameters) throws NotFileWriteReadException {
        loadReports(mainReport);
        if (beans == null) {
            beans = new ArrayList();
            ((List) beans).add(new EmptyReportBean());
        }

        ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
        JRDataSource ds = new JRBeanCollectionDataSource((Collection) beans);
        if (subreports) {
            if (reportParameters == null) {
                reportParameters = new HashMap();
            }

            Iterator var7 = reportsCompiled.keySet().iterator();

            while (var7.hasNext()) {
                String keySubr = (String) var7.next();
                if (!keySubr.equals(mainReport))
                    ((Map) reportParameters).put(keySubr, this.reportsCompiled.get(keySubr));
            }
        }

        try {
            JasperReportUtil.generatePdf(this.reportsCompiled, mainReport, (Map) reportParameters, ds, reportOutputStream);
            FileDocumentDTO fd = new FileDocumentDTO();
            fd.setContent(Base64.getEncoder().encodeToString(reportOutputStream.toByteArray()));
            fd.setMime(HelpersConstants.PDF);
            fd.setName(defaultFileName);
            log.info("Se ha generado correctamente el archivo pdf");
            return fd;
        } catch (Exception ex) {
            log.error("ReportServiceImpl generatePdf", ex);
            throw new NotFileWriteReadException("Error al leer la plantilla para generar el pdf", ex);
        }
    }
    public  byte[] generatePdfByte(String mainReport, Boolean subreports, List<Object> beans, Map<String, Object> reportParameters,Map<String, JasperReport>reportsCompiled) throws NotFileWriteReadException {
        if (beans == null) {
            beans = new ArrayList();
            ((List) beans).add(new EmptyReportBean());
        }

        ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
        JRDataSource ds = new JRBeanCollectionDataSource((Collection) beans);
        if (subreports) {
            if (reportParameters == null) {
                reportParameters = new HashMap();
            }

            Iterator var7 = reportsCompiled.keySet().iterator();

            while (var7.hasNext()) {
                String keySubr = (String) var7.next();
                if (!keySubr.equals(mainReport))
                    ((Map) reportParameters).put(keySubr, reportsCompiled.get(keySubr));
            }
        }

        try {
            JasperReportUtil.generatePdf(reportsCompiled, mainReport, (Map) reportParameters, ds, reportOutputStream);
            Base64.getEncoder().encodeToString(reportOutputStream.toByteArray());
            return reportOutputStream.toByteArray();

        } catch (Exception ex) {
            throw new NotFileWriteReadException(ex.getMessage());
        }
    }

    private void loadReports(String mainReport) throws NotFileWriteReadException {
        try {
            reportsCompiled = new HashMap<>();
            List<DocumentTemplate> list = new ArrayList<>();
            int i = 0;
            if (!mainReport.isEmpty() || mainReport != null)
                list = templateUse.getDocumentTemplateList(mainReport);
            for (DocumentTemplate pq : list) {
                InputStream repoInputStream= new FileInputStream(pq.getDocumentUrl());
                JasperReport report  = JasperCompileManager.compileReport(repoInputStream);
                this.reportsCompiled.put(pq.getDescription(), report);
            }

        } catch (Exception ex) {
            log.error("No se pudo cargar o leer los archivo del reporte", ex);
            throw new NotFileWriteReadException("No se pudo cargar o leer los archivo del reporte", ex);
        }

    }
}
