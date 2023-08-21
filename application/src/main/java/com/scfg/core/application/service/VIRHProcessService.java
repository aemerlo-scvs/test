package com.scfg.core.application.service;

import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import com.scfg.core.application.port.in.ReportServiceUseGeneric;
import com.scfg.core.application.port.in.VIRHUseCase;
import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.port.out.PolicyFileDocumentPort;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PDFMerger;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.PolicyFileDocument;
import com.scfg.core.domain.common.DocumentTemplate;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.application.service.sender.WhatsAppSenderService;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.MessageTypeEnum;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;
import java.util.Base64;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VIRHProcessService implements VIRHUseCase {
    private final DocumentTemplatePort documentTemplatePort;
    private final FileDocumentPort fileDocumentPort;
    private final PolicyFileDocumentPort policyFileDocumentPort;
    private final ReportServiceUseGeneric useGeneric;
    private final AlertService alertService;

    @PersistenceContext
    private EntityManager entityManager;


    private final SenderService senderService;
    private final WhatsAppSenderService whatsAppSenderService;

    @Override
    public String getDataInformationPolicy(String param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_view_data_policy_propose");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("param", param);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }

    @Override
    public FileDocumentDTO generate() throws IOException {
        String numberPolicy ="POL-SMVS-SC-13515-2022-00";
        Long productId=6L;
        List<String> listTo=new ArrayList<>();


        FileDocumentDTO file= null;
        try {
            file = generateAndSavePolicyPdf(numberPolicy,productId,listTo);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return  file;
    }

    private FileDocumentDTO generateAndSavePolicyPdf(String numberPolicy, Long productId, List<String> exclusionPdf) throws IOException, JRException {
        Long policyItem=0L;

        byte[] pdf;
        // 1.- obtener documentTemplate por productid
        List<DocumentTemplate> list = documentTemplatePort.getDocumentByProductId(productId);
        if (exclusionPdf.size() > 0) {
            list.removeIf(e -> exclusionPdf.contains(e.getDescription()));
        }
//        2.- Escoger templete dinamicos y generar uno a uno
        List<DocumentTemplate> listDocumentDynamics = list.stream().filter(x -> x.getIsDynamic() == true).collect(Collectors.toList());
//        2.1 sacar los datos para generar el reporte
        if (listDocumentDynamics.size() > 0) {
            for (DocumentTemplate sw : listDocumentDynamics) {
                switch (sw.getDocumentTypeIdc()) {
                    case 2: {
                        String json = getDjs(numberPolicy);//faltaria construir las consultas para generar los pdf dinamicos

                        Map<String, Object> map = (Map) (new Gson()).fromJson(json, HashMap.class);
                        String mainReport = "DJS";
                        Map<String, String> subreports = (Map) map.get("subreports");
                        List<Object> beans = new ArrayList<>(map.entrySet());
                        Map reportParameters = (Map) map.get("reportParameters");
                        List<DocumentTemplate> templateList = listReportAndSubReport(sw.getId(), list);
                        Map<String, JasperReport> jasperReportMap = loadReports(templateList);
                        byte[] pdc =  useGeneric.generatePdfByte(mainReport, false, beans, reportParameters, jasperReportMap);
                        sw.setContent(Base64.getEncoder().encodeToString(pdc));
                    }
                    case 4: {
                        String json = ""; //faltaria construir las consultas para generar los pdf dinamicos
                        Map<String, Object> map = (Map) (new Gson()).fromJson(json, HashMap.class);
                        String mainReport = (String) map.get("mainReport");
                        Map<String, String> subreports = (Map) map.get("subreports");
                        List<Object> beans = (List) map.get("reportBeansParams");
                        Map reportParameters = (Map) map.get("reportParameters");
                        List<DocumentTemplate> templateList = listReportAndSubReport(sw.getId(), list);
                        Map<String, JasperReport> jasperReportMap = loadReports(templateList);
                        byte[] pdc = useGeneric.generatePdfByte(mainReport, false, beans, reportParameters, jasperReportMap);
                        sw.setContent(Base64.getEncoder().encodeToString(pdc));
                    }
                    default: {

                    }
                }
            }
        }
//        3.- Unir los documents, en un solo documentos
        List<byte[]> listbytes = new ArrayList<>();
        for (DocumentTemplate dc : list) {
            byte[] myBytes = Base64.getDecoder().decode(dc.getContent());
            listbytes.add(myBytes);
        }
        try {
            pdf = PDFMerger.mergePDF(listbytes);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        4.- Guardar el documento */*/*  una ves que tiene unificado los documentos guardas y develvemos el documento
        String filename = numberPolicy + ".pdf";
        FileDocumentDTO fd = new FileDocumentDTO();
        fd.setContent(Base64.getEncoder().encodeToString(pdf));
        fd.setMime(HelpersConstants.PDF);
        fd.setName(filename);
        FileDocument fileDocument= saveFileDocument(numberPolicy,pdf);
        PolicyFileDocument policyFileDocument= savePolicyDocument(fileDocument.getId(),policyItem);
        return fd;
    }

    private PolicyFileDocument savePolicyDocument(Long id, Long policyItem) {
        PolicyFileDocument policyFileDocument= PolicyFileDocument.builder()
                .fileDocumentId(id)
                .policyItemId(policyItem).build();
        policyFileDocument=policyFileDocumentPort.saveOrUpdate(policyFileDocument);
        return policyFileDocument;
    }

    private FileDocument saveFileDocument(String numberPolicy,byte[] pdf) {
        FileDocument fileDocument = new FileDocument();
        fileDocument.setDescription(numberPolicy);
        fileDocument.setTypeDocument(2);
        fileDocument.setContent(Base64.getEncoder().encodeToString(pdf));
        fileDocument.setMime("application/pdf");
        fileDocument = this.fileDocumentPort.SaveOrUpdate(fileDocument);
        return fileDocument;
    }

    private List<DocumentTemplate> listReportAndSubReport(Long id, List<DocumentTemplate> list) {
        List<DocumentTemplate> newList = new ArrayList<>();
        newList.addAll(list.stream().filter(s -> s.getId() == id).collect(Collectors.toList()));
        newList.addAll(list.stream().filter(x -> x.getIdDocumentTemplate() == id).collect(Collectors.toList()));
        return newList;
    }

    private Map<String, JasperReport> loadReports(List<DocumentTemplate> list) throws JRException{

        Map<String, JasperReport> reportsCompiled = new HashMap<>();
        for (DocumentTemplate pq : list) {
            byte[] myBytes = Base64.getDecoder().decode(pq.getContent());
            InputStream repoInputStream = new ByteArrayInputStream(myBytes);
            JasperReport report = JasperCompileManager.compileReport(repoInputStream);
            reportsCompiled.put(pq.getDescription(), report);
            //                InputStream myInputStream = new ByteArrayInputStream(myBytes);

        }
        return reportsCompiled;
    }
    private  String getDjs(String numberPolicy){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_vrih_report_data_djs");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("param", numberPolicy);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }


    @Override
    public FileDocument getDocument(Long id) {
        try {
            FileDocument fileDocument = this.fileDocumentPort.findById(id);
            return fileDocument;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean sendWhatsApp(String number, String message, Long requestId) {
        this.senderService.setStrategy(this.whatsAppSenderService);
        MessageDTO messageDTO = getMessageDTO(number, message, "Envío notificación por WhatsApp", requestId);
        return this.senderService.sendMessage(messageDTO);
    }

    @Override
    public Boolean sendWhatsAppWithAttachment(String number, String message, Long requestId, Long docId) {
        this.senderService.setStrategy(this.whatsAppSenderService);
        MessageDTO messageDTO = getMessageDTO(number, message, "Envío notificación por WhatsApp con adjunto", requestId);

        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(docId+"");

        List<AttachmentDTO> list = new ArrayList<>();
        list.add(attachmentDTO);

        return this.senderService.sendMessageWithAttachment(messageDTO, list);
    }

    private static MessageDTO getMessageDTO(String number, String message, String subject, Long requestId) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(number);
        messageDTO.setMessage(message);
        messageDTO.setSubject(subject);
        messageDTO.setMessageTypeIdc(MessageTypeEnum.WHATSAPP.getValue());
        messageDTO.setReferenceId(requestId);
        messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);
        return messageDTO;
    }
}
