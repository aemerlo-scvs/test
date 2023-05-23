package com.scfg.core.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.application.port.in.GenerateCertificateCoverageUseCase;
import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.application.service.sender.EmailSenderService;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.common.User;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.MessageDTO;
import com.scfg.core.domain.dto.vin.Account;
import com.scfg.core.domain.dto.vin.BfsPaymentDTO;
import com.scfg.core.domain.dto.vin.GenerateCertificateVin;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerateCertificateCoverageService implements GenerateCertificateCoverageUseCase {

    private final GeneratePdfUseCase generatePdfUseCase;
    private final GeneralRequestPort generalRequestPort;
    private final PolicyPort policyPort;
    private final PolicyItemPort policyItemPort;
    private final CoverageProductPlanPort coverageProductPlanPort;
    private final PersonPort personPort;
    private final BeneficiaryPort beneficiaryPort;
    private final ClassifierPort classifierPort;
    private final SenderService senderService;
    private final EmailSenderService emailSenderService;
    private final FileDocumentPort fileDocumentPort;
    private final PolicyFileDocumentPort policyFileDocumentPort;
    private final AlertService alertService;
    private final AccountPort accountPort;
    private final DirectionPort directionPort;
    private final TelephonePort telephonePort;
    private final JksCertificateService jksCertificateService;
    private final PaymentPort paymentPort;
    private final PaymentPlanPort paymentPlanPort;
    private final TransactionPort transactionPort;
    private final PolicyItemMathReservePort policyItemMathReservePort;
    private final MathReservePort mathReservePort;
    private final UserPort userPort;
    private final Environment environment;
    private final MessageSentPort messageSentPort;
    private final MessageResponsePort messageResponsePort;
    private final BrokerPort brokerPort;
    private final String hpBroker = "HP BROKERS";
    private final String sendMails = "ccuellars@santacruzfg.com;gaguilar@santacruzfg.com";

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class, OperationException.class})
    @Override
    public FileDocument generateVINCertificateCoverage(Long requestId, Object object) {

        //#region Obtención de datos generales para el certificado
        GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(requestId);
        PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(generalRequest.getId());
        List<CoverageDTO> coverageDTOList = coverageProductPlanPort.findAllByPolicyItemId(policyItem.getId());
        List<Beneficiary> beneficiaryList = beneficiaryPort.findAllByPolicyItemId(policyItem.getId());
        Person person = personPort.findById(generalRequest.getPersonId());
        Person insurerCompany = personPort.findByNitNumber((long) CompanysNitNumber.SCVS.getValue());
        Person holderCompany = personPort.findByNitNumber((long) CompanysNitNumber.FBS.getValue());
        Policy policy = policyPort.getByRequestIdOneData(generalRequest.getId());
        Classifier moneyType = classifierPort.getClassifierByReferencesIds(policy.getCurrencyTypeIdc(), ClassifierTypeEnum.Currency.getReferenceId());
        Classifier nationality = classifierPort.getClassifierByReferencesIds(person.getNationalityIdc(), ClassifierTypeEnum.Country.getReferenceId());
        Classifier maritalStatus = classifierPort.getClassifierByReferencesIds(person.getNaturalPerson().getMaritalStatusIdc(), ClassifierTypeEnum.MaritalStatus.getReferenceId());
        List<Classifier> relationShipList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(
                ClassifierTypeEnum.Relationship.getReferenceId());
        List<Classifier> regionalList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(
                ClassifierTypeEnum.Regional.getReferenceId());
        List<Classifier> extensions = classifierPort.getAllClassifiersByClassifierTypeReferenceId(
                ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
        Account account = accountPort.findLastByPersonIdAndPolicyId(generalRequest.getPersonId(), policy.getId());
        List<Direction> insurerDirections = directionPort.findAllByPersonId(person.getId());
        person.setDirection(insurerDirections.stream().max(Comparator.comparing(Direction::getCreatedAt)).get());
        insurerCompany.setDirections(directionPort.findAllByPersonId(insurerCompany.getId()));
        insurerCompany.setTelephones(telephonePort.getAllByPersonId(insurerCompany.getId()));
        List<BrokerDTO> brokerList = this.brokerPort.getAllBrokers();
        BrokerDTO brokerDTO = new BrokerDTO();
        for (BrokerDTO br: brokerList) {
            if (br.getBusinessName().contains(hpBroker)) {
                brokerDTO = br;
            }
        }

        MessageDTO message = new MessageDTO();
        List<MessageDTO> messageList = new ArrayList<>();

        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            message.setCreatedAt(new Date());
            message.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
        } else {
            if (generalRequest.getRequestStatusIdc() != RequestStatusEnum.FINALIZED.getValue()) {
                throw new OperationException("La propuesta debe ser aceptada, para generar el certificado de cobertura");
            }
            messageList = messageSentPort.findAllByReferenceIdAndTableReferenceIdc(generalRequest.getId(),
                    (int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());

            List<Long> messageSentIds = messageList.stream().map(MessageDTO::getId).collect(Collectors.toList());
            MessageResponse messageResponse = messageResponsePort.getMessageByMessageSentIds(messageSentIds);

            if (messageResponse == null) {
                throw new OperationException("No se tiene registrada la confirmación del cliente");
            }
            message = messageList.stream().filter(o -> o.getId().equals(messageResponse.getMessageSentId())).findFirst().get();
            message.setCreatedAt(messageResponse.getCreatedAt());
        }


        //#endregion

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            //#region Inserción de datos
            byte[] json = objectMapper.writeValueAsBytes(object);
            BfsPaymentDTO paymentDTO = objectMapper.readValue(json, BfsPaymentDTO.class);
            LocalDateTime dateNow = LocalDateTime.now();
            Payment payment = mapToDomainPayment(policy.getTotalPremium(), policy.getCurrencyTypeIdc(), requestId);
            long paymentId = paymentPort.saveOrUpdate(payment);
            PaymentPlan paymentPlan = mapToDomainPaymentPlan(policy.getTotalPremium(), paymentId, dateNow);
            long paymentPlanId = paymentPlanPort.saveOrUpdate(paymentPlan);
            Transaction transaction = mapToDomainTransaction(paymentDTO, policy.getTotalPremium(), policy.getCurrencyTypeIdc(), paymentPlanId, dateNow);
            long transactionId = transactionPort.saveOrUpdate(transaction);
            boolean changePolId = false;

            Date validateFrom = DateUtils.asDate(LocalDate.now());
            Date validated = DateUtils.asSummaryRestartDays(validateFrom, Calendar.DAY_OF_YEAR, generalRequest.getCreditTermInDays());
            policy.setIssuanceDate(new Date());
            policy.setFromDate(validateFrom);
            policy.setToDate(validated);
            if (policy.getPolicyStatusIdc() == PolicyStatusEnum.CANCELED.getValue()) {
                policy.setId(0L);
                changePolId = true;
            }
            policy.setPolicyStatusIdc(PolicyStatusEnum.ACTIVE.getValue());
            policy.setCreatedAt(new Date());
            policy.setLastModifiedAt(new Date());
            policy.setNumberPolicy(policyPort.getNextSequencyPolNumber(ProductEnum.VIN.getValue()));
            policy = policyPort.saveOrUpdate(policy);
            if (changePolId) {
                policyItem.setPolicyId(policy.getId());
                policyItem = policyItemPort.saveOrUpdate(policyItem);
                List<PolicyItemMathReserve> changeStatusList = policyItemMathReservePort.findByPolicyItemId(policyItem.getId());
                if (changeStatusList != null || !changeStatusList.isEmpty()) {
                    List<PolicyItemMathReserve> changeStatusListAux = new ArrayList<>();
                    for (PolicyItemMathReserve x: changeStatusList) {
                        x.setStatus(PersistenceStatusEnum.DELETED.getValue());
                        changeStatusListAux.add(x);
                    }
                    changeStatusListAux = policyItemMathReservePort.saveOrUpdateAll(changeStatusListAux);
                }
            }
            User user = userPort.findById(policy.getLastModifiedBy());
            Classifier reg = regionalList.stream().filter(x -> x.getReferenceId() == user.getRegionalIdc()).findFirst().orElse(null);
            //#endregion

            LocalDate timeNow = LocalDate.now();
            LocalDate birthDate = person.getNaturalPerson().getBirthDate().toLocalDate();
            Period period = Period.between(birthDate,timeNow);
            List<MathReserve> mathReserveList = mathReservePort.getAllByVersionInsurerYearAndTotalYear(
                    MathReserveVersionEnum.V2.getValue(), period.getYears(), generalRequest.getCreditTermInYears()
            );
            List<PolicyItemMathReserve> policyItemMathReserveList = new ArrayList<>();
            for (int i = 0; i <= mathReserveList.size()-1; i++) {
                PolicyItemMathReserve policyItemMathReserve = PolicyItemMathReserve.builder()
                        .policyItemId(policyItem.getId())
                        .insuranceValidity(mathReserveList.get(i).getVigencyYears())
                        .year(timeNow.getYear() + i)
                        .value(mathReserveList.get(i).getValue())
                        .build();
                policyItemMathReserveList.add(policyItemMathReserve);
            }
            policyItemMathReserveList = policyItemMathReservePort.saveOrUpdateAll(policyItemMathReserveList);

            //#region Preparando DTO para pasar al generador de certificado

            GenerateCertificateVin generateCertificateVin = GenerateCertificateVin.builder()
                    .insurer(person)
                    .insurerCompany(insurerCompany)
                    .holderCompany(holderCompany)
                    .coverageDTOList(coverageDTOList)
                    .beneficiaryList(beneficiaryList)
                    .moneyType(moneyType)
                    .relationShipList(relationShipList)
                    .extensions(extensions)
                    .request(generalRequest)
                    .policy(policy)
                    .nationality(nationality)
                    .maritalStatus(maritalStatus)
                    .account(account)
                    .regional(reg)
                    .messageDTO(message)
                    .brokerDTO(brokerDTO)
                    .build();
            byte[] certificate = generatePdfUseCase.generateVINCoverageCertificate(generateCertificateVin);
            //#endregion

            //#region Guardado de información a nivel de base de datos del archivo

            String base64 = Base64.getEncoder().encodeToString(certificate);

            List<String> signList = new ArrayList<>();
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                signList.add(CertificateOwnerEnum.MFRANCO.getValue());
                signList.add(CertificateOwnerEnum.MAGUIRRE.getValue());
            } else {
                signList.add(CertificateOwnerEnum.RFMOLINA.getValue());
            }
            String signedB64 = jksCertificateService.signDocumentWithP12Cert(base64, signList);

            String fileName = policy.getNumberPolicy();
            FileDocument fileDocument = FileDocument.builder()
                    .mime("application/pdf")
                    .description(fileName)
                    .content(signedB64)
                    .typeDocument(TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue())
                    .build();

            fileDocument = fileDocumentPort.SaveOrUpdate(fileDocument);
            PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                    .fileDocumentId(fileDocument.getId())
                    .policyItemId(policyItem.getId())
                    .isSigned(0)
                    .build();
            policyFileDocument = policyFileDocumentPort.saveOrUpdate(policyFileDocument);
            //#endregion

            //#region Preparado para envió de certíficado por correo y retorno del servicio

            AttachmentDTO attachmentDTO = new AttachmentDTO();
            attachmentDTO.setContent(Base64.getDecoder().decode(signedB64));
            attachmentDTO.setFileName(policy.getNumberPolicy() + ".pdf");
            attachmentDTO.setMimeType("application/pdf");
            List<AttachmentDTO> fileList = new ArrayList<>();
            fileList.add(attachmentDTO);

            List<String> replaces = new ArrayList<>();

            Alert alert = alertService.getAlertByEnumReplacingContent(AlertEnum.VIN_CERTIFICATE_SEND, replaces);

            MessageDTO messageDTO = MessageDTO.builder()
                    .id(0l)
                    .subject(alert.getMail_subject())
                    .message(alert.getMail_body())
                    .messageTypeIdc(MessageTypeEnum.EMAIL.getValue())
                    .numberOfAttempt(0)
                    .lastNumberOfAttempt(0)
                    .build();
            String copy = ";";
            if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                messageDTO.setTo(sendMails);
                messageDTO.setSendTo(sendMails.split(";"));
            } else {
                messageDTO.setTo(person.getEmail());
                if (person.getEmail() != null && !person.getEmail().isEmpty()) {
                    messageDTO.setSendTo(person.getEmail().split(";"));
                }
            }
            messageDTO.setCc(copy);
            messageDTO.setSendCc(copy.split(";"));

            if (person.getEmail() != null && !person.getEmail().isEmpty()) {
                senderService.setStrategy(emailSenderService);
                senderService.sendMessageWithAttachment(messageDTO, fileList);
            }

            return fileDocument;

            //#endregion

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //#region Mappers
    private Payment mapToDomainPayment(double totalPayment, int currencyType, long requestId) {

        Payment payment = Payment.builder()
                .id(0L)
                .paymentTypeIdc(PaymentTypeEnum.Cash.getValue())
                .paymentPeriod(PeriodicityEnum.None.getValue())
                .total(totalPayment)
                .surcharge(0.00)
                .totalSurcharge(0.00)
                .totalPaid(totalPayment)
                .currencyTypeIdc(currencyType)
                .generalRequestId(requestId)
                .build();

        return payment;
    }

    private PaymentPlan mapToDomainPaymentPlan(double totalPayment, long paymentId, LocalDateTime dateNow) {

        PaymentPlan paymentPlan = PaymentPlan.builder()
                .id(0L)
                .quoteNumber(1)
                .amount(totalPayment)
                .amountPaid(totalPayment)
                .residue(0.00)
                .percentage(100)
                .expirationDate(dateNow)
                .datePaid(dateNow)
                .paymentId(paymentId)
                .build();

        return paymentPlan;
    }

    private Transaction mapToDomainTransaction(BfsPaymentDTO paymentDTO, double totalPayment, int currencyType,
                                               long paymentPlanId, LocalDateTime dateNow) {

        Transaction transaction = Transaction.builder()
                .id(0L)
                .amount(totalPayment)
                .remainAmount(0.00)
                .datePaid(dateNow)
                .currencyTypeIdc(currencyType)
                .paymentChannelIdc(PaymentChannelEnum.Cash.getValue())
                .transactionType(TransactionTypeEnum.PremiumPayment.getValue())
                .paymentPlanId(paymentPlanId)
                .voucherNumber(paymentDTO.getNro_comprobante().toString())
                .build();

        return transaction;
    }
    //#endregion
}
