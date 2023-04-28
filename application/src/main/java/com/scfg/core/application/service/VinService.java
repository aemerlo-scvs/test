package com.scfg.core.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.scfg.core.application.port.in.AcceptanceRequestUseCase;
import com.scfg.core.application.port.in.ProcessRequestWithoutSubscriptionUseCase;
import com.scfg.core.application.port.in.VinUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.application.service.sender.EmailSenderService;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.application.service.sender.SmsSenderService;
import com.scfg.core.application.service.sender.WhatsAppSenderService;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.*;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.*;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;
import com.scfg.core.domain.dto.vin.*;
import com.scfg.core.domain.person.NaturalPerson;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class VinService implements ProcessRequestWithoutSubscriptionUseCase, VinUseCase, AcceptanceRequestUseCase {

    private final ClassifierPort classifierPort;
    private final PersonPort personPort;
    private final DirectionPort directionPort;
    private final TelephonePort telephonePort;
    private final GeneralRequestPort generalRequestPort;
    private final PolicyPort policyPort;
    private final PlanPort planPort;
    private final PolicyItemPort policyItemPort;
    private final CoveragePolicyItemPort coveragePolicyItemPort;
    private final BeneficiaryPort beneficiaryPort;
    private final AccountPort accountPort;
    private final PaymentPort paymentPort;
    private final PaymentPlanPort paymentPlanPort;
    private final TransactionPort transactionPort;
    private final AccountPolicyPort accountPolicyPort;
    private final SenderService senderService;
    private final EmailSenderService emailSenderService;
    private final SmsSenderService smsSenderService;
    private final WhatsAppSenderService whatsAppSenderService;
    private final AlertService alertService;
    private final ProductCalculationsService productCalculationsService;
    private final BrokerPort brokerPort;
    private final CoverageProductPlanPort coverageProductPlanPort;
    private final MessageToSendPort messageToSendPort;
    private final MessageSentPort messageSentPort;
    private final VinPort vinPort;
    private final GenerateReportsService excelService;
    private final MessageResponsePort messageResponsePort;
    private final FileDocumentPort fileDocumentPort;
    private final PolicyItemEconomicPort policyItemEconomicPort;
    private final PolicyItemEconomicReinsurancePort policyItemEconomicReinsurancePort;
    private final Environment environment;
    private Gson gson;
    private final String[] urls = new String[]{"http://localhost:4201",
            "https://www.santacruzvidaysalud.com.bo/segurosinclusivospre",
            "https://www.santacruzvidaysalud.com.bo/segurosinclusivos"};
    private final String proposalResponse = "/vin/resp/";
    private final String proposalResponseWeb = "/vin/resp-web/";
    private final String sendMails = "ccuellars@santacruzfg.com;gaguilar@santacruzfg.com";
    private final String sendTelephone = "79855300";
    private final String hpBroker = "HP BROKERS";


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public void processRequest(Object request) {

        gson = new Gson();
        double intermediaryCommissionPercentage = 0.0001;
        List<CoveragePolicyItem> coveragePolicyItemList = new ArrayList<>();
        List<PolicyItemEconomicReinsurance> policyItemEconomicReinsuranceList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            byte[] json = mapper.writeValueAsBytes(request);
            VinProcessRequestDTO processRequest = mapper.readValue(json, VinProcessRequestDTO.class);
            long personId = 0L;

            //#region Validación de calidad de información

            Person personAux = personPort.findByIdentificationNumberAndType(processRequest.getPerson().getNaturalPerson()
                                                                            .getIdentificationNumber(),
                                                                            processRequest.getPerson().getNaturalPerson()
                                                                            .getDocumentType());

            //#endregion

            if (personAux == null) {
                Person person = processRequest.getPerson();
                person.setTelephone(processRequest.getSelectedCellphoneNumber());
                personId = personPort.saveOrUpdate(person);
                personAux = person;
                personAux.setId(personId);
            } else {
                personAux.setTelephone(processRequest.getSelectedCellphoneNumber());
                NaturalPerson naturalPerson = processRequest.getPerson().getNaturalPerson();
                naturalPerson.setId(personAux.getNaturalPerson().getId());
                naturalPerson.setExtIdc(processRequest.getPerson().getNaturalPerson().getExtIdc());
                naturalPerson.setMarriedLastName(processRequest.getPerson().getNaturalPerson().getMarriedLastName());
                naturalPerson.setMaritalStatusIdc(processRequest.getPerson().getNaturalPerson().getMaritalStatusIdc());
                personAux.setNaturalPerson(naturalPerson);
                personAux.setEmail(processRequest.getPerson().getEmail());
                personId = personPort.saveOrUpdate(personAux);
            }

            if (processRequest.getPerson().getDirection() != null) {
                List<Direction> actDirectionList = directionPort.findAllByPersonId(personId);

                if (!actDirectionList.stream()
                        .map(o -> o.getDescription().equals(processRequest.getPerson().getDirection().getDescription()))
                        .findAny().isPresent()) {
                    Direction direction = processRequest.getPerson().getDirection();
                    direction.setPersonId(personId);
                    directionPort.saveOrUpdate(direction);
                }
            }

            List<Telephone> actTelephoneList = telephonePort.getAllByPersonId(personId);

            List<Telephone> telephoneList = new ArrayList<>();
            for (String telephoneAct : processRequest.getCellphoneNumbers()) {
                if (!actTelephoneList.stream().map(o -> o.getNumber().equals(telephoneAct)).findAny().isPresent()) {
                    Telephone telephone = new Telephone(personId, telephoneAct, (int) ClassifierEnum.NUMBER_TYPE_CEL.getReferenceCode());
                    telephoneList.add(telephone);
                }
            }

            telephonePort.saveOrUpdateAll(telephoneList);

            Plan plan = planPort.getPlanByAgreementCodePlandAndAgreementCodeProduct(processRequest.getPlanCode(),processRequest.getProductCode());

            //#region Verificación para cancelación automatica de propuestas anteriores con mismo número de operación

            if (!validateProposalToCancel(processRequest.getOperationNumber(), plan.getId())) {
                throw new OperationException("El número de operación ya se encuentra con un certificado generado");
            }

            //#endregion

            GeneralRequest generalRequest = new GeneralRequest(processRequest, RequestStatusEnum.PENDING.getValue(), personId, plan.getId());
            generalRequest.setRequestNumber(generalRequestPort.nextSequency(ProductEnum.VIN.getValue(), plan.getName()));
            long requestId = generalRequestPort.saveOrUpdate(generalRequest);
            long productId = plan.getProductId();
            double individualPremium = processRequest.getTotalPremium();
            List<BrokerDTO> brokerList = this.brokerPort.getAllBrokers();
            BrokerDTO brokerDTO = new BrokerDTO();
            for (BrokerDTO br: brokerList) {
                if (br.getBusinessName().contains(hpBroker)) {
                    brokerDTO = br;
                }
            }

            Policy policy = new Policy(requestId,processRequest.getCurrencyTypeIdc(),processRequest.getTotalPremium(),0.0,0,productId,processRequest.getTermInDays()); //Validar el tipo de moneda a registrar en poliza, prima total, caital asegurado
            policy.setBrokerId(brokerDTO.getId());
            policy.setIntermediaryCommissionPercentage(intermediaryCommissionPercentage);
            Policy policyAux = policyPort.saveOrUpdate(policy);
            PolicyItem policyItem = new PolicyItem(policyAux, personId, requestId, individualPremium, processRequest.getCoverages());
            policyItem = policyItemPort.saveOrUpdate(policyItem);

            PolicyItemEconomic policyItemEconomic = new PolicyItemEconomic(policyItem.getId(),
                    PolicyMovementTypeClassifierEnum.PRODUCTION.getValue(), policy.getBrokerId(), individualPremium);

            double totalPremiumCeded = productCalculationsService.calcPolicyItemEconomicReinsuranceTotalPremiumCededVIN(
                    null, processRequest.getTermInYears(), policy.getFromDate(),
                    policy.getToDate(), policy.getFromDate());

            productCalculationsService.calcPolicyItemEconomicVIN(policyItemEconomic,null,
                    totalPremiumCeded, processRequest.getTermInYears(), policy.getIntermediaryCommissionPercentage(),
                    policy.getFromDate(), policy.getToDate(), policy.getFromDate());

            policyItemEconomic = policyItemEconomicPort.saveOrUpdate(policyItemEconomic);


            for (CoverageDTO coverage : processRequest.getCoverages()) {
                CoveragePolicyItem coveragePolicyItem = new CoveragePolicyItem(coverage, policyItem.getId());
                coveragePolicyItemList.add(coveragePolicyItem);

                PolicyItemEconomicReinsurance policyItemEconomicReinsurance = new PolicyItemEconomicReinsurance(
                        policyItemEconomic.getId(), coverage.getCoverageId());
                productCalculationsService.calcPolicyItemEconomicReinsuranceVIN(policyItemEconomicReinsurance, null,
                        policyItemEconomic.getMovementTypeIdc(), coverage, policyItemEconomic.getIndividualNetPremium(),
                        processRequest.getTermInYears(), policy.getFromDate(), policy.getToDate(), policy.getFromDate());
                policyItemEconomicReinsuranceList.add(policyItemEconomicReinsurance);
            }

            coveragePolicyItemPort.saveOrUpdateAll(coveragePolicyItemList);
            policyItemEconomicReinsurancePort.saveOrUpdateAll(policyItemEconomicReinsuranceList);

            for (Beneficiary beneficiary : processRequest.getBeneficiaries()) {
                beneficiary.setPolicyItemId(policyItem.getId());
                beneficiary.setPolicyId(policyItem.getPolicyId());
            }

            beneficiaryPort.saveAll(processRequest.getBeneficiaries());

            List<Account> actAccountList = accountPort.findAllByPersonId(personId);
            Account account = Account.builder()
                    .accountNumber(processRequest.getSelectedAccount().getAccountNumber())
                    .accountTypeIdc(processRequest.getSelectedAccount().getAccountType())
                    .accountCurrencyTypeIdc(processRequest.getSelectedAccount().getAccountType())
                    .personId(personId)
                    .build();

            Account finalAccount = account;
            Account accountAux = actAccountList.stream()
                    .filter(o -> o.getAccountNumber().equals(finalAccount.getAccountNumber()) &&
                            o.getAccountTypeIdc().equals(finalAccount.getAccountTypeIdc()))
                    .findAny()
                    .orElse(null);

            if (accountAux == null) {
                account = accountPort.saveOrUpdate(account);
            } else {
                account = accountAux;
            }

            AccountPolicy accountPolicy = AccountPolicy.builder()
                    .accountId(account.getId())
                    .policyId(policyAux.getId())
                    .typeIdc((int) ClassifierEnum.TYPE_TRAN_CLT_PAYMENT.getReferenceCode())
                    .build();

            accountPolicy = accountPolicyPort.saveOrUpdate(accountPolicy);

            //#region Envio de mensaje

            Classifier moneyType = classifierPort.getClassifierByReferencesIds(processRequest.getCurrencyTypeIdc(),
                                                    ClassifierTypeEnum.Currency.getReferenceId());

            Classifier extensionCi = classifierPort.getClassifierByReferencesIds(
                    personAux.getNaturalPerson().getExtIdc(), ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());

            Alert alert = new Alert();
            MessageDTO messageDTO = new MessageDTO();
            if (processRequest.getSendBy() == MessageTypeEnum.EMAIL.getValue()) {

                if (processRequest.getPerson().getEmail() == null || processRequest.getPerson().getEmail().isEmpty()) {
                    throw new OperationException("Debe tener un correo electrónico para enviar el mensaje por el medio seleccionado");
                }

                senderService.setStrategy(emailSenderService);

                String htmlCov = "";
                for (CoverageDTO cov: processRequest.getCoverages()) {
                    htmlCov = htmlCov + "<tr><td>" + cov.getCoverageName() + "</td><td>";
                    htmlCov = htmlCov + moneyType.getAbbreviation() + HelpersMethods.convertNumberToCompanyFormatNumber(
                                                    cov.getInsuredCapitalCoverage()) + "</td></tr>";
                }

                String htmlBen = "";
                int benCount = 1;
                for (Beneficiary beneficiary: processRequest.getBeneficiaries()) {
                    htmlBen = htmlBen + "<tr><td>Beneficiario " + benCount + ": </td><td>";
                    htmlBen = htmlBen + beneficiary.getFullName() + "</td><td> Porcentaje: ";
                    htmlBen = htmlBen + beneficiary.getPercentage() + "</td><td> Relación: ";
                    Classifier classifier = classifierPort.getClassifierByReferencesIds(beneficiary.getRelationshipIdc(),
                            ClassifierTypeEnum.Relationship.getReferenceId());
                    htmlBen = htmlBen + classifier.getDescription() + "</td></tr>";
                    benCount++;
                }

                List<String> valuesToReplace = new ArrayList<>();
                valuesToReplace.add(processRequest.getPerson().getNaturalPerson().getCompleteName());
                valuesToReplace.add(htmlCov);
                valuesToReplace.add(processRequest.getTermInYears().toString());//Valor a reemplazar
                valuesToReplace.add(HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                                    processRequest.getTotalPremium(),moneyType.getAbbreviation()));
                valuesToReplace.add(processRequest.getInsurerCompany().getJuridicalPerson().getName().toUpperCase());
                valuesToReplace.add(htmlBen);

                String urlSet = "";
                String urlSetFalse = "";

                MessageDecideResponseDTO responseTrue = MessageDecideResponseDTO.builder()
                        .referenceTable((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode())
                        .response(1)
                        .requestId(requestId)
                        .build();

                String jsonStringTrue = responseTrue.toString();
                String sendTrue = Base64.getEncoder().encodeToString(jsonStringTrue.getBytes());

                MessageDecideResponseDTO responseFalse = MessageDecideResponseDTO.builder()
                        .referenceTable((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode())
                        .response(2)
                        .requestId(requestId)
                        .build();

                String jsonStringFalse = responseFalse.toString();
                String sendFalse = Base64.getEncoder().encodeToString(jsonStringFalse.getBytes());

                if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                    urlSet = urls[2];
                    urlSet += proposalResponse + sendTrue;
                    valuesToReplace.add(urlSet);

                    urlSetFalse = urls[2];
                    urlSetFalse += proposalResponse + sendFalse;
                    valuesToReplace.add(urlSetFalse);
                } else if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
                    urlSet = urls[1];
                    urlSet += proposalResponse + sendTrue;
                    valuesToReplace.add(urlSet);

                    urlSetFalse = urls[1];
                    urlSetFalse += proposalResponse + sendFalse;
                    valuesToReplace.add(urlSetFalse);
                } else {
                    urlSet = urls[0];
                    urlSet += proposalResponse + sendTrue;
                    valuesToReplace.add(urlSet);

                    urlSetFalse = urls[0];
                    urlSetFalse += proposalResponse + sendFalse;
                    valuesToReplace.add(urlSetFalse);
                }

                valuesToReplace.add(APSCodesEnum.APS_PROPOSAL_VIN_CODE.getValue()+"");
                valuesToReplace.add(processRequest.getHolderCompany().getJuridicalPerson().getName().toUpperCase());
                valuesToReplace.add(HelpersMethods.formatBankAccountNumber(processRequest.getSelectedAccount().getAccountNumber()));
                Date dateNowMail = new Date();
                valuesToReplace.add(HelpersMethods.formatStringOnlyDate(dateNowMail));
                valuesToReplace.add(HelpersMethods.formatStringOnlyHourAndMinute(dateNowMail));

                String identificationAndCmp = personAux.getNaturalPerson().getIdentificationNumber();
                if (!personAux.getNaturalPerson().getComplement().isEmpty()) {
                    identificationAndCmp = identificationAndCmp+"-"+personAux.getNaturalPerson().getComplement();
                }
                String ext = "N/A";
                if (personAux.getNaturalPerson().getExtIdc() != null) {
                    ext = extensionCi.getDescription().toUpperCase();
                }
                valuesToReplace.add(identificationAndCmp);
                valuesToReplace.add(ext);
                valuesToReplace.add("correo electrónico ");
                valuesToReplace.add(personAux.getEmail());
                valuesToReplace.add(brokerDTO.getBusinessName().toUpperCase());

                alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_ACTIVATION_CONFIRM_PROPOSAL, valuesToReplace);

                if (!processRequest.getPerson().getEmail().isEmpty()) {
                    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                        messageDTO.setTo(sendMails);
                        messageDTO.setSendTo(sendMails.split(";"));
                    } else {
                        messageDTO.setTo(processRequest.getPerson().getEmail());
                        messageDTO.setSendTo(processRequest.getPerson().getEmail().split(";"));
                    }
                }

                if (!alert.getMail_cc().isEmpty()) {
                    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                        messageDTO.setCc(";");
                        String replace = ";";
                        messageDTO.setSendCc(replace.split(";"));
                    } else {
                        messageDTO.setCc(alert.getMail_cc());
                        messageDTO.setSendCc(alert.getMail_cc().split(";"));
                    }
                }

                messageDTO.setMessage(alert.getMail_body());
                messageDTO.setSubject(alert.getMail_subject());
                messageDTO.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
                messageDTO.setReferenceId(requestId);
                messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
                messageDTO.setNumberOfAttempt(0);
                messageDTO.setLastNumberOfAttempt(0);
            }

            if (processRequest.getSendBy() == MessageTypeEnum.WHATSAPP.getValue()) {
                senderService.setStrategy(whatsAppSenderService);
            }

            if (processRequest.getSendBy() == MessageTypeEnum.SMS.getValue()) {

                if (processRequest.getSelectedCellphoneNumber() == null || processRequest.getSelectedCellphoneNumber().isEmpty()) {
                    throw new OperationException("Debe tener un número de teléfono celular para enviar el mensaje por el medio seleccionado");
                }

                senderService.setStrategy(smsSenderService);
                List<String> valuesToReplace = new ArrayList<>();
                String urlSet = "";
                String requestJson = "{" +
                        "\"generalRequestId\": " + requestId +
                        "}";
                String conversor = Base64.getEncoder().encodeToString(requestJson.getBytes());
                if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                    urlSet = urls[2];
                } else if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
                    urlSet = urls[1];
                } else {
                    urlSet = urls[0];
                }

                urlSet += proposalResponseWeb + conversor;
                valuesToReplace.add(urlSet);
                alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_ACTIVATION_CONFIRM_PROPOSAL_SMS,valuesToReplace);

                if (!processRequest.getSelectedCellphoneNumber().isEmpty()) {
                    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                        messageDTO.setTo(sendTelephone);
                    } else {
                        messageDTO.setTo(processRequest.getSelectedCellphoneNumber());
                    }
                }

                messageDTO.setMessage(alert.getMail_body());
                messageDTO.setSubject(alert.getMail_subject());
                messageDTO.setMessageTypeIdc(MessageTypeEnum.SMS.getValue());
                messageDTO.setReferenceId(requestId);
                messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
                messageDTO.setNumberOfAttempt(0);
                messageDTO.setLastNumberOfAttempt(0);
            }

            senderService.sendMessage(messageDTO);

            //#endregion

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OperationException e) {
            throw new OperationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public void cancelPolicy(RequestCancelOperationDTO requestCancelOperationDTO) {

        double factor = -1;
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);

        validateCancelOperation(requestCancelOperationDTO);

        List<Integer> operationNumbers = generalRequestPort.getIfExistOperationNumbers(requestCancelOperationDTO.getNro_operacion());

        if(operationNumbers.isEmpty()){
            throw new OperationException("No se pudo realizar la operación, el número de operación no existe");
        } else if (operationNumbers.size() > 1) {
            throw new OperationException("No se pudo realizar la operación, número de operación duplicado");
        }

        Policy policy = policyPort.findByOperationNumber(requestCancelOperationDTO.getNro_operacion());

        long t = ChronoUnit.HOURS.between(DateUtils.asDateToLocalDateTime(policy.getLastModifiedAt()), today);
        if(t > 24 ) {
            throw  new OperationException("No se pudo realizar la operación, han pasado más de 24 Horas desde que se generó el certificado de cobertura");
        }
        if(policy.getPolicyStatusIdc() == PolicyStatusEnum.CANCELED.getValue())
        {
            throw  new OperationException("No se pudo realizar la operación, la póliza ya se encuentra anulada");
        }
        policy.setPolicyStatusIdc(PolicyStatusEnum.CANCELED.getValue());

        Payment payment = paymentPort.findByGeneralRequest(policy.getGeneralRequestId());
        PaymentPlan paymentPlan = new PaymentPlan(policy.getTotalPremium() * factor, payment.getId(), today,
                payment.getCreatedBy(), payment.getLastModifiedBy());
        long paymentPlanId = paymentPlanPort.saveOrUpdate(paymentPlan);
        Transaction transaction = new Transaction(policy.getTotalPremium() * factor, today,
                policy.getCurrencyTypeIdc(), paymentPlanId, requestCancelOperationDTO.getNro_comprobante().toString(),
                payment.getCreatedBy(), payment.getLastModifiedBy());

        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId());
        PolicyItem policyItem = policyItemPort.findByPolicyIdAndPersonId(policy.getId(), generalRequest.getPersonId());
        PolicyItem anulmmentPolicyItem = new PolicyItem(policyItem);
        anulmmentPolicyItem.setTermValidity(DateUtils.asDate(today));
        PolicyItemEconomic policyItemEconomic = policyItemEconomicPort.findLastByPolicyItemIdAndMovementTypeIdc(
                policyItem.getId(), PolicyMovementTypeClassifierEnum.PRODUCTION.getValue(), PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        PolicyItemEconomic anulmmentPolicyItemEconomic = new PolicyItemEconomic(policyItemEconomic);

        List<PolicyItemEconomicReinsurance> policyItemEconomicReinsuranceList = policyItemEconomicReinsurancePort
                .findAllByPolicyItemEconomicId(policyItemEconomic.getId());
        List<PolicyItemEconomicReinsurance> anulmmentPolicyItemEconomicReinsuranceList = new ArrayList(policyItemEconomicReinsuranceList);

        anulmmentPolicyItemEconomic.setId(0L);
        anulmmentPolicyItemEconomic.setMovementTypeIdc(PolicyMovementTypeClassifierEnum.ANNULMENT.getValue());
        productCalculationsService.multiplyWithFactor(anulmmentPolicyItemEconomic, factor);

        PolicyItemEconomic tempAnulmmentPolicyItemEconomic = policyItemEconomicPort.saveOrUpdate(anulmmentPolicyItemEconomic);
        anulmmentPolicyItemEconomicReinsuranceList.forEach(o -> {
            o.setId(0L);
            o.setPremiumCeded(o.getPremiumCeded() * factor);
            o.setPolicyItemEconomicId(tempAnulmmentPolicyItemEconomic.getId());
        });

        transactionPort.saveOrUpdate(transaction);
        policyPort.saveOrUpdate(policy);
        policyItemPort.saveOrUpdate(anulmmentPolicyItem);
        policyItemEconomicReinsurancePort.saveOrUpdateAll(anulmmentPolicyItemEconomicReinsuranceList);

        duplicateDataFromReversion(policy, generalRequest, policyItem, policyItemEconomic,
                policyItemEconomicReinsuranceList);
    }

    @Override
    public boolean isOperationDetailValid(OperationDetailDTO operationDetailDTO) {
        this.validateOperationDetailDTO(operationDetailDTO);

        boolean existOperationNumber = this.generalRequestPort.existOperationNumber(operationDetailDTO.getNro_operacion(),
                ProductAgreementCodeEnum.VIN.getValue(), AgreementCodePlanFBS.VIN_CODE.getValue());

        if (!existOperationNumber) {
            throw new OperationException("El número de operación no existe");
        }

        RequestDetailOperationDTO request = new RequestDetailOperationDTO(operationDetailDTO);

        Classifier ext = existsCIExt(operationDetailDTO.getExtension());

        if (ext != null) {
            request.setExtensionIdc(ext.getReferenceId().intValue());
        }

        VinDetailOperationDTO vinOperationDetailDTO = this.generalRequestPort.getDetailOperation(request);
        if (vinOperationDetailDTO == null) {
            throw new OperationException("No se encontro el detalle de la operacion");
        }

        if (vinOperationDetailDTO.getRequestStatusIdc() != RequestStatusEnum.FINALIZED.getValue()) {
            throw new OperationException("La propuesta debe ser aceptada, para generar el certificado de cobertura");
        }

        List<FileDocumentByRequestDTO> certificateList = fileDocumentPort.getCertificateCoverageDocumentByPolicyItemId(vinOperationDetailDTO.getPolicyItemId());

        if (!certificateList.isEmpty()) {
            throw new OperationException("Ya cuenta con un certificado de cobertura generado");
        }

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public void reSend(Long requestId, Integer reSendBy, String to) {
        boolean toSent = false;
        boolean send = false;
        MessageDTO messageDTO = new MessageDTO();
        messageDTO = messageToSendPort
                    .findByReferenceIdAndTableReferenceIdc(requestId,
                            (int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
        toSent = (messageDTO != null);

        if (!toSent) {
            messageDTO = messageSentPort
                        .findByReferenceIdAndTableReferenceIdc(requestId,
                        (int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
            send = (messageDTO != null);
        }

        if (!toSent && !send) {
            throw new OperationException("No se puede reenviar el mensaje, ya que no existe un primer mensaje");
        }

        if (!messageDTO.getSubject().contains("- Reenvío")) {
            messageDTO.setSubject(messageDTO.getSubject() + " - Reenvío");
        }

        if (toSent) {
            messageToSendPort.delete(messageDTO.getId());
            messageDTO.setId(0L);
            messageDTO.setObservation("Mensaje no enviado / Reenvío de mensaje nuevo");
            messageSentPort.saveOrUpdate(messageDTO);
        }

        if (send) {
            messageDTO.setId(0L);
        }

        messageDTO.setTo(to);
        if(messageDTO.getMessageTypeIdc() == MessageTypeEnum.EMAIL.getValue()){
            messageDTO.setSendTo(to.split(";"));
            messageDTO.setSendCc(";".split(";"));
        }

        if (messageDTO.getMessageTypeIdc() == reSendBy) {
            reSendSimpleMessage(messageDTO);
        } else {
            messageDTO.setMessageTypeIdc(reSendBy);
            reSendComplexMessage(messageDTO);
        }
    }

    @Override
    public VinDetailOperationDTO getOperationDetail(RequestDetailOperationDTO detailOperationDTO) {
        HelpersMethods.throwExceptionIfInvalidTexts("nro de operación", detailOperationDTO.getNro_operacion(), true);
        HelpersMethods.throwExceptionIfInvalidTexts("nro de documento", detailOperationDTO.getNro_documento(), true);
        HelpersMethods.throwExceptionIfInvalidTexts("complemento", detailOperationDTO.getComplemento(), false);
        HelpersMethods.throwExceptionIfInvalidNumber("tipo de documento", detailOperationDTO.getTipo_documento(), true, 0);
        if (detailOperationDTO.getExtensionIdc() == null) {
            detailOperationDTO.setExtensionIdc(0);
        }
        VinDetailOperationDTO vinDetailOperationDTO = this.generalRequestPort.getDetailOperation(detailOperationDTO);
        if (vinDetailOperationDTO == null) {
            throw new OperationException("No se encontro el detalle de la operación");
        }
        List<Beneficiary> beneficiaryList = this.beneficiaryPort.findAllByPolicyItemId(vinDetailOperationDTO.getPolicyItemId());
        vinDetailOperationDTO.setBeneficiaryList(beneficiaryList);
        return vinDetailOperationDTO;
    }

    @Override
    public FileDocumentDTO generateProductionReport(VinReportFilterDTO filterDTO) {
        if (filterDTO.getFromDate() == null && filterDTO.getToDate() == null) {
            throw new OperationException("Los campos fecha desde y fecha hasta son obligatorios");
        }
        if (filterDTO.getPolicyStatusIdc() == null || filterDTO.getPolicyStatusIdc() <= 0) {
            filterDTO.setPolicyStatusIdc(-1);
        }

        filterDTO.setFromDate(DateUtils.formatToStartOrNull(filterDTO.getFromDate()));
        filterDTO.setToDate(DateUtils.formatToEnd(filterDTO.getToDate()));

        List<Object> list = vinPort.getProductionReport(filterDTO);
        if (list.isEmpty()) {
            throw new OperationException("No se encontraron resultados");
        }
        FileDocumentDTO file = this.excelService.generateExcelFileDocumentDTOFromRawObject(headersReport(1),list,"Reporte-Produccion-VIN");
        return file;
    }

    @Override
    public FileDocumentDTO generateCommercialReport(VinReportFilterDTO filterDTO) {
        if (filterDTO.getFromDate() == null && filterDTO.getToDate() == null) {
            throw new OperationException("Los campos fecha desde y fecha hasta son obligatorios");
        }
        if (filterDTO.getPolicyStatusIdc() == null || filterDTO.getPolicyStatusIdc() <= 0) {
            filterDTO.setPolicyStatusIdc(-1);
        }

        filterDTO.setFromDate(DateUtils.formatToStartOrNull(filterDTO.getFromDate()));
        filterDTO.setToDate(DateUtils.formatToEnd(filterDTO.getToDate()));

        List<Object> list = vinPort.getCommercialReport(filterDTO);
        if (list.isEmpty()) {
            throw new OperationException("No se encontraron resultados");
        }
        FileDocumentDTO file = this.excelService.generateExcelFileDocumentDTOFromRawObject(headersReport(2),list,"Reporte-Ventas-VIN");
        return file;
    }

    @Override
    public VinProposalDetail getProposalDetail(Long requestId) {
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(requestId);
        PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(generalRequest.getId());
        Person person = personPort.findById(generalRequest.getPersonId());
        Policy policy = policyPort.getByRequestIdOneData(generalRequest.getId());
        List<CoverageDTO> coverageDTOList = coverageProductPlanPort.findAllByPolicyItemId(policyItem.getId());
        List<Beneficiary> beneficiaryList = beneficiaryPort.findAllByPolicyItemId(policyItem.getId());
        Person insurerCompany = personPort.findByNitNumber((long) CompanysNitNumber.SCVS.getValue());
        Person holderCompany = personPort.findByNitNumber((long) CompanysNitNumber.FBS.getValue());
        List<Classifier>  moneyType = classifierPort.getAllWithDetailByClassifierTypeReferenceId(ClassifierTypeEnum.Currency.getReferenceId());
        List<Classifier>  ext = classifierPort.getAllWithDetailByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        Account account = accountPort.findLastByPersonIdAndPolicyId(person.getId(), policy.getId());
        List<Classifier> relaList = classifierPort.getAllWithDetailByClassifierTypeReferenceId(ClassifierTypeEnum.Relationship.getReferenceId());
        List<BrokerDTO> brokerList = this.brokerPort.getAllBrokers();
        BrokerDTO brokerDTO = new BrokerDTO();
        for (BrokerDTO br: brokerList) {
            if (br.getBusinessName().contains(hpBroker)) {
                brokerDTO = br;
            }
        }

        VinProposalDetail vinProposalDetail = VinProposalDetail.builder()
                .request(generalRequest)
                .coverageList(coverageDTOList)
                .policyItem(policyItem)
                .insurer(person)
                .policy(policy)
                .beneficiaryList(beneficiaryList)
                .insurerCompany(insurerCompany)
                .holderCompany(holderCompany)
                .extList(ext)
                .relationShipList(relaList)
                .moneyTypeList(moneyType)
                .account(account)
                .broker(brokerDTO)
                .build();
        return vinProposalDetail;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    public Boolean acceptanceRequest(MessageDecideResponseDTO messageResponseDTO) {
        boolean resp = false;

        MessageDTO message = messageSentPort.findByReferenceIdAndTableReferenceIdc(messageResponseDTO.getRequestId(), messageResponseDTO.getReferenceTable());

        GeneralRequest request = generalRequestPort.getGeneralRequest(messageResponseDTO.getRequestId());

        if (request.getRequestStatusIdc() != RequestStatusEnum.PENDING.getValue()) {
            throw new OperationException("Está solicitud ya fue atendida");
        }

        if (messageResponseDTO.getResponse() == 1 ) {
            request.setRequestStatusIdc(RequestStatusEnum.FINALIZED.getValue());
        } else {
            request.setRequestStatusIdc(RequestStatusEnum.REJECTED.getValue());
        }

        MessageResponse messageResponse = MessageResponse.builder()
                .messageSentId(message.getId())
                .message(messageResponseDTO.getResponse() == 1 ? "SI" : "NO")
                .from(message.getTo())
                .build();

        long messageResponseId = messageResponsePort.saveOrUpdate(messageResponse);

        Person person = personPort.findById(request.getPersonId());
        List<String> valuesToReplace = new ArrayList<>();
        valuesToReplace.add(person.getNaturalPerson().getCompleteName());

        long req = generalRequestPort.saveOrUpdate(request);
        if (req > 0) {
            resp = messageResponseDTO.getResponse() == 1 ? true : false;


            Alert alert = alertService.getAlertByEnumReplacingContent(
                    messageResponseDTO.getResponse() == 1 ? AlertEnum.VIN_ACCEPT_PROP : AlertEnum.VIN_REJECT_PROP, valuesToReplace
            );
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage(alert.getMail_body());
            messageDTO.setSubject(alert.getMail_subject());
            messageDTO.setMessageTypeIdc(message.getMessageTypeIdc());
            messageDTO.setReferenceId(messageResponseId);
            messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_MESSAGERESPONSE.getReferenceCode());
            messageDTO.setNumberOfAttempt(0);
            messageDTO.setLastNumberOfAttempt(0);

            if (message.getMessageTypeIdc() == MessageTypeEnum.EMAIL.getValue()) {
                senderService.setStrategy(emailSenderService);

                if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                    messageDTO.setTo(sendMails);
                    messageDTO.setSendTo(sendMails.split(";"));
                    messageDTO.setCc(";");
                    messageDTO.setSendCc(message.getCc().split(";"));
                } else {
                    messageDTO.setTo(message.getTo());
                    messageDTO.setSendTo(message.getTo().split(";"));
                    messageDTO.setCc(message.getCc());
                    messageDTO.setSendCc(message.getCc().split(";"));
                }
            }

            if (message.getMessageTypeIdc() == MessageTypeEnum.WHATSAPP.getValue()) {
                senderService.setStrategy(whatsAppSenderService);
            }

            if (message.getMessageTypeIdc() == MessageTypeEnum.SMS.getValue()) {
                senderService.setStrategy(smsSenderService);

                if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                    messageDTO.setTo(sendTelephone);
                } else {
                    messageDTO.setTo(message.getTo());
                }
            }

            senderService.sendMessage(messageDTO);
        }
        return resp;
    }

    //#region Auxiliary Methods

    private List<String> headersReport(int option) {
        List<String> headers = new ArrayList<>();
        if (option == 1) {
            headers.add("Póliza");
            headers.add("Año");
            headers.add("MES");
            headers.add("Fecha");
            headers.add("Tipo de Movimiento");
            headers.add("SubTipo de Movimiento");
            headers.add("Moneda");
            headers.add("Regional SCVS");
            headers.add("Sucursal BFS");
            headers.add("Agencia BFS");
            headers.add("Ramo");
            headers.add("Producto");
            headers.add("Plan");
            headers.add("Código Cliente");
            headers.add("Tomador");
            headers.add("Asegurado");
            headers.add("ACTIVIDAD");
            headers.add("No. Identificacion");
            headers.add("Extensión");
            headers.add("Nacionalidad");
            headers.add("Fecha de Nacimiento");
            headers.add("Edad");
            headers.add("Género");
            headers.add("Estado Civil");
            headers.add("DIRECCION");
            headers.add("Beneficiario");
            headers.add("Fecha de Inicio de Vigencia");
            headers.add("Fecha Fin de Vigencia");
            headers.add("CANTIDAD DE AÑOS");
            headers.add("Capital de Vida (Fallecimiento)");
            headers.add("Indemnización adicional por muerte accidental");
            headers.add("Gastos Medicos");
            headers.add("Capital Asegurado Total");
            headers.add("Prima Anual");
            headers.add("Fecha de Anulación");
            headers.add("Prima Total (Vigencia Total)");
            headers.add("Prima Neta");
            headers.add("Prima Adicional");
            headers.add("Prima Pura de Riesgo");
            headers.add("APS");
            headers.add("FPA");
            headers.add("Servicio de Cobranza");
            headers.add("Comisión[$] Individual");
            headers.add("Comisión[%] Individual");
            headers.add("Intermediario");
            headers.add("Comisión[$] Individual Broker");
            headers.add("Comisión[%] Individual Broker");
            headers.add("Comprobante");
            headers.add("Estado");
            headers.add("Observación");
            headers.add("Capital Asegurado Cedido Vida");
            headers.add("Prima Cedida Vida");
            headers.add("Imp. Remesas Vida");
            headers.add("Capital Asegurado Cedido Indemnización");
            headers.add("Prima Cedida Indemnización");
            headers.add("Imp. Remesas Indemnización");
            headers.add("CA Cedido Gastos Medicos");
            headers.add("Prima Cedida GM");
            headers.add("Impuesto Remesas GM");
            headers.add("Capital Cedido Total");
            headers.add("Prima Cedida Total");
            headers.add("IRE Total");
            headers.add("Edad Actuarial a la fecha de corte");
            headers.add("Edad actuarial Mancomunada a la fecha de corte");
            headers.add("Fumador");
            headers.add("Capital Saldado si corresponde");
            headers.add("Forma de Pago");
            headers.add("Tiempo incurrido desde el inicio de vigencia de póliza");
            headers.add("Reserva Matemática Gestion Anterior");
            headers.add("Reserva Matemática Gestion Actual");
            headers.add("Reserva Matemática Seguros Complementarios (si los hubiera) Gestión Actual");
            headers.add("Saldo Cuenta Individual (Reserva Financiera)");
            headers.add("% De ahorro (si corresponde)");
            headers.add("Tasa de Interés Financiera (%)");
            headers.add("Reserva Matemática y Financiera Total");
            headers.add("Fecha de Pago");
            headers.add("No. de Comprobante");
        }
        if (option == 2) {
            headers.add("No. Propuesta");
            headers.add("Fecha de Generación de Propuesta");
            headers.add("Fecha de Aceptación de Propuesta");
            headers.add("Días transcurridos desde la Generación de la Propuesta");
            headers.add("Estado de la Propuesta");
            headers.add("Póliza");
            headers.add("MES");
            headers.add("Tipo de Movimiento");
            headers.add("Fecha");
            headers.add("Moneda");
            headers.add("Regional SCVS");
            headers.add("Sucursal BFS");
            headers.add("Zona BFS");
            headers.add("Agencia BFS");
            headers.add("Gestor de Negocios BFS");
            headers.add("Código Gestor de Negocios BFS");
            headers.add("Ramo");
            headers.add("Producto");
            headers.add("Plan");
            headers.add("Código Cliente");
            headers.add("Tomador");
            headers.add("ACTIVIDAD");
            headers.add("Asegurado");
            headers.add("No. Identificación");
            headers.add("Nacionalidad");
            headers.add("Fecha de Nacimiento");
            headers.add("Edad");
            headers.add("Género");
            headers.add("DIRECCION");
            headers.add("Estado Civil");
            headers.add("CORREO ELECTRÓNICO");
            headers.add("NÚMERO DE TELÉFONO");
            headers.add("Fecha de Emisión");
            headers.add("Vigencia Inicial de Póliza");
            headers.add("Vigencia Final de Póliza");
            headers.add("CANTIDAD DE AÑOS");
            headers.add("Capital de Vida (Fallecimiento)");
            headers.add("Indemnización adicional por muerte accidental");
            headers.add("Gastos Medicos");
            headers.add("Capital Asegurado Total");
            headers.add("Prima Total");
            headers.add("Tomador");
            headers.add("Estado");
        }
        return headers;
    }

    public void reSendSimpleMessage(MessageDTO messageDTO) {
        messageDTO.setNumberOfAttempt(messageDTO.getNumberOfAttempt() + 1);

        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.EMAIL.getValue()) {
            senderService.setStrategy(emailSenderService);
            messageDTO.setSendTo(messageDTO.getTo().split(";"));
            messageDTO.setSendCc(messageDTO.getCc().split(";"));
        }

        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.WHATSAPP.getValue()) {
            senderService.setStrategy(whatsAppSenderService);
        }

        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.SMS.getValue()) {
            senderService.setStrategy(smsSenderService);
        }

        senderService.sendMessage(messageDTO);
    }

    public void reSendComplexMessage(MessageDTO messageDTO) {
        messageDTO.setNumberOfAttempt(messageDTO.getNumberOfAttempt() + 1);
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(messageDTO.getReferenceId());

        Alert alert = new Alert();
        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.EMAIL.getValue()) {
            List<BrokerDTO> brokerList = this.brokerPort.getAllBrokers();
            BrokerDTO brokerDTO = new BrokerDTO();
            for (BrokerDTO br: brokerList) {
                if (br.getBusinessName().contains(hpBroker)) {
                    brokerDTO = br;
                }
            }

            PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(messageDTO.getReferenceId());
            List<CoverageDTO> coverageDTOList = coverageProductPlanPort.findAllByPolicyItemId(policyItem.getId());
            List<Beneficiary> beneficiaryList = beneficiaryPort.findAllByPolicyItemId(policyItem.getId());
            Person person = personPort.findById(generalRequest.getPersonId());
            Policy policy = policyPort.getByRequestIdOneData(generalRequest.getId());
            Person insurerCompany = personPort.findByNitNumber((long) CompanysNitNumber.SCVS.getValue());
            Person holderCompany = personPort.findByNitNumber((long) CompanysNitNumber.FBS.getValue());
            Classifier moneyType = classifierPort.getClassifierByReferencesIds(policy.getCurrencyTypeIdc(), ClassifierTypeEnum.Currency.getReferenceId());
            Classifier ext = classifierPort.getClassifierByReferencesIds(person.getNaturalPerson().getExtIdc(), ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
            Account account = accountPort.findLastByPersonIdAndPolicyId(person.getId(), policy.getId());

            senderService.setStrategy(emailSenderService);

            messageDTO.setSendTo(messageDTO.getTo().split(";"));

            if (messageDTO.getCc() == null) {
                messageDTO.setCc(";");
            }
            messageDTO.setSendCc(messageDTO.getCc().split(";"));

            String htmlCov = "";
            for (CoverageDTO cov : coverageDTOList) {
                htmlCov = htmlCov + "<tr><td>" + cov.getCoverageName() + "</td><td>";
                htmlCov = htmlCov + moneyType.getAbbreviation() + HelpersMethods.convertNumberToCompanyFormatNumber(
                        cov.getInsuredCapitalCoverage()) + "</td></tr>";
            }

            String htmlBen = "";
            int benCount = 1;
            for (Beneficiary beneficiary : beneficiaryList) {
                htmlBen = htmlBen + "<tr><td>Beneficiario " + benCount + ": </td><td>";
                htmlBen = htmlBen + beneficiary.getFullName() + "</td><td> Porcentaje: ";
                htmlBen = htmlBen + beneficiary.getPercentage() + "</td><td> Relación: ";
                Classifier classifier = classifierPort.getClassifierByReferencesIds(beneficiary.getRelationshipIdc(),
                        ClassifierTypeEnum.Relationship.getReferenceId());
                htmlBen = htmlBen + classifier.getDescription() + "</td></tr>";
                benCount++;
            }

            List<String> valuesToReplace = new ArrayList<>();
            valuesToReplace.add(person.getNaturalPerson().getCompleteName());
            valuesToReplace.add(htmlCov);
            valuesToReplace.add(generalRequest.getCreditTerm().toString());
            valuesToReplace.add(HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(policy.getTotalPremium(), moneyType.getAbbreviation()));
            valuesToReplace.add(insurerCompany.getJuridicalPerson().getName().toUpperCase());
            valuesToReplace.add(htmlBen);

            String urlSet = "";
            String urlSetFalse = "";
            MessageDecideResponseDTO responseTrue = MessageDecideResponseDTO.builder()
                    .referenceTable((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode())
                    .response(1)
                    .requestId(generalRequest.getId())
                    .build();

            String jsonStringTrue = responseTrue.toString();
            String sendTrue = Base64.getEncoder().encodeToString(jsonStringTrue.getBytes());

            MessageDecideResponseDTO responseFalse = MessageDecideResponseDTO.builder()
                    .referenceTable((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode())
                    .response(2)
                    .requestId(generalRequest.getId())
                    .build();

            String jsonStringFalse = responseFalse.toString();
            String sendFalse = Base64.getEncoder().encodeToString(jsonStringFalse.getBytes());

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                urlSet = urls[2];
                urlSet += proposalResponse + sendTrue;
                valuesToReplace.add(urlSet);

                urlSetFalse = urls[2];
                urlSetFalse += proposalResponse + sendFalse;
                valuesToReplace.add(urlSetFalse);
            } else if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
                urlSet = urls[1];
                urlSet += proposalResponse + sendTrue;
                valuesToReplace.add(urlSet);

                urlSetFalse = urls[1];
                urlSetFalse += proposalResponse + sendFalse;
                valuesToReplace.add(urlSetFalse);
            } else {
                urlSet = urls[0];
                urlSet += proposalResponse + sendTrue;
                valuesToReplace.add(urlSet);

                urlSetFalse = urls[0];
                urlSetFalse += proposalResponse + sendFalse;
                valuesToReplace.add(urlSetFalse);
            }

            valuesToReplace.add(APSCodesEnum.APS_PROPOSAL_VIN_CODE.getValue() + "");
            valuesToReplace.add(holderCompany.getJuridicalPerson().getName().toUpperCase());
            valuesToReplace.add(HelpersMethods.formatBankAccountNumber(account.getAccountNumber()));
            Date dateNowMail = new Date();
            valuesToReplace.add(HelpersMethods.formatStringOnlyDate(dateNowMail));
            valuesToReplace.add(HelpersMethods.formatStringOnlyHourAndMinute(dateNowMail));

            String identificationAndCmp = person.getNaturalPerson().getIdentificationNumber();
            if (!person.getNaturalPerson().getComplement().isEmpty()) {
                identificationAndCmp = identificationAndCmp + "-" + person.getNaturalPerson().getComplement().toUpperCase();
            }
            String extI = "N/A";
            if (person.getNaturalPerson().getExtIdc() != null) {
                extI = ext.getDescription().toUpperCase();
            }
            valuesToReplace.add(identificationAndCmp);
            valuesToReplace.add(extI);
            valuesToReplace.add("correo electrónico ");
            valuesToReplace.add(person.getEmail());
            valuesToReplace.add(brokerDTO.getBusinessName().toUpperCase());

            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_ACTIVATION_CONFIRM_PROPOSAL, valuesToReplace);

            messageDTO.setMessage(alert.getMail_body());
            if (!messageDTO.getSubject().contains(" - Reenvío")) {
                messageDTO.setSubject(messageDTO.getSubject() + " - Reenvío");
            }
        }

        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.WHATSAPP.getValue()) {
            senderService.setStrategy(whatsAppSenderService);
        }

        if (messageDTO.getMessageTypeIdc() == MessageTypeEnum.SMS.getValue()) {
            senderService.setStrategy(smsSenderService);
            List<String> valuesToReplace = new ArrayList<>();
            String urlSet = "";
            String requestJson = "{" +
                    "\"generalRequestId\": " + generalRequest.getId() +
                    "}";
            String conversor = Base64.getEncoder().encodeToString(requestJson.getBytes());
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                urlSet = urls[2];
            } else if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
                urlSet = urls[1];
            } else {
                urlSet = urls[0];
            }

            urlSet += proposalResponseWeb + conversor;
            valuesToReplace.add(urlSet);
            alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_ACTIVATION_CONFIRM_PROPOSAL_SMS, valuesToReplace);


            if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                messageDTO.setTo(sendTelephone);
            } else {
                messageDTO.setTo(messageDTO.getTo());
            }

            messageDTO.setMessage(alert.getMail_body());
            messageDTO.setSubject(alert.getMail_subject());
            messageDTO.setMessageTypeIdc(MessageTypeEnum.SMS.getValue());
            messageDTO.setReferenceId(generalRequest.getId());
            messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
            messageDTO.setNumberOfAttempt(0);
            messageDTO.setLastNumberOfAttempt(0);
        }

        senderService.sendMessage(messageDTO);
    }

    private void duplicateDataFromReversion(Policy policy, GeneralRequest request, PolicyItem policyItem,
                                            PolicyItemEconomic policyItemEconomic,
                                            List<PolicyItemEconomicReinsurance> policyItemEconomicReinsuranceList) {
        double factor = -1;
        Policy policyAux = policy;
        AccountPolicy accountPolicy = accountPolicyPort.findByPolicyId(policyAux.getId());
        List<Beneficiary> beneficiaryList = beneficiaryPort.findAllByPolicyItemId(policyItem.getId());
        List<CoveragePolicyItem> coveragePolicyItemList = coveragePolicyItemPort.findByPolicyItemId(policyItem.getId());
        MessageDTO messageSent = messageSentPort.findByReferenceIdAndTableReferenceIdc(request.getId(),
                (int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
        boolean messageSentControl = false;
        boolean messageSentControlTwo = false;
        if (messageSent == null) {
            messageSent = messageToSendPort.findByReferenceIdAndTableReferenceIdc(request.getId(),
                    (int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
            messageSentControl = true;
            if (messageSent == null) {
                messageSentControlTwo = true;
            }
        }
        MessageResponse response = new MessageResponse();
        MessageDTO messageConfirm = new MessageDTO();
        boolean messageToSendStatus = false;
        boolean messageNoSender = false;
        if (messageSent != null) {
            response = messageResponsePort.getMessageByMessageSentId(messageSent.getId());
            if (response != null) {
                messageConfirm = messageSentPort.findByReferenceIdAndTableReferenceIdc(response.getId(),
                        (int) ClassifierEnum.REFERENCE_TABLE_MESSAGERESPONSE.getReferenceCode());
                if (messageConfirm == null) {
                    messageConfirm = messageToSendPort.findByReferenceIdAndTableReferenceIdc(response.getId(),
                            (int) ClassifierEnum.REFERENCE_TABLE_MESSAGERESPONSE.getReferenceCode());
                    if (messageConfirm == null) {
                        messageNoSender = true;
                    } else {
                        messageToSendStatus = true;
                    }
                }
            }
        }

        //#region Cambios de id para duplicar valores

        request.setId(0L);
        long requestId = generalRequestPort.saveOrUpdate(request);

        Date issuanceDate = new Date();

        policyAux.setId(0L);
        policyAux.setGeneralRequestId(requestId);
        policyAux.setNumberPolicy("SP-" + HelpersMethods.formatStringDate(issuanceDate));
        policyAux.setPolicyStatusIdc(0);
        policyAux = policyPort.saveOrUpdate(policyAux);

        policyItem.setId(0L);
        policyItem.setGeneralRequestId(requestId);
        policyItem.setPolicyId(policyAux.getId());
        policyItem = policyItemPort.saveOrUpdate(policyItem);

        policyItemEconomic.setId(0L);
        policyItemEconomic.setMovementTypeIdc(PolicyMovementTypeClassifierEnum.PRODUCTION.getValue());
        policyItemEconomic.setPolicyItemId(policyItem.getId());
        policyItemEconomic = policyItemEconomicPort.saveOrUpdate(policyItemEconomic);

        PolicyItemEconomic tempPolicyItemEconomic = policyItemEconomic;
        policyItemEconomicReinsuranceList.forEach(o -> {
            o.setId(0L);
            o.setPremiumCeded(o.getPremiumCeded() * factor);
            o.setPolicyItemEconomicId(tempPolicyItemEconomic.getId());
        });
        policyItemEconomicReinsurancePort.saveOrUpdateAll(policyItemEconomicReinsuranceList);

        accountPolicy.setId(0L);
        accountPolicy.setPolicyId(policyItem.getPolicyId());
        accountPolicy = accountPolicyPort.saveOrUpdate(accountPolicy);

        List<Beneficiary> beneficiaryListAux = new ArrayList<>();
        for (Beneficiary bn: beneficiaryList) {
            bn.setId(0L);
            bn.setPolicyId(policyItem.getPolicyId());
            bn.setPolicyItemId(policyItem.getId());
            beneficiaryListAux.add(bn);
        }
        boolean benAdd = beneficiaryPort.saveAll(beneficiaryListAux);

        List<CoveragePolicyItem> coveragePolicyItemListAux = new ArrayList<>();

        for (CoveragePolicyItem pl: coveragePolicyItemList) {
            pl.setId(0L);
            pl.setPolicyItemId(policyItem.getId());
            coveragePolicyItemListAux.add(pl);
        }
        coveragePolicyItemListAux = coveragePolicyItemPort.saveOrUpdateAll(coveragePolicyItemListAux);
        if (!messageSentControlTwo) {
            messageSent.setId(0L);
            messageSent.setReferenceId(requestId);
            long messageSentId = 0L;
            if (messageSentControl) {
                messageSentId = messageToSendPort.saveOrUpdate(messageSent).getId();
            } else {
                messageSentId = messageSentPort.saveOrUpdate(messageSent);
            }

            if (response != null) {
                response.setMessageSentId(0L);
                response.setMessageSentId(messageSentId);
                long responseId = messageResponsePort.saveOrUpdate(response);

                if (!messageNoSender) {
                    messageConfirm.setId(0L);
                    messageConfirm.setReferenceId(responseId);
                    long messageConfirmId = 0L;
                    if (messageToSendStatus) {
                        messageConfirm = messageToSendPort.saveOrUpdate(messageConfirm);
                        messageConfirmId = messageConfirm.getId();
                    } else {
                        messageConfirmId = messageSentPort.saveOrUpdate(messageConfirm);
                    }
                }
            }
        }

        //#endregion
    }

    //#endregion

    //#region Validations

    public void validateCancelOperation(RequestCancelOperationDTO requestCancelOperationDTO) {
        OperationException.throwExceptionIfTextInvalid("número de operacion", requestCancelOperationDTO.getNro_operacion());
        OperationException.throwExceptionIfNumberInvalid("número de comprobante", requestCancelOperationDTO.getNro_comprobante());
        OperationException.throwExceptionIfTextInvalid("motivo", requestCancelOperationDTO.getMotivo());
        OperationException.throwExceptionIfNumberInvalid("código de usuario", requestCancelOperationDTO.getCod_usuario());
        OperationException.throwExceptionIfTextInvalid("nombre de usuario", requestCancelOperationDTO.getNombre_usuario());
    }
    public void validateOperationDetailDTO(OperationDetailDTO operationDetailDTO) {
        OperationException.throwExceptionIfTextInvalid("número de operacion", operationDetailDTO.getNro_operacion());
        OperationException.throwExceptionIfNumberInvalid("tipo de documento", operationDetailDTO.getTipo_documento().longValue());
        OperationException.throwExceptionIfTextInvalid("número de documento", operationDetailDTO.getNro_documento());
    }
    public boolean validateProposalToCancel(String operationNumber, Long planId) {
        List<GeneralRequest> generalRequestList = generalRequestPort.getAllByOperationNumberAndPlanId(operationNumber, planId);
        if (generalRequestList == null || generalRequestList.isEmpty()) {
            return true;
        }

        GeneralRequest generalRequest = generalRequestList.stream().filter(x ->
                x.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue() ||
                        x.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue()).findFirst().orElse(null);
        if (generalRequest == null) {
            return true;
        }

        Policy policy = policyPort.getByRequestId(generalRequest.getId());
        if (policy == null || (policy.getPolicyStatusIdc() == 0 && policy.getNumberPolicy().contains( "SP") &&
                policy.getCorrelativeNumber() == 0 && policy.getInsuredCapital() == 0)) {
            generalRequest.setRequestStatusIdc(RequestStatusEnum.CANCELLED.getValue());
            generalRequestPort.saveOrUpdate(generalRequest);
            return true;
        }

        if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
            generalRequest.setRequestStatusIdc(RequestStatusEnum.CANCELLED.getValue());
            generalRequestPort.saveOrUpdate(generalRequest);
            return true;
        }

        return false;
    }

    public Classifier existsCIExt(String searchCIExt) {
        List<Classifier> ciExtList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());

        Classifier ciExt = ciExtList.stream()
                .filter(e -> e.getDescription().equals(searchCIExt))
                .findFirst()
                .orElse(null);

        return ciExt;
    }

    //#endregion

    //#region Schedule

    @Scheduled(cron = "0 0 01 * * *", zone = "America/La_Paz")
    public void automaticCancelationProposal() {
        Calendar toDay = DateUtils.getDateNowByGregorianCalendar();
        toDay.add(Calendar.DAY_OF_MONTH, -180);
        toDay.setTime(DateUtils.changeHourInDateMorningAndNight(toDay.getTime(),true,false));
        List<GeneralRequest> generalRequestList = generalRequestPort.getAllPendingToActivationProposalToCancelation(LocalDateTime.ofInstant(toDay.toInstant(),toDay.getTimeZone().toZoneId()));
        generalRequestList.forEach(x -> x.setRequestStatusIdc(RequestStatusEnum.CANCELLED.getValue()));
        generalRequestPort.saveOrUpdateAll(generalRequestList);
    }

    //#endregion

}
