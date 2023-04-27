package com.scfg.core.application.service;

import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.in.RequestAnnexeUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.enums.AnnexedRequirementEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.enums.TypesAttachmentsEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.AnnexeRequirement;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.RequestPolicyDetailDto;
import com.scfg.core.domain.dto.vin.Account;
import com.scfg.core.domain.dto.vin.GenerateDocSettlement;
import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import com.scfg.core.domain.dto.vin.RequirementDTO;
import com.scfg.core.domain.person.Person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestAnnexeService implements RequestAnnexeUseCase {
    private final AnnexeFileDocumentPort annexeFileDocumentPort;
    private final AnnexeRequirementPort annexeRequirementPort;
    private final RequestAnnexePort requestAnnexePort;
    private final AnnexeRequirementControlPort annexeRequirementControlPort;
    private final GeneratePdfUseCase generatePdfUseCase;
    private final PolicyPort policyPort;
    private final ClassifierPort classifierPort;
    private final AccountPort accountPort;
    private final GeneralRequestPort generalRequestPort;
    private final PersonPort personPort;

    @Override
    public Boolean processRequest(RequestAnnexeDTO requestAnnexeDTO) {
        List<AnnexeRequirement> anexedList = this.annexeRequirementPort.findAllByAnnexeTypeId(1L);
        this.validateRequestAnnexe(requestAnnexeDTO);
        Long requestAnnexeId = requestAnnexePort.saveOrUpdate(requestAnnexeDTO);
        Policy policy = policyPort.findByPolicyId(requestAnnexeDTO.getPolicyDetail().getId());
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId());
        Person person = personPort.findById(generalRequest.getPersonId());
        Classifier extensionCi = classifierPort.getClassifierByReferencesIds(person.getNaturalPerson().getExtIdc(), ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        Account account = accountPort.findLastByPersonIdAndPolicyId(generalRequest.getPersonId(), policy.getId());
        String policyNumber = policy.getNumberPolicy();
        Date policyFromDate = policy.getFromDate();
        Date policyToDate = policy.getToDate();
        int milisecondsByDay = (1000 * 60 * 60 * 24);
        Date requestDate = new Date();
        Integer daysPassed = (int) Math.floor(((requestDate.getTime() - policy.getIssuanceDate().getTime()) / milisecondsByDay));
        Integer daysValidity =(int) Math.floor(((policyToDate.getTime() - policyFromDate.getTime()) / milisecondsByDay));
        Integer yearsPassed = (int) (daysPassed / 365);
        Integer crediTermInYears = generalRequest.getCreditTermInYears();
        String accountNumber = account.getAccountNumber();
        Classifier currency = classifierPort.getClassifierByReferencesIds(policy.getCurrencyTypeIdc(), ClassifierTypeEnum.Currency.getReferenceId());
        String currencyDesc = currency.getDescription();
        String currencyAbbreviation = currency.getAbbreviation();
        Double currencyDollarValue = 6.86;
        Double premiumPaid = policy.getTotalPremium();
        Double premiumPaidAnnual = 0.0;
        try {
            premiumPaidAnnual = premiumPaid / crediTermInYears;
        } catch (ArithmeticException e) {
            System.err.println(e);
        }
        Double adminExpenses = 0.0;
        if (daysPassed > 30) {
            adminExpenses = premiumPaidAnnual * 0.30;
        }
        if( adminExpenses<0.0){
            adminExpenses=0.0;
        }
        Double discountProrataDay = (premiumPaid / daysValidity) * daysPassed;
        Double valueToReturn = premiumPaid - adminExpenses - discountProrataDay;
        Double amountAccepted = valueToReturn;
        String assuranceName = requestAnnexeDTO.getPolicyDetail().getNames() + " " + requestAnnexeDTO.getPolicyDetail().getLastName();
        String assuranceIdentificationNumber = requestAnnexeDTO.getPolicyDetail().getIdentificationNumber();
        String assuranceIdentificationExtension = extensionCi.getDescription();
        String insurerCompany = requestAnnexeDTO.getInsurerCompany().getJuridicalPerson().getName();

        //#region init model

        GenerateDocSettlement modeltDto = new GenerateDocSettlement();
        modeltDto.setPolicyName("PÓLIZA DE SEGURO INDIVIDUAL TEMPORAL INCLUSIVO");
        modeltDto.setPolicyNumber(policyNumber);
        modeltDto.setPolicyFromDate(policyFromDate);
        modeltDto.setPolicyToDate(policyToDate);
        modeltDto.setRequestDate(requestDate);
        modeltDto.setDaysPassed(daysPassed - (365 * yearsPassed));
        modeltDto.setYearsPassed(yearsPassed);
        modeltDto.setCrediTermInYears(crediTermInYears);
        modeltDto.setAccountNumber(accountNumber);
        modeltDto.setAmountAccepted(amountAccepted);
        modeltDto.setCurrencyAbbreviation(currencyAbbreviation);
        modeltDto.setCurrencyDesc(currencyDesc);
        modeltDto.setCurrencyDollarValue(currencyDollarValue);
        modeltDto.setPremiumPaid(premiumPaid);
        modeltDto.setPremiumPaidAnnual(premiumPaidAnnual);
        modeltDto.setAdminExpenses(adminExpenses);
        modeltDto.setDiscountProrataDay(discountProrataDay);
        modeltDto.setValueToReturn(valueToReturn);
        modeltDto.setAssuranceName(assuranceName);
        modeltDto.setAssuranceIdentificationNumber(assuranceIdentificationNumber);
        modeltDto.setAssuranceIdentificationExtension(assuranceIdentificationExtension);
        modeltDto.setInsurerCompanyName(insurerCompany);
        modeltDto.setNroSettlement("0001");
        modeltDto.setInsuredCapital(50000.00);
        modeltDto.setCellphone(person.getTelephone());
        modeltDto.setCity("Santa Cruz de la Sierra");

        //#endregion

        List<Document> lis= new ArrayList<Document>();
        byte[] docSettlement = generatePdfUseCase.generateVINSettlement(modeltDto);
        byte[] docRescue = generatePdfUseCase.generateVINRescission(modeltDto);
        String docSettlementBase64 = Base64.getEncoder().encodeToString(docSettlement);
        String docRescueBase64 = Base64.getEncoder().encodeToString(docRescue);
        Document settlementFileDocument = Document.builder()
                .mimeType("application/pdf")
                .description("Finiquito.pdf")
                .content(docSettlementBase64)
                .documentTypeIdc(TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue())
                .build();
        Document rescueFileDocument = Document.builder()
                .mimeType("application/pdf")
                .description("Solicitud de Rescate.pdf")
                .content(docRescueBase64)
                .documentTypeIdc(TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue())
                .build();
        String dateISO =HelpersMethods.formatStringDate("yyyy-MM-dd'T'HH:mm:ss'Z'", new Date());
        RequirementDTO reqSettlement = new RequirementDTO(
                0L, "Finiquito", dateISO, dateISO,"", settlementFileDocument,
                anexedList.stream().filter(x->x.getName().equals(AnnexedRequirementEnum.REQ_FINIQUITO.getValue()))
                        .collect(Collectors.toList()).get(0).getId()
                );
        RequirementDTO reqRescue = new RequirementDTO(
                0L, "Solicitud de rescate", dateISO, dateISO,"", rescueFileDocument,
                anexedList.stream().filter(x->x.getName().equals(AnnexedRequirementEnum.REQ_RESCUE.getValue()))
                        .collect(Collectors.toList()).get(0).getId()
        );
        requestAnnexeDTO.getRequestList().add(reqSettlement);
        requestAnnexeDTO.getRequestList().add(reqRescue);
        requestAnnexeDTO.getRequestList().forEach(o -> {
            Long fileDocumentId = annexeFileDocumentPort.saveOrUpdate(o.getFileDocument());
            annexeRequirementControlPort.saveOrUpdate(o, fileDocumentId, requestAnnexeId);
            lis.add(o.getFileDocument());
        });
        return requestAnnexeId>0;
    }

    @Override
    public Boolean hasPendingRequests(Long policyId, Long annexeTypeId) {
        OperationException.throwExceptionIfNumberInvalid("Id poliza", policyId);
        OperationException.throwExceptionIfNumberInvalid("Id tipo de anexo", annexeTypeId);

        List<RequestAnnexeDTO> list = requestAnnexePort.getRequestByPolicyIdAndAnnexeTypeIdAndStatus(policyId,
                annexeTypeId, RequestStatusEnum.PENDING.getValue());

        return list.size() > 0;
    }

    //#region Validations
    public void validateRequestAnnexe(RequestAnnexeDTO requestAnnexeDTO) {
        OperationException.throwExceptionIfNumberInvalid("código de tipo de anexo", requestAnnexeDTO.getAnnexeTypeId());
        OperationException.throwExceptionIfTextInvalid("motivo de anulación", requestAnnexeDTO.getAnnulmentReason());
        this.throwExceptionIfRequirementInvalid(requestAnnexeDTO.getRequestList());
        this.throwExceptionIfPolicyDetailInvalid(requestAnnexeDTO.getPolicyDetail());
        this.throwExceptionIfPlanInvalid(requestAnnexeDTO.getPlan());
        this.throwExceptionIfPersonInvalid(requestAnnexeDTO.getInsurerCompany());
    }


    public void throwExceptionIfPolicyDetailInvalid(RequestPolicyDetailDto policyDetail) {
        if (policyDetail == null) {
            throw new OperationException("No se pudo realizar la operación");
        }
    }

    public void throwExceptionIfPlanInvalid(Plan plan) {
        if (plan == null) {
            throw new OperationException("No se pudo realizar la operación");
        }
    }

    public void throwExceptionIfPersonInvalid(Person person) {
        if (person == null) {
            throw new OperationException("No se pudo realizar la operación");
        }
    }

    public void throwExceptionIfRequirementInvalid(List<RequirementDTO> lis) {
        if (lis.isEmpty()) {
            throw new OperationException("No se pudo realizar la operación, la lista de requerimientos esta vacía");
        }
        RequirementDTO requirementDTO = lis.stream().findFirst().orElse(null);
        if (requirementDTO.getFileDocument() == null) {
            throw new OperationException("No se pudo realizar la operación, se requiere el documento");
        }
        if (requirementDTO.getFileDocument().getContent() == null || requirementDTO.getFileDocument().getContent().trim().isEmpty()) {
            throw new OperationException("No se pudo realizar la operación, se requiere el documento");
        }
    }

    //#endregion

}
