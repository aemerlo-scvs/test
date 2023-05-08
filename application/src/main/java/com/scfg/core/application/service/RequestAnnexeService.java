package com.scfg.core.application.service;

import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.in.RequestAnnexeUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.application.service.sender.EmailSenderService;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.*;
import com.scfg.core.domain.dto.RequestPolicyDetailDto;
import com.scfg.core.domain.dto.vin.*;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.person.Person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import java.text.ParseException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestAnnexeService implements RequestAnnexeUseCase {
    private final GeneratePdfUseCase generatePdfUseCase;
    private final SenderService senderService;
    private final EmailSenderService emailSenderService;
    private final ProductCalculationsService productCalculationsService;
    private final AlertService alertService;
    private final AnnexeFileDocumentPort annexeFileDocumentPort;
    private final AnnexeRequirementPort annexeRequirementPort;
    private final RequestAnnexePort requestAnnexePort;
    private final AnnexeTypePort annexeTypePort;
    private final AnnexeRequirementControlPort annexeRequirementControlPort;
    private final PolicyPort policyPort;
    private final PolicyItemPort policyItemPort;
    private final ClassifierPort classifierPort;
    private final AccountPort accountPort;
    private final GeneralRequestPort generalRequestPort;
    private final PersonPort personPort;
    private final AnnexePort annexePort;
    private final PaymentPort paymentPort;
    private final PaymentPlanPort paymentPlanPort;
    private final TransactionPort transactionPort;
    private final PaymentFileDocumentPort paymentFileDocumentPort;
    private final TransactionFileDocumentPort transactionFileDocumentPort;
    private final PolicyItemMathReservePort policyItemMathReservePort;
    private final CoverageProductPlanPort coverageProductPlanPort;
    private final PolicyItemEconomicPort policyItemEconomicPort;
    private final PolicyItemEconomicReinsurancePort policyItemEconomicReinsurancePort;
    private final CoveragePolicyItemPort coveragePolicyItemPort;
    private final UserPort userPort;

    @Override
    public RequestAnnexeDetailDTO getRequestAnnexeDetail(Long policyId, Long annexeTypeId) {
        OperationException.throwExceptionIfNumberInvalid("Id poliza", policyId);
        OperationException.throwExceptionIfNumberInvalid("Id tipo de anexo", annexeTypeId);

        List<Integer> requestAnnexeStatusList = new ArrayList<>();
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.PENDING.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.OBSERVED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.REQUESTED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.ACCEPTED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.REJECTED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.PAID.getValue());

        List<RequestAnnexe> requestAnnexeList = requestAnnexePort.findAllRequestByPolicyIdAndAnnexeTypeIdAndRequestStatus(policyId,
                annexeTypeId, requestAnnexeStatusList);

        if (requestAnnexeList.isEmpty()) {
            throw new OperationException("No encontró el detalle de la solicitud");
        }

        RequestAnnexe lastRequestAnnexe = requestAnnexeList.get(0);
        String paymentDesc = annexePort.findPaymentDescByRequestAnnexeId(lastRequestAnnexe.getId());

        return RequestAnnexeDetailDTO.builder()
                .requestAnnexe(lastRequestAnnexe)
                .paymentDesc(paymentDesc)
                .annexeRequirementList(annexeRequirementControlPort.findAllByRequestAnnexeIdAndAnnexeTypeId(lastRequestAnnexe.getId()))
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public List<AnnexeRequirementControl> processRequest(RequestAnnexeDTO requestAnnexeDTO) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int oneYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

        double currencyDollarValue = 6.86;
        double intermediaryComissionPercentage = 0.0;
        double insuredCapital = 70000;
        String policyName = "PÓLIZA DE SEGURO INDIVIDUAL TEMPORAL INCLUSIVO";
        String requirementControlComment = "GENERADO POR SISTEMA";
        String city = "Santa Cruz de la Sierra";
        String coverageToSearch = "MUERTE";


        ZonedDateTime zonedUTC = requestAnnexeDTO.getRequestDate().atZone(ZoneId.of("UTC"));
        ZonedDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("America/La_Paz"));
        requestAnnexeDTO.setRequestDate(zonedIST.toLocalDateTime());

        Date requestDate = DateUtils.asDate(requestAnnexeDTO.getRequestDate());
        LocalDateTime today = DateUtils.asDateToLocalDateTime(requestDate);
        List<PolicyItemEconomicReinsurance> policyItemEconomicReinsuranceList = new ArrayList<>();

        this.validateRequestAnnexe(requestAnnexeDTO);

        List<AnnexeRequirement> annexeRequirementList = this.annexeRequirementPort.findAllByAnnexeTypeId(requestAnnexeDTO.getAnnexeTypeId());

        Policy policy = policyPort.findByPolicyId(requestAnnexeDTO.getPolicyDetail().getId());

        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId());
        Person person = personPort.findById(generalRequest.getPersonId());
        PolicyItem policyItem = policyItemPort.findByPolicyIdAndPersonId(policy.getId(), person.getId());
        PolicyItemEconomic prodPolicyItemEconomic = policyItemEconomicPort.findLastByPolicyItemIdAndMovementTypeIdc(
                policyItem.getId(), PolicyMovementTypeClassifierEnum.PRODUCTION.getValue(), PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<PolicyItemEconomicReinsurance> prodPolicyItemEconomicReinsuranceList = policyItemEconomicReinsurancePort
                .findAllByPolicyItemEconomicId(prodPolicyItemEconomic.getId());
        List<CoverageDTO> coverageDTOList = coverageProductPlanPort.findAllByPolicyItemId(policyItem.getId());
        Classifier extensionCi = classifierPort.getClassifierByReferencesIds(person.getNaturalPerson().getExtIdc(), ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        Account account = accountPort.findLastByPersonIdAndPolicyId(generalRequest.getPersonId(), policy.getId());

        int daysPassed = DateUtils.daysBetween(policy.getFromDate(), requestDate).intValue();
        int policyDaysValidity = DateUtils.daysBetween(policy.getFromDate(), policy.getToDate()).intValue();
        int policyYearsPassed = (daysPassed / oneYear);

        Classifier currency = classifierPort.getClassifierByReferencesIds(policy.getCurrencyTypeIdc(), ClassifierTypeEnum.Currency.getReferenceId());
        String currencyDesc = currency.getDescription();
        String currencyAbbreviation = currency.getAbbreviation();

        double rescueValue = calcRescueValue(policyItem.getId(), policy.getFromDate(), requestDate, policy.getTotalPremium());
        Double premiumPaidAnnual = policy.getTotalPremium() / generalRequest.getCreditTermInYears();
        double adminExpenses = calcAdminExpenses(premiumPaidAnnual, daysPassed);
        double discountPerPassedDays = calcDiscountPerDay(rescueValue, daysPassed, policyDaysValidity);
        double valueToReturn = rescueValue - adminExpenses - discountPerPassedDays;

        if (valueToReturn < 0) {
            valueToReturn = 0;
        }

        if (valueToReturn > prodPolicyItemEconomic.getIndividualPremium()) {
            valueToReturn = prodPolicyItemEconomic.getIndividualPremium();
        }


        String insuredIdentificationExtension = extensionCi.getDescription();
        String insurerCompanyName = requestAnnexeDTO.getInsurerCompany().getJuridicalPerson().getName();

        CoverageDTO deathCoverage = coverageDTOList.stream()
                .filter(e -> e.getCoverageName().toUpperCase().trim().contains(coverageToSearch))
                .findFirst()
                .orElse(null);
        if (deathCoverage != null) {
            insuredCapital = deathCoverage.getInsuredCapitalCoverage();
        }

        //#region DTO for Documents

        GenerateDocSettlement documentDTO = new GenerateDocSettlement();
        documentDTO.setDate(requestDate);
        documentDTO.setPolicyName(policyName);
        documentDTO.setPolicyNumber(policy.getNumberPolicy());
        documentDTO.setPolicyFromDate(policy.getFromDate());
        documentDTO.setPolicyToDate(policy.getToDate());
        documentDTO.setRequestDate(requestDate);
        documentDTO.setDaysPassed(daysPassed - (oneYear * policyYearsPassed));
        documentDTO.setYearsPassed(policyYearsPassed);
        documentDTO.setCreditTermInYears(generalRequest.getCreditTermInYears());
        documentDTO.setAccountNumber(account.getAccountNumber());
        documentDTO.setAmountAccepted(valueToReturn);
        documentDTO.setCurrencyAbbreviation(currencyAbbreviation);
        documentDTO.setCurrencyDesc(currencyDesc);
        documentDTO.setCurrencyDollarValue(currencyDollarValue);
        documentDTO.setPremiumPaid(policy.getTotalPremium());
        documentDTO.setPremiumPaidAnnual(premiumPaidAnnual);
        documentDTO.setRescueValue(rescueValue);
        documentDTO.setAdminExpenses(adminExpenses);
        documentDTO.setDiscountPerDay(discountPerPassedDays);
        documentDTO.setValueToReturn(valueToReturn);
        documentDTO.setAssuranceName(requestAnnexeDTO.getPersonCompleteName());
        documentDTO.setAssuranceIdentificationNumber(requestAnnexeDTO.getPolicyDetail().getIdentificationNumber());
        documentDTO.setAssuranceIdentificationExtension(insuredIdentificationExtension);
        documentDTO.setInsurerCompanyName(insurerCompanyName);
        documentDTO.setNroSettlement("0");
        documentDTO.setInsuredCapital(insuredCapital);
        documentDTO.setCellphone(person.getTelephone());
        documentDTO.setCity(city);

        //#endregion

        Long docSettlementNumber = this.annexeFileDocumentPort.getNextNumber(
                AnnulmentRequestAnnexeDocumentTypeClassifierEnum.SETTLEMENT.getValue());
        documentDTO.setNroSettlement(String.format("%04d", docSettlementNumber));
        byte[] docSettlement = generatePdfUseCase.generateVINSettlement(documentDTO);
        byte[] docRescue = generatePdfUseCase.generateVINRescission(documentDTO);
        String docSettlementBase64 = Base64.getEncoder().encodeToString(docSettlement);
        String docRescueBase64 = Base64.getEncoder().encodeToString(docRescue);


        Document settlementFileDocument = Document.builder()
                .mimeType(HelpersConstants.PDF)
                .description(requestAnnexeDTO.getPolicyDetail().getIdentificationNumber() + '_' +
                        AnnexeRequirementEnum.SETTLEMENT.getValue().toUpperCase() + '_' + requestDate.getTime())
                .content(docSettlementBase64)
                .documentNumber(docSettlementNumber)
                .documentTypeIdc(AnnulmentRequestAnnexeDocumentTypeClassifierEnum.SETTLEMENT.getValue())
                .build();

        Document rescueFileDocument = Document.builder()
                .mimeType(HelpersConstants.PDF)
                .description(requestAnnexeDTO.getPolicyDetail().getIdentificationNumber() + '_' +
                        AnnexeRequirementEnum.RESCUE_REQUEST.getValue().toUpperCase() + '_' + requestDate.getTime())
                .content(docRescueBase64)
                .documentTypeIdc(AnnulmentRequestAnnexeDocumentTypeClassifierEnum.RESCUE_REQUEST.getValue())
                .build();


        AnnexeRequirementControl reqSettlement = new AnnexeRequirementControl(
                AnnexeRequirementEnum.SETTLEMENT.getValue(), requirementControlComment, today, today,
                annexeRequirementList.stream().filter(x -> x.getName().equalsIgnoreCase(AnnexeRequirementEnum.SETTLEMENT.getValue()))
                        .collect(Collectors.toList()).get(0).getId(), false, settlementFileDocument
        );

        AnnexeRequirementControl reqRescue = new AnnexeRequirementControl(
                AnnexeRequirementEnum.RESCUE_REQUEST.getValue(), requirementControlComment, today, today,
                annexeRequirementList.stream().filter(x -> x.getName().equalsIgnoreCase(AnnexeRequirementEnum.RESCUE_REQUEST.getValue()))
                        .collect(Collectors.toList()).get(0).getId(), false, rescueFileDocument
        );

        AnnexeRequirementControl signedReqSettlement = new AnnexeRequirementControl(
                AnnexeRequirementEnum.SETTLEMENT.getValue(), "", today, null,
                annexeRequirementList.stream().filter(x -> x.getName().equalsIgnoreCase(AnnexeRequirementEnum.SETTLEMENT.getValue()))
                        .collect(Collectors.toList()).get(0).getId(), true, null
        );

        AnnexeRequirementControl signedReqRescue = new AnnexeRequirementControl(
                AnnexeRequirementEnum.RESCUE_REQUEST.getValue(), "", today, null,
                annexeRequirementList.stream().filter(x -> x.getName().equalsIgnoreCase(AnnexeRequirementEnum.RESCUE_REQUEST.getValue()))
                        .collect(Collectors.toList()).get(0).getId(), true, null
        );

        requestAnnexeDTO.getRequestList().get(0).setRequirementId(
                annexeRequirementList.stream().filter(x -> x.getName().equalsIgnoreCase(AnnexeRequirementEnum.REVERSE_AND_FRONT_CI.getValue()))
                        .collect(Collectors.toList()).get(0).getId()
        );

        zonedUTC = requestAnnexeDTO.getRequestList().get(0).getRequestDate().atZone(ZoneId.of("UTC"));
        zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("America/La_Paz"));
        requestAnnexeDTO.getRequestList().get(0).setRequestDate(zonedIST.toLocalDateTime());
        requestAnnexeDTO.getRequestList().get(0).setReceptionDate(zonedIST.toLocalDateTime());
        requestAnnexeDTO.getRequestList().get(0).setSigned(false);

        requestAnnexeDTO.getRequestList().add(reqSettlement);
        requestAnnexeDTO.getRequestList().add(reqRescue);
        requestAnnexeDTO.getRequestList().add(signedReqSettlement);
        requestAnnexeDTO.getRequestList().add(signedReqRescue);

        PolicyItemEconomic policyItemEconomic = new PolicyItemEconomic(policyItem.getId(),
                PolicyMovementTypeClassifierEnum.ANNULMENT.getValue(), prodPolicyItemEconomic.getIntermediaryId(), valueToReturn);
        policyItemEconomic.setStatus(PersistenceStatusEnum.DELETED.getValue());

        double totalPremiumCeded = productCalculationsService.calcPolicyItemEconomicReinsuranceTotalPremiumCededVIN(
                prodPolicyItemEconomicReinsuranceList, generalRequest.getCreditTermInYears(), policy.getFromDate(),
                policy.getToDate(), requestDate);

        if (prodPolicyItemEconomic.getIndividualIntermediaryCommissionPercentage() != null &&
                prodPolicyItemEconomic.getIndividualIntermediaryCommissionPercentage() > 0) {
            intermediaryComissionPercentage = prodPolicyItemEconomic.getIndividualIntermediaryCommissionPercentage() / 100;
        }

        productCalculationsService.calcPolicyItemEconomicVIN(policyItemEconomic, prodPolicyItemEconomic,
                totalPremiumCeded, generalRequest.getCreditTermInYears(), intermediaryComissionPercentage,
                policy.getFromDate(), policy.getToDate(), requestDate);

        policyItemEconomic = policyItemEconomicPort.saveOrUpdate(policyItemEconomic);

        for (CoverageDTO coverage : coverageDTOList) {
            PolicyItemEconomicReinsurance policyItemEconomicReinsurance = new PolicyItemEconomicReinsurance(
                    policyItemEconomic.getId(), coverage.getCoverageId());

            productCalculationsService.calcPolicyItemEconomicReinsuranceVIN(policyItemEconomicReinsurance,
                    prodPolicyItemEconomicReinsuranceList, policyItemEconomic.getMovementTypeIdc(),
                    coverage, policyItemEconomic.getIndividualNetPremium(),
                    generalRequest.getCreditTermInYears(), policy.getFromDate(), policy.getToDate(), requestDate);

            policyItemEconomicReinsurance.setStatus(PersistenceStatusEnum.DELETED.getValue());
            policyItemEconomicReinsuranceList.add(policyItemEconomicReinsurance);
        }

        policyItemEconomicReinsurancePort.saveOrUpdateAll(policyItemEconomicReinsuranceList);

        Long requestAnnexeId = requestAnnexePort.saveOrUpdate(new RequestAnnexe(requestAnnexeDTO));

        requestAnnexeDTO.getRequestList().forEach(o -> {
            if (o.getFileDocument() != null) {
                Long fileDocumentId = annexeFileDocumentPort.saveOrUpdate(o.getFileDocument());
                o.setFileDocumentId(fileDocumentId);
            }
            o.setRequestAnnexeId(requestAnnexeId);
            o.setId(annexeRequirementControlPort.saveOrUpdate(o));
        });

         return requestAnnexeDTO.getRequestList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    public List<AnnexeRequirementDto> processRequest(UpdateRequestAnnexeDTO updateRequestAnnexeDTO) {

        OperationException.throwExceptionIfNumberInvalid("anexo", updateRequestAnnexeDTO.getRequestAnnexeId());
        OperationException.throwExceptionIfNumberInvalid("motivo de anulación", updateRequestAnnexeDTO.getAnnulmentReasonIdc().longValue());

        RequestAnnexe requestAnnexe = this.requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(updateRequestAnnexeDTO.getRequestAnnexeId());
        requestAnnexe.setReasonIdc(updateRequestAnnexeDTO.getAnnulmentReasonIdc());
        this.requestAnnexePort.saveOrUpdate(requestAnnexe);
        if (updateRequestAnnexeDTO.getRequestList().size() > 0) {
            if (updateRequestAnnexeDTO.getRequestList().get(0).getFileDocument() != null) {
                this.annexeFileDocumentPort.saveOrUpdate(updateRequestAnnexeDTO.getRequestList().get(0).getFileDocument());
            }
        }

        return this.annexeRequirementControlPort.findAllByRequestAnnexeIdAndAnnexeTypeId(requestAnnexe.getId());
    }

    @Override
    public RequestAnnexeFileDocumentDTO hasPendingRequests(Long policyId, Long annexeTypeId) {
        OperationException.throwExceptionIfNumberInvalid("Id poliza", policyId);
        OperationException.throwExceptionIfNumberInvalid("Id tipo de anexo", annexeTypeId);

        List<Integer> requestAnnexeStatusList = new ArrayList<>();
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.PENDING.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.OBSERVED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.REQUESTED.getValue());

        List<RequestAnnexe> requestAnnexeList = requestAnnexePort.findAllRequestByPolicyIdAndAnnexeTypeIdAndRequestStatus(policyId,
                annexeTypeId, requestAnnexeStatusList);

        RequestAnnexeFileDocumentDTO requestAnnexeFileDocumentDTO = RequestAnnexeFileDocumentDTO.builder().build();
        if (requestAnnexeList.size() > 0) {
            RequestAnnexe requestAnnexe = requestAnnexeList.get(0);
            if (requestAnnexe.getStatusIdc().equals(RequestAnnexeStatusEnum.REQUESTED.getValue())) {
                throw new OperationException("No se pudo realizar la operación, la póliza ya tiene una solicitud de rescate en curso");
            }
            requestAnnexeFileDocumentDTO.setRequestAnnexe(requestAnnexe);
            Document ciDocument = this.annexeFileDocumentPort.getFileByRequestAnnexeIdAndTypeDocumentIdc(
                    requestAnnexeFileDocumentDTO.getRequestAnnexe().getId(),
                    AnnulmentRequestAnnexeDocumentTypeClassifierEnum.REVERSE_AND_FRONT_CI.getValue());
            AnnexeRequirementControl annexeRequirementControl = this.annexeRequirementControlPort.findByRequestAnnexeIdAndFileDocumentId(
                    requestAnnexe.getId(), ciDocument.getId());
            annexeRequirementControl.setFileDocument(ciDocument);
            requestAnnexeFileDocumentDTO.setAnnexeRequirementControl(annexeRequirementControl);
            return requestAnnexeFileDocumentDTO;
        } else return null;
    }

    @Override
    public PageableDTO getAllPageByAnnexeFilters(Integer page, Integer size, RequestAnnexeSearchFiltersDto filtersDto) throws ParseException {
        this.throwExceptionIfInvalidFilters(filtersDto);
        return this.requestAnnexePort.findAllPageByFilters(filtersDto, page, size);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public void processCancellationRequest(Long requestAnnexeId, RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto) {
        this.validateProcessCancelRequestAnnexe(requestAnnexeCancelaltionDto, requestAnnexeId);
        Alert alert = new Alert();
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        MessageDTO messageDTO = new MessageDTO();
        List<String> valuesToReplace = new ArrayList<>();

        senderService.setStrategy(emailSenderService);
        List<AttachmentDTO> fileList = new ArrayList<>();

        Classifier statusTechnicalPosition = this.classifierPort.getClassifierById(requestAnnexeCancelaltionDto.getStatusTechnicalPosition());
        Policy policy = this.policyPort.findByPolicyIdOrThrowExcepcion(requestAnnexeCancelaltionDto.getPolicyId());
        RequestAnnexe requestAnnexe = this.requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(requestAnnexeId);
        requestAnnexe.setPolicyId(policy.getId());

        List<String> valuesSubjectToReplace = new ArrayList<>();
        Person person = personPort.findByPolicyIdWhenPolicyAndPersonIsOneToOne(policy.getId());
        valuesSubjectToReplace.add(person.getNaturalPerson().getCompleteName());
        User user = this.userPort.findById(requestAnnexe.getCreatedBy());
        valuesToReplace.add(user.getCompleteName());

        if (statusTechnicalPosition.getReferenceId().equals((long) StatusTechnicalPositionEnum.ACCEPTED.getValue())) {
            requestAnnexe.setStatusIdc(RequestAnnexeStatusEnum.ACCEPTED.getValue());
            this.annexeTypePort.findByIdOrExcepcion(requestAnnexeCancelaltionDto.getAnnexeTypeId());
            requestAnnexeCancelaltionDto.setEndDate(policy.getToDate());
            Long annexeId = this.annexePort.saveOrUpdate(requestAnnexeCancelaltionDto, requestAnnexeId);

            policy.setPolicyStatusIdc(PolicyStatusEnum.CANCELED.getValue());
            this.policyPort.saveOrUpdate(policy);

            Long documentNumber = this.paymentFileDocumentPort.getMaxNumberTransactionFileDocument();
            AttachmentDTO attachmentDTO = this.generateReportPDFRamsonStatement(policy, documentNumber);
            fileList.add(attachmentDTO);
            PaymentFileDocument paymentFileDocument = PaymentFileDocument.builder()
                    .content(Base64.getEncoder().encodeToString(attachmentDTO.getContent()))
                    .description(attachmentDTO.getFileName())
                    .mimeType(attachmentDTO.getMimeType())
                    .documentNumber(String.valueOf(documentNumber))
                    .documentTypeIdc(AnnulmentRequestAnnexeDocumentTypeClassifierEnum.RESCUE_NOTE.getValue())
                    .build();
            AttachmentDTO finiquito = this.getSettlement(requestAnnexeId);
            fileList.add(finiquito);
            Long paymentFileDocumentId = this.paymentFileDocumentPort.saveOrUpdate(paymentFileDocument);

            PolicyItem policyItem = policyItemPort.findByPolicyIdAndPersonId(policy.getId(), person.getId());
            policyItem.setTermValidity(DateUtils.asDate(today));

            PolicyItemEconomic policyItemEconomic = this.policyItemEconomicPort.findLastByPolicyItemIdAndMovementTypeIdc(
                    policyItem.getId(),
                    PolicyMovementTypeClassifierEnum.ANNULMENT.getValue()
            );
            policyItemEconomic.setAnnexeId(annexeId);
            policyItemEconomic.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

            Payment payment = new Payment(
                    policyItemEconomic.getIndividualPremium(),
                    policy.getCurrencyTypeIdc(),
                    policy.getGeneralRequestId(),
                    annexeId
            );

            payment.setPaymentFileDocumentId(paymentFileDocumentId);
            long paymentId = this.paymentPort.saveOrUpdate(payment);

            LocalDateTime dateNow = LocalDateTime.now();
            PaymentPlan paymentPlan = new PaymentPlan(
                    policyItemEconomic.getIndividualPremium(),
                    paymentId,
                    dateNow,
                    annexeId
            );
            this.paymentPlanPort.saveOrUpdate(paymentPlan);

            this.policyItemPort.saveOrUpdate(policyItem);

            this.policyItemEconomicPort.saveOrUpdate(policyItemEconomic);

            this.policyItemEconomicReinsurancePort.setStatusAllByPolicyItemEconomicId(
                    PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),
                    policyItemEconomic.getId());

            valuesToReplace.add(requestAnnexeCancelaltionDto.getCommentTechnicalPosition());
            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_REQUESTANNEXE_ACEPTED, valuesToReplace, valuesSubjectToReplace);
        }
        if (statusTechnicalPosition.getReferenceId().equals((long) StatusTechnicalPositionEnum.OBSERVED.getValue())) {
            requestAnnexe.setStatusIdc(RequestAnnexeStatusEnum.OBSERVED.getValue());
            ;
            valuesToReplace.add(requestAnnexeCancelaltionDto.getCommentTechnicalPosition());
            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_REQUESTANNEXE_OBSERVED, valuesToReplace, valuesSubjectToReplace);
        }
        if (statusTechnicalPosition.getReferenceId().equals((long) StatusTechnicalPositionEnum.REJECTED.getValue())) {
            requestAnnexe.setStatusIdc(RequestAnnexeStatusEnum.REJECTED.getValue());
            valuesToReplace.add(requestAnnexeCancelaltionDto.getCommentTechnicalPosition());
            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_REQUESTANNEXE_REJECTED, valuesToReplace, valuesSubjectToReplace);
        }

        alert.setMail_to(user.getEmail().concat(";").concat(alert.getMail_to()));
        messageDTO.setMessage(alert.getMail_body());
        messageDTO.setSubject(alert.getMail_subject());
        messageDTO.setSendTo(alert.getMail_to().split(";"));
        messageDTO.setSendCc(alert.getMail_cc().split(";"));
        messageDTO.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
        messageDTO.setReferenceId(requestAnnexeId);
        messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_REQUESTANNEXE.getReferenceCode());
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);

        requestAnnexe.setComment(requestAnnexeCancelaltionDto.getCommentTechnicalPosition());
        this.requestAnnexePort.saveOrUpdate(requestAnnexe);
        senderService.sendMessage(messageDTO);

        if (statusTechnicalPosition.getReferenceId().equals((long) StatusTechnicalPositionEnum.ACCEPTED.getValue())) {
            Alert alert2 = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_REQUESTANNEXE_ACEPTED_ACCOUTING, valuesToReplace, valuesSubjectToReplace);
            this.sendMail(alert2, requestAnnexeId, (int) ClassifierEnum.REFERENCE_TABLE_REQUESTANNEXE.getReferenceCode(), fileList);
        }
    }

    @Override
    public Long validateVoucherPayment(Long requestAnnexeId) {
        HelpersMethods.throwExceptionIfInvalidNumber("requestAnnexeId", requestAnnexeId, true, 0L);
        RequestAnnexe requestAnnexe = this.requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(requestAnnexeId);
        List<AnnexeDTO> annexeList = this.annexePort.getAllAnnexeByAnnexeTypeAndRequestAnnexeId(requestAnnexe.getAnnexeTypeId(), requestAnnexeId);
        if (annexeList.isEmpty()) {
            throw new OperationException("No existe una solicitud de anexo aceptada para adjuntar el comprobante de pago");
        }
        return annexeList.get(0).getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    public void saveVoucherPayment(Long annexeId, RequestSaveVoucherPaymentDto requestSaveVoucherPaymentDto) {

        HelpersMethods.throwExceptionIfInvalidNumber("voucherNumber", requestSaveVoucherPaymentDto.getVoucherNumber(), true, 0L);
        HelpersMethods.throwExceptionRequiredIfNull("voucher", requestSaveVoucherPaymentDto.getVoucher());
        HelpersMethods.throwExceptionIfInvalidNumber("requestAnnexeId", annexeId, true, 0L);

        AnnexeDTO annexeDTO = this.annexePort.findAnnexeById(annexeId);
        Policy policy = this.policyPort.findByPolicyIdOrThrowExcepcion(annexeDTO.getPolicy());
        PaymentPlan paymentPlan = this.paymentPlanPort.findByAnnexeIdOrExcepcion(annexeId);
        boolean newTransaction = false;
        Transaction transaction = this.transactionPort.findLastByPaymentPlanId(paymentPlan.getId());
        TransactionFileDocument transactionFileDocument;
        Alert alert = new Alert();
        if(transaction == null || transaction.getAmount() > 0) {
            newTransaction = true;
            transactionFileDocument = TransactionFileDocument.builder()
                    .content(requestSaveVoucherPaymentDto.getVoucher().getContent())
                    .description(requestSaveVoucherPaymentDto.getVoucher().getDescription())
                    .mimeType(requestSaveVoucherPaymentDto.getVoucher().getMimeType())
                    .documentNumber(this.paymentFileDocumentPort.getMaxNumberTransactionFileDocument().toString())
                    .documentTypeIdc(AnnulmentRequestAnnexeDocumentTypeClassifierEnum.PROOF_OF_PAYMENT.getValue())
                    .build();
            transaction = new Transaction(
                    paymentPlan.getAmount(),
                    requestSaveVoucherPaymentDto.getPayDate(),
                    policy.getCurrencyTypeIdc(),
                    paymentPlan.getId(),
                    requestSaveVoucherPaymentDto.getVoucherNumber().toString()
            );
            List<String> valuesToReplace = new ArrayList<>();
            valuesToReplace.add(policy.getNumberPolicy());

            List<String> valuesSubjectToReplace = new ArrayList<>();
            Person person = personPort.findByPolicyIdWhenPolicyAndPersonIsOneToOne(policy.getId());
            valuesSubjectToReplace.add(person.getNaturalPerson().getCompleteName());
            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_ANNEXE_CONFIRM_PAYMENT_VOUCHER, valuesToReplace, valuesSubjectToReplace);
            RequestAnnexe requestAnnexe = this.requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(annexeDTO.getRequestAnnexe());
            requestAnnexe.setStatusIdc(RequestAnnexeStatusEnum.PAID.getValue());
            requestAnnexePort.saveOrUpdate(requestAnnexe);
        } else {
            transaction.setVoucherNumber(requestSaveVoucherPaymentDto.getVoucherNumber().toString());
            transaction.setDatePaid(requestSaveVoucherPaymentDto.getPayDate());
            transactionFileDocument = this.transactionFileDocumentPort.findByTransactionFileDocumentId(transaction.getTransactionFileDocumentId());
            if(transactionFileDocument == null) {
                transactionFileDocument = TransactionFileDocument.builder()
                        .documentNumber(this.transactionFileDocumentPort.getMaxNumberTransactionFileDocument().toString())
                        .documentTypeIdc(AnnulmentRequestAnnexeDocumentTypeClassifierEnum.PROOF_OF_PAYMENT.getValue())
                        .build();
            }
            transactionFileDocument.setContent(requestSaveVoucherPaymentDto.getVoucher().getContent());
            transactionFileDocument.setDescription(requestSaveVoucherPaymentDto.getVoucher().getDescription());
            transactionFileDocument.setMimeType(requestSaveVoucherPaymentDto.getVoucher().getMimeType());
        }
        Long transactionFileDocumentId = this.transactionFileDocumentPort.saveOrUpdate(transactionFileDocument);
        transaction.setTransactionFileDocumentId(transactionFileDocumentId);
        this.transactionPort.saveOrUpdate(transaction);

        if (newTransaction) {
            this.sendMail(alert, transaction.getId(), (int) ClassifierEnum.REFERENCE_TABLE_TRANSACTION.getReferenceCode(), null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    public void updateRequirements(RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto, Long requestAnnexeId) {
        HelpersMethods.throwExceptionIfInvalidNumber("requestAnnexeId", requestAnnexeId, true, 0L);
        RequestAnnexe requestAnnexe = this.requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(requestAnnexeId);
        Date requestDate = new Date();
        LocalDateTime today = DateUtils.asDateToLocalDateTime(requestDate);
        if(requestAnnexeCancelaltionDto.getRequirements().size() > 0) {
            requestAnnexeCancelaltionDto.getRequirements().forEach(req -> {
                AnnexeRequirementControl requirementControl = this.annexeRequirementControlPort.getAnnexeRequirementControlByIdOrExcepcion(req.getId());
                Document documentAnnexe = Document.builder()
                        .mimeType(HelpersConstants.PDF)
                        .description(req.getDescription()+'_' + requestDate.getTime())
                        .content(req.getFileDocument().getContent())
                        .documentTypeIdc(this.getTypeDocumentIdc(req.getDescription()))
                        .build();
                Long documentId = this.annexeFileDocumentPort.saveOrUpdate(documentAnnexe);
                requirementControl.setFileDocumentId(documentId);
                requirementControl.setComment(req.getComment());
                requirementControl.setReceptionDate(today);
                this.annexeRequirementControlPort.saveOrUpdate(requirementControl);
            });
        }
        requestAnnexe.setStatusIdc(RequestAnnexeStatusEnum.REQUESTED.getValue());
        this.requestAnnexePort.saveOrUpdate(requestAnnexe);
    }

    //#region Auxiliary methods

    public AttachmentDTO getSettlement(Long requestAnnexeId) {
        Document finiquito = this.annexeFileDocumentPort.findRequestAnnexeIdAndAnnexeTypeIdcAndSigned(
                requestAnnexeId,
                AnnulmentRequestAnnexeDocumentTypeClassifierEnum.SETTLEMENT.getValue(),
                true
        );
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setContent(Base64.getDecoder().decode(finiquito.getContent()));
        attachmentDTO.setFileName("finiquito" + ".pdf");
        attachmentDTO.setMimeType("application/pdf");
        return attachmentDTO;
    }
    public Integer getTypeDocumentIdc(String typeDocument) {
        Integer valueTypeDocumentIdc = null ;
        if (typeDocument == null || typeDocument.isEmpty()) {
            return null;
        }
        if (typeDocument.equalsIgnoreCase(AnnexeRequirementEnum.RESCUE_REQUEST.getValue())) {
            valueTypeDocumentIdc = AnnulmentRequestAnnexeDocumentTypeClassifierEnum.RESCUE_REQUEST.getValue();
        }
        if (typeDocument.equalsIgnoreCase(AnnexeRequirementEnum.SETTLEMENT.getValue())) {
            valueTypeDocumentIdc = AnnulmentRequestAnnexeDocumentTypeClassifierEnum.SETTLEMENT.getValue();
        }
        if (typeDocument.equalsIgnoreCase(AnnexeRequirementEnum.REVERSE_AND_FRONT_CI.getValue())) {
            valueTypeDocumentIdc = AnnulmentRequestAnnexeDocumentTypeClassifierEnum.REVERSE_AND_FRONT_CI.getValue();
        }
        return valueTypeDocumentIdc;
    }

    public void sendMail(Alert alert, Long referenceId, Integer referenceTableIdc, List<AttachmentDTO> attachmentlist) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(alert.getMail_body());
        messageDTO.setSubject(alert.getMail_subject());
        messageDTO.setSendTo(alert.getMail_to().split(";"));
        messageDTO.setSendCc(alert.getMail_cc().split(";"));
        messageDTO.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
        messageDTO.setReferenceId(referenceId);
        messageDTO.setReferenceTableIdc(referenceTableIdc);
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);

        senderService.setStrategy(emailSenderService);
        if (attachmentlist != null && attachmentlist.size() > 0) {
            senderService.sendMessageWithAttachment(messageDTO, attachmentlist);
        } else {
            senderService.sendMessage(messageDTO);
        }
    }

    public AttachmentDTO generateReportPDFRamsonStatement (Policy policy, Long documentNumber) {
        DocRescueStatement docRescueStatement = this.getDataDocRescueStatement(policy);
        docRescueStatement.setDocumentNumber(documentNumber);
        byte[] ransomStatement = generatePdfUseCase.generateVINRamsonSettlement(docRescueStatement);
        String docRescueBase64 = Base64.getEncoder().encodeToString(ransomStatement);
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setContent(Base64.getDecoder().decode(docRescueBase64));
        attachmentDTO.setFileName("comunicadoRescate" + ".pdf");
        attachmentDTO.setMimeType("application/pdf");
        return attachmentDTO;
    }

    private DocRescueStatement getDataDocRescueStatement (Policy policy) {

        Account account = this.accountPort.findByPolicyId(policy.getId());

        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId());
        Person person = personPort.findById(generalRequest.getPersonId());

        PolicyItem policyItem = policyItemPort.findByPolicyIdAndPersonId(policy.getId(), person.getId());
        PolicyItemEconomic policyItemEconomic = this.policyItemEconomicPort.findLastByPolicyItemIdAndMovementTypeIdc(
                policyItem.getId(),
                PolicyMovementTypeClassifierEnum.ANNULMENT.getValue()
        );
        PolicyItemMathReserve policyItemMathReserve = this.getMathReserve(policyItem.getId(), null);
        Double insuredCapitalCeded = this.coveragePolicyItemPort.getInsuredCapitalCededByPolicyItemId(policyItem.getId());
        Double ire = this.coveragePolicyItemPort.getIreByPolicyItemId(policyItem.getId());

        return DocRescueStatement.builder()
                .nameOf("LUIS MAURICIO FRANCO MELAZZINI")
                .nameFor("JOSE LUIS SUAREZ SAUCEDO")
                .areaSend("TECNICA")
                .areaReception("CONTABILIDAD")
                .requestDate(HelpersMethods.formatStringOnlyDate(new Date()))
                .ref("DEVOLUCION POR SOLICITUD DE RESCATE")

                .paymentMethod("Abono en Cuenta Bco Fassil No")
                .numberPaymentMethod(account.getAccountNumber())
                .currencyPayment(getCurrency(policy.getCurrencyTypeIdc()))
                .policyNumber(policy.getNumberPolicy())
                .identificationNumber(person.getNaturalPerson().getIdentificationNumber()+
                        (person.getNaturalPerson().getComplement().isEmpty() ? "" : "-"+person.getNaturalPerson().getComplement()))
                .toNamePayment(person.getNaturalPerson().getCompleteName())
                .extension(getExtension(person.getNaturalPerson().getExtIdc()))

                .capitalAseguradoTotal(BigDecimal.valueOf(policyItem.getIndividualInsuredCapital()).setScale(2, RoundingMode.HALF_UP).abs())
                .valorRescateADevolver(BigDecimal.valueOf(policyItemEconomic.getIndividualPremium()).setScale(2, RoundingMode.HALF_UP).abs())
                .primaNetaRescatada(BigDecimal.valueOf(policyItemEconomic.getIndividualNetPremium()).setScale(2, RoundingMode.HALF_UP).abs())
                .primaAdicionalRescatada(BigDecimal.valueOf(policyItemEconomic.getIndividualAdditionalPremium()).setScale(2, RoundingMode.HALF_UP).abs())
                .aps(BigDecimal.valueOf(policyItemEconomic.getAPS()).setScale(2, RoundingMode.HALF_UP).abs())
                .fpa(BigDecimal.valueOf(policyItemEconomic.getFPA()).setScale(2, RoundingMode.HALF_UP).abs())
                .primaRiesgo(BigDecimal.valueOf(policyItemEconomic.getIndividualRiskPremium()).setScale(2, RoundingMode.HALF_UP).abs())
                .servicioCobranzaRescatado(BigDecimal.valueOf(policyItemEconomic.getIndividualCollectionServiceCommission()).setScale(2, RoundingMode.HALF_UP).abs())
                .primaCedidaRescatada(BigDecimal.valueOf(policyItemEconomic.getIndividualPremiumCeded()).setScale(2, RoundingMode.HALF_UP).abs())
                .comisionBrokerRescata(BigDecimal.valueOf(policyItemEconomic.getIndividualIntermediaryCommission()).setScale(2, RoundingMode.HALF_UP).abs())
                .capitalCedidoRescatado(BigDecimal.valueOf(insuredCapitalCeded).setScale(2, RoundingMode.HALF_UP).abs())
                .reservaMatematica(policyItemMathReserve == null ? new BigDecimal(0) : BigDecimal.valueOf(policyItemMathReserve.getValue()).setScale(2, RoundingMode.HALF_UP).abs())
                .impuestosRemesas(BigDecimal.valueOf(ire).setScale(2, RoundingMode.HALF_UP).abs())
                .build();
    }

    public PolicyItemMathReserve getMathReserve (Long policyItemId, Integer year) {
        Integer yearValue = null;
        if(year == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            yearValue = calendar.get(Calendar.YEAR);
        } else {
            yearValue = year;
        }
        return this.policyItemMathReservePort.findByPolicyItemIdAndYear(policyItemId, yearValue);
    }

    public double calcRescueValue(Long policyItemId, Date policyFromDate, Date requestAnnexeDate, Double totalPremium) {

        int maxDays = 30;
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);

        Long days = DateUtils.daysBetween(policyFromDate, requestAnnexeDate);
        if (days <= maxDays) {
            return totalPremium;
        }

        List<PolicyItemMathReserve> list = policyItemMathReservePort.findByPolicyItemId(policyItemId);

        PolicyItemMathReserve policyItemMathReserve = list.stream()
                .filter(e -> e.getYear().equals(today.getYear()))
                .findFirst()
                .orElse(null);


        return (policyItemMathReserve != null) ? policyItemMathReserve.getValue() : 0;
    }

    public double calcAdminExpenses(double rescueValue, int daysPassed) {
        int maxDays = 30;
        double adminExpenses = 0;

        if (daysPassed > maxDays) {
            adminExpenses = rescueValue * 0.30;
        }
        return adminExpenses;
    }

    public double calcDiscountPerDay(double rescueValue, int daysPassed, int policyDaysValidity) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

        double discountPerDay = 0;

        if (daysPassed <= 30) {
            return 0;
        }

        if (daysPassed > maxDays) {
            discountPerDay = (daysPassed - maxDays) * rescueValue / (policyDaysValidity - maxDays);
        } else {
            discountPerDay = (daysPassed * rescueValue) / policyDaysValidity;
        }
        return discountPerDay;
    }
    public String getExtension(Integer searchExt) {
        List<Classifier> extList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        Classifier ext = extList.stream()
                .filter(e -> e.getReferenceId().equals(searchExt))
                .findFirst()
                .orElse(null);

        return ext != null ? ext.getDescription() : "";
    }
    public String getCurrency(Integer searchCurrency) {
        List<Classifier> currencyList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Currency.getReferenceId());

        Classifier currency = currencyList.stream()
                .filter(e -> e.getReferenceId().intValue() == searchCurrency)
                .findFirst()
                .orElse(null);

        return currency != null ? currency.getDescription().toUpperCase() : "";
    }

    //#endregion

    //#region Validations

    public void validateProcessCancelRequestAnnexe(RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto, Long requestAnnexeId) {
        HelpersMethods.throwExceptionIfInvalidNumber("statusTechnicalPosition", requestAnnexeCancelaltionDto.getStatusTechnicalPosition(), true, 0L);
        HelpersMethods.throwExceptionIfInvalidNumber("requestAnnexeId", requestAnnexeId, true, 0L);
        HelpersMethods.throwExceptionIfInvalidNumber("policyId", requestAnnexeCancelaltionDto.getPolicyId(), true, 0L);
        HelpersMethods.throwExceptionIfInvalidNumber("annexeTypeId", requestAnnexeCancelaltionDto.getAnnexeTypeId(), true, 0L);
        if(requestAnnexeCancelaltionDto.getRequirements() == null || requestAnnexeCancelaltionDto.getRequirements().isEmpty()) {
            throw new OperationException("No se puede anular la solicitud por que no cumples con todos los requerimientos");
        }
    }

    public void validateRequestAnnexe(RequestAnnexeDTO requestAnnexeDTO) {
        OperationException.throwExceptionIfNumberInvalid("código de tipo de anexo", requestAnnexeDTO.getAnnexeTypeId());
        OperationException.throwExceptionIfNumberInvalid("motivo de anulación", requestAnnexeDTO.getAnnulmentReasonIdc().longValue());

        this.throwExceptionIfRequirementInvalid(requestAnnexeDTO.getRequestList());
        this.throwExceptionIfPolicyDetailInvalid(requestAnnexeDTO.getPolicyDetail());
        this.throwExceptionIfPlanInvalid(requestAnnexeDTO.getPlan());
        this.throwExceptionIfPersonInvalid(requestAnnexeDTO.getInsurerCompany());
    }

    public void throwExceptionIfRequirementInvalid(List<AnnexeRequirementControl> list) {
        if (list.isEmpty()) {
            throw new OperationException("No se pudo realizar la operación, la lista de requitos esta vacía");
        }
        AnnexeRequirementControl annexeRequirementControl = list.stream().findFirst().orElse(null);
        if (annexeRequirementControl.getFileDocument() == null) {
            throw new OperationException("No se pudo realizar la operación, se requiere el documento de cédula de identidad");
        }
        if (annexeRequirementControl.getFileDocument().getContent() == null || annexeRequirementControl.getFileDocument().getContent().trim().isEmpty()) {
            throw new OperationException("No se pudo realizar la operación, se requiere el documento de cédula de identidad");
        }
    }

    public void throwExceptionIfPolicyDetailInvalid(RequestPolicyDetailDto policyDetail) {
        if (policyDetail == null) {
            throw new OperationException("No se pudo realizar la operación, no se encontró el detalle de la póliza");
        }
    }

    public void throwExceptionIfPlanInvalid(Plan plan) {
        if (plan == null) {
            throw new OperationException("No se pudo realizar la operación, no se encontraron los datos del plan");
        }
    }

    public void throwExceptionIfPersonInvalid(Person person) {
        if (person == null) {
            throw new OperationException("No se pudo realizar la operación, no se encontraron los datos de la compañia aseguradora");
        }
    }

    public void throwExceptionIfInvalidFilters(RequestAnnexeSearchFiltersDto filtersDto) throws ParseException {
        if (filtersDto != null) {
            if (filtersDto.getFromDate() != null || filtersDto.getToDate() != null) {
                if(filtersDto.getToDate() == null ) {
                    throw new OperationException("El campo fecha final no debe estar vacio");
                }
                if(filtersDto.getFromDate() == null) {
                    throw new OperationException("El campo fecha de inicio no debe estar vacio");
                }
                Date initDate = filtersDto.getFromDate();
                Date endDate= filtersDto.getToDate();
                if(initDate.after(endDate)){
                    throw new OperationException("La fecha de inicio no puede ser mayor a la fecha de vencimiento");
                }
            }
        }
    }

    //#endregion

}
