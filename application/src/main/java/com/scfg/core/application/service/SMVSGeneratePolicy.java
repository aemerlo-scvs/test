package com.scfg.core.application.service;

import com.google.gson.Gson;
import com.scfg.core.application.port.in.SMVSUseGeneratePolicy;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.NotFileWriteReadException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.person.NaturalPerson;
import com.scfg.core.domain.report.sepelio.ReportBeneficiaries;
import com.scfg.core.domain.report.sepelio.ReportLegals;
import com.scfg.core.domain.report.sepelio.Reportbean;
import com.scfg.core.domain.smvs.PendingActivateErrorDTO;
import com.scfg.core.domain.smvs.SavePolicyDTO;
import com.scfg.core.domain.smvs.SendMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMVSGeneratePolicy implements SMVSUseGeneratePolicy {
    private final ClassifierPort classifierPort;
    private final ProductPort productPort;
    private final PolicyPort policyPort;
    private final DocumentPort documentPort;
    private final BeneficiaryPort beneficiaryPort;
    private final GeneralRequestPort generalRequestPort;
    private final PlanPort planPort;
    private final ReportServiceGeneric reportServiceGeneric;
    private final ReceiptPort receiptPort;
    private final SMVSCommonService smvsCommonService;
    private final FileDocumentPort fileDocumentPort;
    private final PolicyFileDocumentPort policyFileDocumentPort;
    private final PolicyItemPort policyItemPort;
    private final CoverageProductPlanPort coverageProductPlanPort;
    private final CoveragePolicyItemPort coveragePolicyItemPort;
    private final GenerateReportsService generateReportsServiceExcel;
    private final AlertPort alertPort;
    private final EmailService emailService;
    @Autowired
    MyProperties path;
    private final ProductCalculationsService productCalculationsService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NotFileWriteReadException.class, Exception.class})
    @Override
    public FileDocumentDTO generatePolicy(SavePolicyDTO o) {
        GeneralRequest generalRequestAux = generalRequestPort.getGeneralRequest(o.getRequestId());
        GeneralRequest generalRequest = changeRequestStatusToFinalized(o.getRequestId());
        FileDocumentDTO fileDocumentDTO = new FileDocumentDTO();
        if (generalRequestAux.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
            fileDocumentDTO = generatePolicyAux(o);
        } else {
            if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue())
                fileDocumentDTO.setTypeId(1000);
        }
        return fileDocumentDTO;
    }

    public FileDocumentDTO generatePolicyAux(SavePolicyDTO o) {
        FileDocumentDTO fileDocumentDTO = new FileDocumentDTO();
        List<Classifier> classifierListForRelationTypes = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Relationship.getReferenceId());
        List<Classifier> classifierListForMartialStatus = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.MaritalStatus.getReferenceId());
        List<Classifier> classifierListForExtensionCi = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        List<Classifier> listActivity = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Activity.getReferenceId());

        Reportbean report = new Reportbean();
        //para sacar la nomeclatura temenos que saber el producto

        String urlpathDocuments = this.path.getPathdocument();
        String urldirectionCertificate = this.path.getPathCertificateCoverage();

        //para sacar la nomeclatura temenos que saber el producto
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(o.getRequestId());
        Plan plan1 = planPort.getPlanById(generalRequest.getPlanId());
        Product product = productPort.findProductByPlanId(generalRequest.getPlanId());
        String nomenclature = product.getNomenclature();

        //#region 2.- Get policy last number

        Long numberPolicyMax = policyPort.getNumberPolicyMax(product.getId());
        Long numberPolicyNext = numberPolicyMax + 1;
        String numberPolicyNew = generateNumberPolicy(nomenclature, numberPolicyNext);

        //#endregion

        //#region 3.- Add new policy plan1.get().getAmount()
        Policy oPolicy = new Policy(o.getRequestId(), 1, numberPolicyNext, numberPolicyNew, plan1.getTotalPremium(), plan1.getTotalInsuredCapital(),
                PolicyStatusEnum.ACTIVE.getValue(), product.getId());
        Policy policy = policyPort.saveOrUpdate(oPolicy);

        // 3.1 Add policyItem una que se se genera la poliza
        PolicyItem policyItemAux = new PolicyItem(policy, o.getPerson().getId(), generalRequest.getId());
        PolicyItem policyItem = policyItemPort.saveOrUpdate(productCalculationsService.calculateVariables(policyItemAux));

        // 3.2 add CoveragePolicyItem, primero tenemos que sacar el CoverageProductPlan
        CoverageProductPlan coverageProductPlan = coverageProductPlanPort.findCoverageProductPlanByPlanIdAndProductId(product.getId(), plan1.getId());
        CoveragePolicyItem coveragePolicyItem = new CoveragePolicyItem(coverageProductPlan.getId(), policyItem.getId(), plan1.getTotalInsuredCapital());
        coveragePolicyItemPort.save(coveragePolicyItem);

        //#endregion

        //#region 4.- Add to beneficiariesList the beneficiaries

        String names = smvsCommonService.getCompleteName(o.getPerson().getNaturalPerson().getName(), o.getPerson().getNaturalPerson().getLastName(), o.getPerson().getNaturalPerson().getMotherLastName(), o.getPerson().getNaturalPerson().getMarriedLastName());
        List<Beneficiary> beneficiaryList = o.getBeneficiaryList();

        Receipt receipt = receiptPort.getReceiptForGeneralRequestById(generalRequest.getId());
        report.setPolicyNumber(policy.getNumberPolicy());
        report.setFullName(names);
        report.setPhoneNumber(o.getPerson().getTelephone() != null ? o.getPerson().getTelephone() : "");
        report.setHomeAddress(o.getPerson().getDirection().getDescription() != null ? o.getPerson().getDirection().getDescription() : "");
        report.setIdentificationNumber(o.getPerson().getNaturalPerson().getIdentificationNumber());
        report.setIdentificationExtension(loadExt(o.getPerson().getNaturalPerson().getExtIdc(), classifierListForExtensionCi));
        report.setGender(loadGender(o.getPerson().getNaturalPerson().getMaritalStatusIdc(), classifierListForMartialStatus));
        report.setAmountInsured(HelpersMethods.convertNumberToCompanyFormatNumber(policy.getInsuredCapital()));
        report.setDateValidateFrom(dateValidationFromString(policy));
        report.setAmountTotal(policy.getTotalPremium());
        report.setFullNameRequest(names);
        report.setOccupation(loadOccupation(o.getPerson().getActivityIdc(), listActivity));
        report.setDatePolicy(HelpersMethods.formatStringDate("dd/MM/yyyy", policy.getFromDate()));
        report.setSiteCity(receipt.getSalePlace());
        List<ReportBeneficiaries> beneficiariesList = new ArrayList<>();
        List<ReportLegals> legalsList = new ArrayList<>();
        AtomicReference<Integer> legalExit = new AtomicReference<>(2);

        beneficiaryList.forEach(a -> {
            a.setPolicyId(policy.getId());
            a.setPolicyItemId(policyItem.getId());
            beneficiariesList.add(loadReportbeneficiaries(a, classifierListForRelationTypes));
            if (a.getIsUnderAge() == 2) {
                legalsList.add(loadReportLegal(names, a, classifierListForExtensionCi, classifierListForRelationTypes));
                legalExit.set(1);
            }
        });

        // Save all beneficiaries
        beneficiaryPort.saveAll(beneficiaryList);
        report.setBeneficiary(beneficiariesList);
        report.setLegals(legalsList);

        //#endregion

        //#region 5.- Save the documents

        // The documents are saved in base64 format
        List<FileDocumentDTO> documentDTOList = o.getDocumentList();
        String identificationNumber = o.getPerson().getNaturalPerson().getIdentificationNumber();

        documentDTOList.forEach(g -> {
            Document document = formatDocument(g, generalRequest.getPersonId(), identificationNumber);
            documentPort.saveOrUpdate(document);
        });

        // Save the signature
        if (o.getDocumentFirm() != null) {
            FileDocumentDTO firm = o.getDocumentFirm();
            Document document = formatDocument(firm, generalRequest.getPersonId(), identificationNumber);
            documentPort.saveOrUpdate(document);
        }
        //#endregion

        //#region 6.- Change the request status

        generalRequest.setRequestStatusIdc(RequestStatusEnum.FINALIZED.getValue());
        generalRequestPort.saveOrUpdate(generalRequest);

        //#endregion

        //#region 7.- Generate the certificate coverage

        String dateRequest = HelpersMethods.formatStringDate("dd/MM/yyyy", DateUtils.asDate(generalRequest.getRequestDate()));
        report.setFirmDigital(o.getDocumentFirm().getContent());
        report.setSiteCity(receipt.getSalePlace());
        report.setDateRequest(dateRequest);
        // report
        List<Reportbean> reportList = new ArrayList<>();
        reportList.add(report);

        Object ols = reportList;
        FileDocumentDTO documentDTO = generateCertificateCoverage(ols, legalExit.get());

        //#endregion

        //#region 8.- Save certificate coverage in fileDocument

        documentDTO.setTypeId(TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue());
        String filename = policy.getNumberPolicy() + "_" + o.getPerson().getNaturalPerson().getIdentificationNumber();
        documentDTO.setName(filename);

        FileDocument fileDocument = formatFileDocument(documentDTO);
        fileDocument = fileDocumentPort.SaveOrUpdate(fileDocument);

        if (fileDocument.getId() != null) {
            PolicyFileDocument policyFileDocument = new PolicyFileDocument(policyItem.getId(), fileDocument.getId(), LocalDate.now());
            policyFileDocumentPort.saveOrUpdate(policyFileDocument);
        }

        //#endregion

        //#region Send Email Notification

        byte[] attachment = Base64.getDecoder().decode(documentDTO.getContent().getBytes(StandardCharsets.UTF_8));
        String attachmentName = filename + ".pdf";
        documentDTO.setName(attachmentName);
        SendMessageDTO messageDTO = SendMessageDTO.builder()
                .requestId(o.getRequestId())
                .name(o.getPerson().getNaturalPerson().getName())
                .email(o.getPerson().getEmail())
                .attachmentFile(attachment)
                .attachmentName(attachmentName)
                .phoneNumber(o.getPerson().getTelephone())
                .messageTypeEnum(AlertEnum.SMVS_ACTIVATION)
                .build();
        smvsCommonService.sendMessages(messageDTO);

        //#endregion

        fileDocumentDTO = documentDTO;
        return fileDocumentDTO;
    }

    public GeneralRequest changeRequestStatusToFinalized(Long requestId) {

        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(requestId);
        if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue()) {
            return generalRequest;
        }
        generalRequest.setRequestStatusIdc(RequestStatusEnum.FINALIZED.getValue());
        generalRequestPort.saveOrUpdate(generalRequest);
        return generalRequest;

    }

    public boolean changeRequestStatusToPending(Long requestId) {
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(requestId);
        generalRequest.setRequestStatusIdc(RequestStatusEnum.PENDING.getValue());
        generalRequestPort.saveOrUpdate(generalRequest);
        return true;
    }


    //#region Auxiliary Methods

    //#region Getters And Validations

    private String getDocumentDescription(FileDocumentDTO documentDTO, String identificationNumber) {
        String dateString = HelpersMethods.formatStringDate(new Date());
        String fileName = "";

        // Document FrontCard
        if (documentDTO.getTypeId() == TypesDocumentPersonEnum.FRONTCARD.getValue()) {
            fileName = identificationNumber + "ANV" + dateString;
        }
        // Document ReverseCard
        if (documentDTO.getTypeId() == TypesDocumentPersonEnum.REVERSECARD.getValue()) {
            fileName = identificationNumber + "REV" + dateString;
        }
        // Document ReverseAndFrontCard
        if (documentDTO.getTypeId() == TypesDocumentPersonEnum.REVERSEANDFRONTCARD.getValue()) {
            fileName = identificationNumber + "ANVREV" + dateString;
        }
        // Signature PNG Format
        if (documentDTO.getTypeId() == TypesDocumentPersonEnum.DIGITALFIRM.getValue()) {
            fileName = identificationNumber + "FIRM" + dateString;
        }

        return fileName;
    }

    private String getRelationType(Integer relationShipIdc, List<Classifier> classifierListForRelationTypes) {
        String relationString = "";
        if (relationShipIdc != null)
            relationString = classifierListForRelationTypes.stream().filter(classifier -> classifier.getReferenceId() == relationShipIdc.longValue()).findFirst().get().getDescription();
        return relationString;
    }

    private String dateValidationFromString(Policy policy) {

        String datefrom = HelpersMethods.formatStringDate("dd/MM/yyyy", policy.getFromDate());
        String dateTo = HelpersMethods.formatStringDate("dd/MM/yyyy", policy.getToDate());
        String validateFromToTo ="Desde hrs. 12:00 (md) "+datefrom + " a hrs. 12:00 (md) del " + dateTo;
        return validateFromToTo;
    }

    //#endregion

    //#region Loads

    // Load Attribute

    private String loadOccupation(Integer activityIdc, List<Classifier> listActivity) {
        String activity = "";
        if (activityIdc != null)
            activity = listActivity.stream().filter(classifier -> classifier.getReferenceId() == activityIdc.longValue()).findFirst().get().getDescription();
        return activity;
    }

    private String loadExt(Integer legalExt, List<Classifier> classifierListForExtensionCi) {
        String extString = "";
        if (legalExt != null)
            extString = classifierListForExtensionCi.stream().filter(classifier -> classifier.getReferenceId() == legalExt.longValue()).findFirst().get().getDescription();
        return extString;
    }

    private String loadGender(Integer genderIdc, List<Classifier> listGender) {
        String genderString = "";
        if (genderIdc != null)
            genderString = listGender.stream().filter(classifier -> classifier.getReferenceId() == genderIdc.longValue()).findFirst().get().getDescription();
        return genderString;
    }

    // Load Object

    private ReportLegals loadReportLegal(String fullNamesInsured, Beneficiary a, List<Classifier> classifierListForExtensionCi, List<Classifier> classifierListForRelationTypes) {
        String fullNameBeneficiary = smvsCommonService.getCompleteName(a.getName(), a.getLastName(), a.getMotherLastName(), a.getMarriedLastName());
        String extString = "";
        if (a.getLegalExt() != null) extString = loadExt(a.getLegalExt(), classifierListForExtensionCi);
        ReportLegals reportLegals = ReportLegals.builder()
                .ciLegal(a.getLegalIdentification())
                .legalExt(extString)
                .fullNameLegal(a.getRepresentativeLegalName())
                .fullNameBeneficiary(fullNameBeneficiary)
                .fullNames(fullNamesInsured)
                .percentage(a.getPercentage().doubleValue())
                .relation(getRelationType(a.getRelationshipIdc(), classifierListForRelationTypes))
                .build();
        return reportLegals;
    }

    private ReportBeneficiaries loadReportbeneficiaries(Beneficiary a, List<Classifier> classifierListForRelationTypes) {
        String names = smvsCommonService.getCompleteName(a.getName(), a.getLastName(), a.getMotherLastName(), a.getMarriedLastName());
        return ReportBeneficiaries.builder()
                .partyFullName(names)
                .partyRelationType(getRelationType(a.getRelationshipIdc(), classifierListForRelationTypes))
                .percentage(a.getPercentage())
                .build();
    }

    //#endregion

    //#region Format Objects

    private Document formatDocument(FileDocumentDTO documentDTO, long personId, String identificationNumber) {
        return Document.builder()
                .documentTypeIdc(documentDTO.getTypeId().intValue())
                .description(getDocumentDescription(documentDTO, identificationNumber))
                .content(documentDTO.getContent())
                .mimeType(documentDTO.getMime())
                .personId(personId)
                .build();
    }

    private FileDocument formatFileDocument(FileDocumentDTO fileDocument) {
        return FileDocument.builder()
                .description(fileDocument.getName())
                .typeDocument(fileDocument.getTypeId().intValue())
                .content(fileDocument.getContent())
                .mime(fileDocument.getMime())
                .build();
    }

    //#endregion

    private FileDocumentDTO generateCertificateCoverage(Object objectString, int i) throws NotFileWriteReadException {
        String mainReport = HelpersConstants.NAME_REPORT_CERTIFICATE_SMVS;
        Map<String, Object> map = new HashMap<>();
        map.put("mainReport", mainReport);
        map.put("reportBeansParams", objectString);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("existLegal", i);
        map.put("reportParameters", parameters);
        Gson g = new Gson();
        String json = g.toJson(map);
        FileDocumentDTO fileDocumentDTO = new FileDocumentDTO();
        fileDocumentDTO = generateReport(json);
        return fileDocumentDTO;
    }

    public FileDocumentDTO generateReport(String json) throws NotFileWriteReadException {
        Map<String, Object> map = (Map) (new Gson()).fromJson(json, HashMap.class);
        String mainReport = (String) map.get("mainReport");
        Map<String, String> subreports = (Map) map.get("subreports");
        List<Object> beans = (List) map.get("reportBeansParams");
        Map reportParameters = (Map) map.get("reportParameters");

        return reportServiceGeneric.generatePdf(mainReport, true, beans, reportParameters);
    }

    private String generateNumberPolicy(String nomenclature, long numberPolicyNext) {
        //String myStr = "POL-SMVS-SC-XXXX1-202X-00";
        Integer i = nomenclature.indexOf("X");
        String nom = nomenclature.substring(0, i);
        String numerical = String.valueOf(numberPolicyNext);
        Formatter fmt = new Formatter();
        String numberses = nom;
        int year = LocalDate.now().getYear();
        if (numerical.length() < 6) {
            Formatter s = fmt.format("%05d", numberPolicyNext);
            numberses = numberses + s + "-" + year + "-00";
        } else {
            numberses = numberses + numerical + "-" + year + "-00";
        }
        return numberses;
    }

    //#endregion


    //#region Deprecated Methods

    private String saveOrUpdateFile(FileDocumentDTO g, String identificationUser, String path) {
        String dateString = HelpersMethods.formatStringDate(new Date());
        String fileName;
        String pathfull = "";

        if (g.getTypeId() == TypesDocumentPersonEnum.FRONTCARD.getValue()) {//carnet anverso
            fileName = identificationUser + "ANV" + dateString;
            BufferedImage bufferedImage = HelpersMethods.decodeToImage(g.getContent());
            pathfull = HelpersMethods.saveImageToPath(path, fileName, g.getMime(), bufferedImage);
        }
        if (g.getTypeId() == TypesDocumentPersonEnum.REVERSECARD.getValue()) {// carnet Reverso
            fileName = identificationUser + "REV" + dateString;
            BufferedImage bufferedImage = HelpersMethods.decodeToImage(g.getContent());
            pathfull = HelpersMethods.saveImageToPath(path, fileName, g.getMime(), bufferedImage);
        }
        if (g.getTypeId() == TypesDocumentPersonEnum.REVERSEANDFRONTCARD.getValue()) {//Carnet Anverso/reverso Archivo de pdf
            fileName = identificationUser + "ANVREV" + dateString;
            pathfull = HelpersMethods.savePdfToPath(path, fileName, g.getContent());
        }
        if (g.getTypeId() == TypesDocumentPersonEnum.DIGITALFIRM.getValue()) {//Firma formato png
            fileName = identificationUser + "FIRM" + dateString;
            BufferedImage bufferedImage = HelpersMethods.decodeToImage(g.getContent());
            BufferedImage bufferedImage1 = HelpersMethods.resize(bufferedImage, 300, 350);
            pathfull = HelpersMethods.saveImageToPath(path, fileName, g.getMime(), bufferedImage1);
        }
        return pathfull;
    }

    private FileDocument formatFileDocumentDeprecated(String description, int documentType, String directoryLocation) {
        return FileDocument.builder()
                .typeDocument(documentType)
                .description(description)
                .directoryLocation(directoryLocation)
                .build();
    }

    private FileDocument formatFileDocument(String description, int documentType, String content) {
        return FileDocument.builder()
                .description(description)
                .typeDocument(documentType)
                .content(content)
                .build();
    }

    private String formatFirm(String content) {
        BufferedImage bufferedImage = HelpersMethods.decodeToImage(content);
        BufferedImage bufferedImage1 = HelpersMethods.resize(bufferedImage, 300, 350);
        String cont = HelpersMethods.encodeToString(bufferedImage, "png");
        return cont;
    }

    private String getFullName(NaturalPerson naturalPerson) {
        String names = naturalPerson.getComplement();
        return names;
    }

    public FileDocumentDTO generateReport(Map<String, Object> map) throws Exception {
        String mainReport = (String) map.get("mainReport");
        Map<String, String> subreports = (Map) map.get("subreports");
        List<Object> beans = (List) map.get("reportBeansParams");
        Map reportParameters = (Map) map.get("reportParameters");
        return reportServiceGeneric.generatePdf(mainReport, true, beans, reportParameters);
    }

    private String saveOrUpdateFilePdf(FileDocumentDTO g, String fileName, String path) {
        String pathfull = "";
        if (g.getMime() == HelpersConstants.PDF)
            pathfull = HelpersMethods.savePdfToPath(path, fileName, g.getContent());
        return pathfull;
    }

    private Policy formatPolicy(Long generalRequestId, Integer currencyTypeIdc, Long numberPolicyNext, String numberPolicy, Double premium, Double insuredCapital, Integer policyStatusIdc) {
        Date validateFrom = DateUtils.asDate(LocalDate.now());
        Date issuanceDate = new Date();
        Date validated = DateUtils.asSummaryRestartDays(validateFrom, Calendar.YEAR, 1);
        Policy policy = Policy.builder()
                .generalRequestId(generalRequestId)
                .currencyTypeIdc(currencyTypeIdc)
                .exchangeRate(0)
                .correlativeNumber(numberPolicyNext)
                .numberPolicy(numberPolicy)
                .issuanceDate(issuanceDate)
                .fromDate(DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validateFrom)))
                .toDate(DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validated)))
                .totalPremium(premium)
                .additionalPremium(0.0)
                .netPremium(premium)
                .riskPremium(0.0)
                .aps(0.0)
                .iva(0.0)
                .it(0.0)
                .intermediaryCommission(0.0)
                .intermediaryCommissionPercentage(0.0)
                .collectionServiceCommission(0.0)
                .collectionServiceCommissionPercentage(0.0)
                .insuredCapital(insuredCapital)
                .policyStatusIdc(policyStatusIdc)
                .branchOfficeId(null)
                .agencyId(null)
                .renewal(0)
                .assignedExecutiveId(null)
                .intermediaryTypeIdc(0)
                .brokerId(null)
                .agentId(null)
                .build();
        return policy;
    }

    private PolicyItem formatPolicyItem(Policy policy, Long personId, Long generalRequestId) {
        return PolicyItem.builder()
                .personId(personId)
                .policyId(policy.getId())
                .generalRequestId(generalRequestId)
                .individualPremium(policy.getTotalPremium())
                .individualInsuredCapital(policy.getInsuredCapital())
                .validityStart(policy.getFromDate())
                .termValidity(policy.getToDate())
                .riskPositionIdc(1)
                .build();
    }

    private CoveragePolicyItem formatCoverageProductItem(Long coveragePlanId, Long policyItemId) {
        CoveragePolicyItem coveragePolicyItem = CoveragePolicyItem.builder()
                .coverageProductPlanId(coveragePlanId)
                .policyItemId(policyItemId)
                .build();
        return coveragePolicyItem;
    }

    private PolicyFileDocument formatPolicyFileDocument(Long policyId, Long fileDocumentId, LocalDate uploadDate) {
        PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                .policyId(policyId)
                .fileDocumentId(fileDocumentId)
                .uploadDate(DateUtils.asDate(uploadDate))
                .build();
        return policyFileDocument;
    }

    //#endregion

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {NotFileWriteReadException.class, Exception.class})
    public String generatePolicyPendingToActivate(SavePolicyDTO o, GeneralRequest request) {
        try {
            GeneralRequest generalRequest = changeRequestStatusToFinalized(o.getRequestId());
            FileDocumentDTO fileDocumentDTO = new FileDocumentDTO();
            if (request.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
                generalRequest.setLegalHeirs(1);
                generalRequest.setAcceptanceReasonIdc((int) ClassifierEnum.ACCEPT_AUTOMATIC_SMVS_SYSTEM.getReferenceCode());
                generalRequestPort.saveOrUpdate(generalRequest);
                fileDocumentDTO = generatePolicyAux(o);
            } else {
                if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue()) {
                    fileDocumentDTO.setTypeId(1000);
                }
            }
            return fileDocumentDTO.getName();
        } catch (NotFileWriteReadException ex) {
            log.error("Error al generar la póliza automatica en el periodo seleccionado por: [{]]", ex.getMessage());
            throw ex;
        }   catch (Exception e) {
            log.error("Error al generar la póliza automatica en el periodo seleccionado", e.getMessage());
            throw e;
        }
    }

    public void sendMailWithExcel(List<PendingActivateErrorDTO> dataList) {
        List<String> headers = new ArrayList<>();
        headers.add("SOLICITUD");
        headers.add("CI");
        headers.add("MENSAJE");
        headers.add("COD. ACT.");
        headers.add("FECHA DE COMPRA DEL SEGURO");
        headers.add("ESTADO GENERACIÓN");
        FileDocumentDTO file = this.generateReportsServiceExcel.generateExcelFileDocumentDTO(headers,
                new ArrayList(dataList),"Solicitudes activadas por sistema-");
        Alert alert = this.alertPort.findByAlert(SMVSMessageTypeEnum.SYSTEM_ACTIVATE_AUTO.getValue());
        Emailbody emailbody = new Emailbody(alert,"SEPELIO - ACTIVACIÓN AUTOMÁTICA", 0L);
        byte[] decodeExcelFile = Base64.getDecoder().decode(file.getContent());
        emailbody.setBytes(decodeExcelFile);
        emailbody.setName_attachment(file.getName() + ".xlsx");
        emailService.saveEmail(emailbody);
    }

}
