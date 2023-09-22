package com.scfg.core.application.service;

import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import com.scfg.core.application.port.in.ReportServiceUseGeneric;
import com.scfg.core.application.port.in.VIRHUseCase;
import com.scfg.core.application.port.out.CommercialManagementViewWppSenderPort;
import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.port.out.PolicyFileDocumentPort;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PDFMerger;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.PolicyFileDocument;
import com.scfg.core.domain.common.DocumentTemplate;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.application.service.sender.WhatsAppSenderService;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import com.scfg.core.domain.dto.virh.CommercialManagementViewWppSenderDTO;
import com.scfg.core.domain.dto.virh.DebtRegisterUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.hibernate.type.NTextType;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import javax.persistence.*;
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
    private final CommercialManagementViewWppSenderPort cmWppPort;
    private final CommercialManagementService commercialManagementService;


    @PersistenceContext
    private EntityManager entityManager;


    private final SenderService senderService;
    private final WhatsAppSenderService whatsAppSenderService;
    private final Environment environment;

    private final String[] urls = new String[]{"https://www.santacruzvidaysalud.com.bo",
            "http://10.170.222.75:8081",
            "https://www.santacruzvidaysalud.com.bo"};

    private final Integer limitSenderPerDay = 10; // Limite diario de envios por WhatsApp - Default: 5000

    @Override
    public String getDataInformationPolicy(String param) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_view_virh_report_policy");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", NTextType.class, ParameterMode.OUT);
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
        file = generateAndSavePolicyPdf(numberPolicy,productId,listTo,1L);
        return  file;
    }

    @Override
    public String saveInformationPolicy(String data) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_virh_save_information_policy");
        query.registerStoredProcedureParameter("json_data", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("json_data", data);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");
        return result;

    }

    public String updateDebtRegisterQuery(DebtRegisterUpdateDTO data) {
        return "UPDATE DebtRegistry SET " +
                "collectionCode_lib='"+data.getCodigo_recaudacion()+ "' , " +
                "transactionId_lib='"+data.getId_transaccion()+"' , " +
                "qrUrl_lib='"+data.getQr_simple_url()+"' , " +
                "url_lib='"+data.getUrl_pasarela_pagos()+ "' "+
                "WHERE id='"+data.getId()+"'";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, NotDataFoundException.class, Exception.class})
    public void updateDebtRegister(DebtRegisterUpdateDTO data) {
        try {
            Query query = this.entityManager.createNativeQuery(updateDebtRegisterQuery(data));
            query.executeUpdate();
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

  //  public FileDocumentDTO generateAndSavePolicyPdf(String numberPolicy, Long productId, List<String> exclusionPdf) throws IOException, JRException {
    public FileDocumentDTO generateAndSavePolicyPdf(String numberPolicy, Long productId, List<String> exclusionPdf, Long policyItemId) {
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
                    case 1: {//condiciones particulares
                        String json = getConditionParticular(numberPolicy);//faltaria construir las consultas para generar los pdf dinamicos

                        Map<String, Object> map = (Map) (new Gson()).fromJson(json, HashMap.class);
                        String mainReport = sw.getDescription();
//                        Map<String, String> subreports = (Map) map.get("subreports");
//                        List<Object> beans = new ArrayList<>(map.entrySet());
//                        Map reportParameters = (Map) map.get("reportParameters");
                        List<DocumentTemplate> templateList = listReportAndSubReport(sw.getId(), list);
                        Map<String, JasperReport> jasperReportMap = null;
                        try {
                            jasperReportMap = loadReports(templateList);
                        } catch (JRException e) {
                            throw new RuntimeException(e);
                        }
                        byte[] pdc =  useGeneric.generatePdfByte(mainReport, false, null, map, jasperReportMap);
                        sw.setContent(Base64.getEncoder().encodeToString(pdc));
                        break;
                    }
                    case 5: {//information DJS
                        String json = getDjs(numberPolicy); //faltaria construir las consultas para generar los pdf dinamicos
                        Map<String, Object> map = (Map) (new Gson()).fromJson(json, HashMap.class);
                        String mainReport = sw.getDescription();//(String) map.get("mainReport");
//                        Map<String, String> subreports = (Map) map.get("subreports");
//                        List<Object> beans = (List) map.get("reportBeansParams");
//                        Map reportParameters = (Map) map.get("reportParameters");
                        List<DocumentTemplate> templateList = listReportAndSubReport(sw.getId(), list);
                        Map<String, JasperReport> jasperReportMap = null;
                        try {
                            jasperReportMap = loadReports(templateList);
                        } catch (JRException e) {
                            throw new RuntimeException(e);
                        }
                        byte[] pdc = useGeneric.generatePdfByte(mainReport, false, null, map, jasperReportMap);
                        sw.setContent(Base64.getEncoder().encodeToString(pdc));
                        break;
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
        fd.setId(fileDocument.getId());
        PolicyFileDocument policyFileDocument= savePolicyDocument(fileDocument.getId(),policyItemId);
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
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_virh_report_data_djs");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", NTextType.class, ParameterMode.OUT);
        query.setParameter("param", numberPolicy);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }
    private  String getConditionParticular(String numberPolicy){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_virh_report_data_condition_particular");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", NTextType.class, ParameterMode.OUT);
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
    public Boolean sendWhatsApp(String number, String message, Long requestId, Integer referecenteTableIdc) {
        this.senderService.setStrategy(this.whatsAppSenderService);
        MessageDTO messageDTO = getMessageDTO(number, message, "Envío notificación por WhatsApp", requestId, referecenteTableIdc);
        return this.senderService.sendMessage(messageDTO);
    }

    @Override
    public Boolean sendWhatsAppWithAttachment(String number, String message, Long requestId, Long docId, Integer referenceTableIdc) {
        this.senderService.setStrategy(this.whatsAppSenderService);

        MessageDTO messageDTO = getMessageDTO(number, message, "Envío notificación por WhatsApp con adjunto", requestId, referenceTableIdc);

        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(docId+"");

        List<AttachmentDTO> list = new ArrayList<>();
        list.add(attachmentDTO);

        return this.senderService.sendMessageWithAttachment(messageDTO, list);
    }

    @Override
    public String getWelcomeMessageText(String insuredName, String oldProduct)
    {
        List<String> arr = new ArrayList<>();
        arr.add(insuredName);
        arr.add(oldProduct);
        arr.add("62177077");
        Alert alert = alertService.getAlertByEnumReplacingContent(
                AlertEnum.VIRH_WELCOME,arr);
        return alert.getMail_body();
    }

    private static MessageDTO getMessageDTO(String number, String message, String subject, Long requestId, Integer referenceTableIdc) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(number);
        messageDTO.setMessage(message);
        messageDTO.setSubject(subject);
        messageDTO.setMessageTypeIdc(MessageTypeEnum.WHATSAPP.getValue());
        messageDTO.setReferenceId(requestId);
        messageDTO.setReferenceTableIdc(referenceTableIdc);
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);
        return messageDTO;
    }

    public void automaticSenderNotificationToRenew() {
        List<CommercialManagementViewWppSenderDTO> senders = this.cmWppPort.findAll();
        List<CommercialManagement> commercialManagementList = new ArrayList<>();
        int countSender = 0;
        for (CommercialManagementViewWppSenderDTO obj: senders) {
            if (countSender < this.limitSenderPerDay) {
                CommercialManagement cms = sendNotification(obj);
                if (cms != null) {
                    commercialManagementList.add(cms);
                }
                countSender++;
            } else {
                break;
            }
        }
        this.commercialManagementService.saveAll(commercialManagementList);
    }

    public Boolean manualSenderNotificationToRenew(Integer priority, Integer limitMessage) {
        List<CommercialManagementViewWppSenderDTO> senders = this.cmWppPort.findAll();
        List<CommercialManagementViewWppSenderDTO> sendersFilter = senders.stream().filter(x -> x.getPrioritySender() == priority).collect(Collectors.toList());
        List<CommercialManagement> commercialManagementList = new ArrayList<>();
        int countSender = 0;
        for (CommercialManagementViewWppSenderDTO obj: sendersFilter) {
            if (countSender < limitMessage) {
                CommercialManagement cms = sendNotification(obj);
                if (cms != null) {
                    commercialManagementList.add(cms);
                }
                countSender++;
            } else {
                break;
            }
        }
        return this.commercialManagementService.saveAll(commercialManagementList);
    }

    public void testWhatsAppSender(String number, String message, long docId) {
//        List<Alert> alertList = alertService.getAlertsByListId(
//                Arrays.asList(AlertEnum.VIRH_SCH_1.getValue(),AlertEnum.VIRH_SCH_2.getValue(),AlertEnum.VIRH_SCH_3.getValue(),
//                        AlertEnum.VIRH_SCH_4.getValue(),AlertEnum.VIRH_WELCOME.getValue()));
//        List<CommercialManagementViewWppSenderDTO> senders = this.cmWppPort.findAll();
//        List<CommercialManagement> commercialManagementList = new ArrayList<>();
//        int countSender = 0;
//        for (CommercialManagementViewWppSenderDTO obj: senders) {
//            List<Alert> auxAlertList = new ArrayList<>();
//            if (countSender < this.limitSenderPerDay && obj.getPrioritySender() == 1) {
//                auxAlertList.addAll(alertList);
//                CommercialManagement cms = sendNotification(obj);
//                if (cms != null) {
//                    commercialManagementList.add(cms);
//                }
//                countSender++;
//            } else {
//                break;
//            }
//        }
//        this.commercialManagementService.saveAll(commercialManagementList);
        List<String> arr = new ArrayList<>();
        arr.add("JUAN PABLO SUAREZ");
        arr.add("SEPELIO");
        arr.add("62177077");
        Alert alert = alertService.getAlertByEnumReplacingContent(
                AlertEnum.VIRH_WELCOME,arr);
        senderService.setStrategy(whatsAppSenderService);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(alert.getMail_body());
        messageDTO.setTo(number);
//        senderService.sendMessage(messageDTO);
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(docId+"");
        List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
        attachmentDTOList.add(attachmentDTO);
        senderService.sendMessageWithAttachment(messageDTO, attachmentDTOList);
    }

    private CommercialManagement sendNotification(CommercialManagementViewWppSenderDTO sender) {
        String bank = "Banco Fassil";
        CommercialManagement commercialManagement = null;
        String urlBase = "";
        String differenceDay = sender.getDateDifference() > 0 ? sender.getDateDifference() + "" : (sender.getDateDifference() * -1) + "";
        int changeStatus = 0;
        int changeSubStatus = 0;

        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            urlBase = urls[0];
        } else if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
            urlBase = urls[1];
        } else {
            urlBase = urls[2];
        }
        urlBase = urlBase + "/virh/scfg/subscription/" + UUID.fromString(sender.getUniqueCode().toString());
        Alert alert = new Alert();
        List<String> valuesToReplace = new ArrayList<>();
        String productName = "";
        if (sender.getNumberPolicy().toUpperCase().contains("SMVS")) {
            productName = "Sepelio Vida Segura";
        } else if (sender.getNumberPolicy().toUpperCase().contains("VIRH")) {
            productName = "Vinda Individual Renta Hospitalaria";
        } else {
            productName = "Vida + Cáncer";
        }
        if (sender.getPrioritySender() == PrioritySenderEnum.FIRST.getValue()) {

            valuesToReplace.add(sender.getInsured());
            valuesToReplace.add(productName);
            valuesToReplace.add(sender.getNumberPolicy());
            valuesToReplace.add(HelpersMethods.formatStringOnlyDate(sender.getStartOfCoverage()));
            valuesToReplace.add(differenceDay);
            valuesToReplace.add(urlBase);

            alert = this.alertService.getAlertByEnumReplacingContent(
                    AlertEnum.VIRH_SCH_1,valuesToReplace);

            changeStatus = (int) ClassifierEnum.CM_S_AUTOMATIC_CAMPAIGN.getReferenceCode();
            changeSubStatus = (int) ClassifierEnum.CM_AUTO_CAMPAIGN_C1_M2_1.getReferenceCode();
        } else if (sender.getPrioritySender() == PrioritySenderEnum.SECOND.getValue()) {

            valuesToReplace.add(sender.getInsured());
            valuesToReplace.add(productName);
            valuesToReplace.add(sender.getNumberPolicy());
            valuesToReplace.add(HelpersMethods.formatStringOnlyDate(sender.getStartOfCoverage()));
            valuesToReplace.add(urlBase);

            alert = this.alertService.getAlertByEnumReplacingContent(
                    AlertEnum.VIRH_SCH_2,valuesToReplace);

            changeStatus = (int) ClassifierEnum.CM_S_AUTOMATIC_CAMPAIGN.getReferenceCode();
            changeSubStatus = (int) ClassifierEnum.CM_AUTO_CAMPAIGN_C1_M3_10.getReferenceCode();
        } else if (sender.getPrioritySender() == PrioritySenderEnum.THIRD.getValue()) {

            valuesToReplace.add(sender.getInsured());
            valuesToReplace.add(productName);
            valuesToReplace.add(sender.getNumberPolicy());
            valuesToReplace.add(HelpersMethods.formatStringOnlyDate(sender.getStartOfCoverage()));
            valuesToReplace.add(urlBase);

            alert = this.alertService.getAlertByEnumReplacingContent(
                    AlertEnum.VIRH_SCH_3,valuesToReplace);

            changeStatus = (int) ClassifierEnum.CM_S_IN_COMMERCIAL_MANAGEMENT.getReferenceCode();
            changeSubStatus = (int) ClassifierEnum.CM_EGM_PENDING.getReferenceCode();
        } else if (sender.getPrioritySender() == PrioritySenderEnum.FOUR.getValue()) { //TODO Solo se tiene programado hasta prioridad de envio 4, en caso de existir otra prioridad adicional, esta debe de agregarse en la condicional
            valuesToReplace.add(sender.getInsured());
            valuesToReplace.add(productName);
            valuesToReplace.add(sender.getNumberPolicy());
            valuesToReplace.add(bank);
            valuesToReplace.add(urlBase);

            alert = this.alertService.getAlertByEnumReplacingContent(
                    AlertEnum.VIRH_SCH_4,valuesToReplace);
            changeStatus = (int) ClassifierEnum.CM_S_AUTOMATIC_CAMPAIGN.getReferenceCode();
            changeSubStatus = (int) ClassifierEnum.CM_AUTO_CAMPAIGN_C2_M2_10.getReferenceCode();
        } else if (sender.getPrioritySender() == PrioritySenderEnum.FIFTH.getValue()) {
            valuesToReplace.add(sender.getInsured());
            valuesToReplace.add(productName);
            valuesToReplace.add(urlBase);

            alert = this.alertService.getAlertByEnumReplacingContent(
                    AlertEnum.VIRH_SCH_5,valuesToReplace);
            changeStatus = (int) ClassifierEnum.CM_S_IN_COMMERCIAL_MANAGEMENT.getReferenceCode();
            changeSubStatus = (int) ClassifierEnum.CM_EGM_PENDING.getReferenceCode();
        }
        senderService.setStrategy(whatsAppSenderService);
        MessageDTO messageDTO = getMessageDTO(sender.getNumber(),alert.getMail_body(),alert.getMail_subject(),sender.getCommercialManagementId(), (int) ClassifierEnum.REFERENCE_TABLE_COMMERCIALMANAGEMENT.getReferenceCode());
//        MessageDTO messageDTO = getMessageDTO("79855300",alert.getMail_body(),alert.getMail_subject(),sender.getCommercialManagementId(), (int) ClassifierEnum.REFERENCE_TABLE_COMMERCIALMANAGEMENT.getReferenceCode());
        boolean canSend = senderService.sendMessage(messageDTO);
        if (canSend) {
            commercialManagement = this.commercialManagementService.findById(sender.getCommercialManagementId());
            commercialManagement.setManagementStatusIdc(changeStatus);
            commercialManagement.setManagementSubStatusIdc(changeSubStatus);
            commercialManagement.setMessageSentDate(new Date());
        }
        return commercialManagement;
    }

    @Override
    public String savePayment(String transactionId, String paymentMethod) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_virh_save_payment");
        query.registerStoredProcedureParameter("transactionId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("paymentMethod", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("transactionId", transactionId);
        query.setParameter("paymentMethod", paymentMethod);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }
}
