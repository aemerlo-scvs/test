package com.scfg.core.domain.report;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JasperReportUtil {
    private static final Logger logger = Logger.getLogger(JasperReportUtil.class.getName());

    public JasperReportUtil() {
    }

    public static void compile(String key, byte[] content, Map<String, JasperReport> reportsCompiled) throws Exception {
        JasperReport jr = JasperCompileManager.compileReport(new ByteArrayInputStream(content));
        reportsCompiled.put(key.trim(), jr);
    }

    public static ByteArrayOutputStream generate(Map<String, JasperReport> reportsCompiled, List<SubReportDTO> subReportsDto) throws Exception {
        ByteArrayOutputStream outputStream;
        if (subReportsDto.size() > 1) {
            outputStream = generateFromSubReports(reportsCompiled, subReportsDto);
        } else {
            outputStream = generateFromSubReport(reportsCompiled, (SubReportDTO)subReportsDto.iterator().next());
        }

        return outputStream;
    }

    public static ByteArrayOutputStream generateExternal(Map<String, JasperReport> reportsCompiled, List<SubReportExternalDTO> subReportsDto) throws Exception {
        ByteArrayOutputStream outputStream;
        if (subReportsDto.size() > 1) {
            outputStream = generateFromSubReportsExternal(reportsCompiled, subReportsDto);
        } else {
            outputStream = generateFromSubReportExternal(reportsCompiled, (SubReportExternalDTO)subReportsDto.iterator().next());
        }

        return outputStream;
    }

    public static ByteArrayOutputStream generateFromSubReportExternal(Map<String, JasperReport> reportsCompiled, SubReportExternalDTO subReportDto) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperPrint jasperPrint = buildJasperPrint(reportsCompiled, subReportDto);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }

    private static ByteArrayOutputStream generateFromSubReportsExternal(Map<String, JasperReport> reportsCompiled, List<SubReportExternalDTO> subReportsDto) throws Exception {
        List<JasperPrint> jasperPrints = new ArrayList();
        Iterator var3 = subReportsDto.iterator();

        while(var3.hasNext()) {
            SubReportExternalDTO subReportDto = (SubReportExternalDTO)var3.next();
            jasperPrints.add(buildJasperPrint(reportsCompiled, subReportDto));
        }

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCreatingBatchModeBookmarks(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return outputStream;
    }

    private static ByteArrayOutputStream generateFromSubReport(Map<String, JasperReport> reportsCompiled, SubReportDTO subReportDto) throws Exception {
        JasperPrint jasperPrint = buildJasperPrint(reportsCompiled, subReportDto);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }

    private static ByteArrayOutputStream generateFromSubReports(Map<String, JasperReport> reportsCompiled, List<SubReportDTO> subReportsDto) throws Exception {
        List<JasperPrint> jasperPrints = new ArrayList();
        Iterator var3 = subReportsDto.iterator();

        while(var3.hasNext()) {
            SubReportDTO subReportDto = (SubReportDTO)var3.next();
            jasperPrints.add(buildJasperPrint(reportsCompiled, subReportDto));
        }

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCreatingBatchModeBookmarks(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return outputStream;
    }

    private static JRDataSource buildJRDataSource(List<Object> beans) {
        if (beans != null && !beans.isEmpty()) {
            Vector<Object> collection = new Vector();
            beans.stream().forEach((object) -> {
                collection.add(object);
            });
            return new JRBeanCollectionDataSource(collection);
        } else {
            return new JREmptyDataSource();
        }
    }

    private static JasperPrint buildJasperPrint(Map<String, JasperReport> reportsCompiled, SubReportDTO subReportDto) throws Exception {
        JasperReport jasperReport = (JasperReport)reportsCompiled.get(subReportDto.getResourceName());
        if (subReportDto.getEmbeddedResourcesName() != null && !subReportDto.getEmbeddedResourcesName().isEmpty()) {
            subReportDto.getEmbeddedResourcesName().forEach((embeddedResourceName) -> {
                subReportDto.getFixedParameters().put(embeddedResourceName, reportsCompiled.get(embeddedResourceName));
                logger.log(Level.INFO, "buildJasperPrint embeddedResourceName:" + embeddedResourceName);
            });
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, subReportDto.getFixedParameters(), buildJRDataSource(subReportDto.getBeans()));
        return jasperPrint;
    }

    private static JasperPrint buildJasperPrint(Map<String, JasperReport> reportsCompiled, SubReportExternalDTO subReportDto) throws Exception {
        HashMap<String, Object> fixedParametersForJasper = new HashMap();
        List<Object> beansForJasper = new ArrayList();
        JasperReport jasperReport = (JasperReport)reportsCompiled.get(subReportDto.getResourceName());
        if (subReportDto.getEmbeddedResourcesName() != null && !subReportDto.getEmbeddedResourcesName().isEmpty()) {
            subReportDto.getEmbeddedResourcesName().forEach((embeddedResourceName) -> {
                fixedParametersForJasper.put(embeddedResourceName, reportsCompiled.get(embeddedResourceName));
            });
        }

        if (subReportDto.getFixedParameters() != null && !subReportDto.getFixedParameters().isEmpty()) {
            resolveFixedParameters(subReportDto, fixedParametersForJasper);
        }

        if (subReportDto.getBeans() != null && !subReportDto.getBeans().isEmpty()) {
            resolveBeans(subReportDto, beansForJasper);
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, fixedParametersForJasper, buildJRDataSource(beansForJasper));
        return jasperPrint;
    }

    private static void resolveFixedParameters(SubReportExternalDTO subReportDto, HashMap<String, Object> fixedParametersForJasper) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Iterator var3 = subReportDto.getFixedParameters().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, SubReportContentDTO> entry = (Map.Entry)var3.next();
            fixedParametersForJasper.put(entry.getKey(), entry.getValue());
            if (((SubReportContentDTO)entry.getValue()).getContentImpl() != null && !((SubReportContentDTO)entry.getValue()).getContentImpl().isEmpty()) {
                fixedParametersForJasper.put(entry.getKey(), mapper.convertValue(((SubReportContentDTO)entry.getValue()).getContent(), Class.forName(((SubReportContentDTO)entry.getValue()).getContentImpl())));
            }
        }

    }

    private static void resolveBeans(SubReportExternalDTO subReportDto, List<Object> beansForJasper) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Iterator var3 = subReportDto.getBeans().iterator();

        while(true) {
            while(var3.hasNext()) {
                SubReportContentDTO subReportContentDto = (SubReportContentDTO)var3.next();
                if (subReportContentDto.getContentImpl() != null && !subReportContentDto.getContentImpl().isEmpty()) {
                    beansForJasper.add(mapper.convertValue(subReportContentDto.getContent(), Class.forName(subReportContentDto.getContentImpl())));
                } else {
                    beansForJasper.add(subReportContentDto.getContent());
                }
            }

            return;
        }
    }

    public static void generatePdf(Map<String, JasperReport> reportsCompiled, String name, Map<String, Object> parameters, JRDataSource ds, OutputStream out) throws Exception {
        generatePdf(out, fillReport(reportsCompiled, name, parameters, ds));
    }

    private static void generatePdf(OutputStream out, JasperPrint jp) throws Exception {
        JasperExportManager.exportReportToPdfStream(jp, out);
    }

    private static JasperPrint fillReport(Map<String, JasperReport> reportsCompiled, String name, Map<String, Object> parameters, JRDataSource data) throws Exception {
        return JasperFillManager.fillReport((JasperReport)reportsCompiled.get(name), parameters, data);
    }
}
