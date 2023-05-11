package com.scfg.core.application.service;

import com.scfg.core.application.port.in.SMVSIntegrationUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.person.NaturalPerson;
import com.scfg.core.domain.person.Person;
import com.scfg.core.domain.smvs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SMVSIntegrationService implements SMVSIntegrationUseCase {

    private final ClassifierPort classifierPort;
    private final PersonPort personPort;
    private final DirectionPort directionPort;
    private final GeneralRequestPort generalRequestPort;
    private final PaymentPort paymentPort;
    private final PaymentPlanPort paymentPlanPort;
    private final TransactionPort transactionPort;
    private final ReceiptPort receiptPort;

    private final SMVSCommonService smvsCommonService;


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Override
    public PaymentResponseDTO makePayment(MakePaymentDTO paymentDTO) {

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        String notFoundMessage = "La Operación no fue completada debido a que no se encontraron ";
        String completeName = this.smvsCommonService.getCompleteName(paymentDTO.getPrimer_nombre(),
                paymentDTO.getApellido_paterno(), paymentDTO.getApellido_materno(),
                paymentDTO.getApellido_casada());
        boolean isValid = true;
        long personId = 0L;


        //#region Validación de Forma
        String paymentInvalidFields = validatePaymentDTO(paymentDTO);
        if (!paymentInvalidFields.isEmpty()) {
            String message = "Pago de Sepelio, cliente " + completeName + " con CI: " + paymentDTO.getNro_documento() + ". " +
                              notFoundMessage + "los siguientes campos obligatorios: " + paymentInvalidFields;
            log.error(message);
            SendPaymentAlertEmail(paymentDTO, message);
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", message);
            return paymentResponseDTO;
        }
        //#endregion

        //#region Validación de Calidad de Información

        Integer extIdc = 0;
        Integer countryIdc = 0;
        Person personAux = personPort.findByIdentificationNumberAndType(paymentDTO.getNro_documento(), paymentDTO.getTipo_documento());
        long dayDiff = ChronoUnit.DAYS.between((paymentDTO.getFecha_nacimiento() == null ? today : paymentDTO.getFecha_nacimiento()), today);
        double yearDiff = Precision.round((double) dayDiff / 365.00, 1);

        if (personAux != null && generalRequestPort.getAllGeneralRequestWitActivePoliciesByPersonId(personAux.getId()).size() > 0) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", "La Operación no fue completada debido a que ya cuenta con una póliza activa");
            isValid = false;
        }

        if (personAux != null && generalRequestPort.getAllGeneralRequestByPersonIdAndStatus(personAux.getId(), RequestStatusEnum.PENDING.getValue()).size() > 0) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", "La Operación no fue completada debido a que ya cuenta con una solicitud pendiente");
            isValid = false;
        }

        if (yearDiff != 0 && (yearDiff < 18 || yearDiff > 70)) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", "La Operación no fue completada debido a que no está dentro del rango de edad de 18 a 70 años para adquirir el seguro");
            isValid = false;
        }

        Classifier existsCIExt = existsCIExt(paymentDTO.getExtension());
        if (existsCIExt != null) {
            extIdc = existsCIExt.getReferenceId().intValue();
        }

        Boolean existsDocumentType = existsDocumentType(paymentDTO.getTipo_documento());
        if (!existsDocumentType) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", notFoundMessage + "el tipo de documento enviado: " + paymentDTO.getTipo_documento());
            isValid = false;
        }

        Boolean existsCurrencyType = existsCurrencyType(paymentDTO.getTipo_moneda());
        if (!existsCurrencyType) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", notFoundMessage + "el tipo de moneda enviado: " + paymentDTO.getTipo_moneda());
            isValid = false;
        }

        Classifier existsCountry = existsCountry(paymentDTO.getPais());
        if (existsCountry == null && paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.BANK_CLIENT.getValue()) {
            paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", notFoundMessage + "la nacionalidad enviada: " + paymentDTO.getPais());
            isValid = false;
        }

        if (existsCountry != null) {
            countryIdc = existsCountry.getReferenceId().intValue();
        }

        //#region Sólo para clientes del banco
        if (paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.BANK_CLIENT.getValue()) {

            Boolean existsMaritalStatus = existsMaritalStatus(paymentDTO.getEstado_civil());
            if (!existsMaritalStatus) {
                paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", notFoundMessage + "el estado civil enviado: " + paymentDTO.getEstado_civil());
                isValid = false;
            }

            Boolean existsGender = existsGender(paymentDTO.getGenero());
            if (!existsGender) {
                paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), "", notFoundMessage + "el género enviado: " + paymentDTO.getGenero());
                isValid = false;
            }

        }
        //#endregion

        //#endregion

        if (!isValid) {
            String message = "Pago de Sepelio, cliente " + completeName + " con CI: " + paymentDTO.getNro_documento() + ". " + paymentResponseDTO.getMensaje();
            log.error(message);
            SendPaymentAlertEmail(paymentDTO, message);
            return paymentResponseDTO;
        }

        if (personAux == null) {
            Person person = mapToDomainPerson(paymentDTO, countryIdc, extIdc);
            personId = personPort.saveOrUpdate(person);
            if (!paymentDTO.getDomicilio().isEmpty()) { // Si no tiene dirección no se guarda
                Direction direction = mapToDomainDirection(paymentDTO, personId);
                directionPort.saveOrUpdate(direction);
            }
        } else {
            personAux.getNaturalPerson().setClientCode(paymentDTO.getCod_cliente());
            personPort.saveOrUpdate(personAux);
            personId = personAux.getId();
        }

        GeneralRequest generalRequest = mapToDomainGeneralRequest(paymentDTO, personId);
        long requestId = generalRequestPort.saveOrUpdate(generalRequest);
        Payment payment = mapToDomainPayment(paymentDTO, requestId);
        long paymentId = paymentPort.saveOrUpdate(payment);
        PaymentPlan paymentPlan = mapToDomainPaymentPlan(paymentDTO, paymentId);
        long paymentPlanId = paymentPlanPort.saveOrUpdate(paymentPlan);
        Transaction transaction = mapToDomainTransaction(paymentDTO, paymentPlanId);
        long transactionId = transactionPort.saveOrUpdate(transaction);
        Receipt receipt = mapToDomainReceipt(paymentDTO, transactionId);
        receiptPort.saveOrUpdate(receipt);


        SendMessageDTO messageDTO = SendMessageDTO.builder()
                .name(paymentDTO.getPrimer_nombre())
                .email(paymentDTO.getCorreo())
                .requestId(requestId)
                .activationCode(generalRequest.getActivationCode())
                .phoneNumber(paymentDTO.getTelefono_movil().toString())
                .messageTypeEnum(AlertEnum.SMVS_WELCOME_MESSAGE)
                .build();
        smvsCommonService.sendMessages(messageDTO);

        paymentResponseDTO = setPaymentResponse(SMVSResponseEnum.OK.getValue(), generalRequest.getActivationCode(), "Operación realizada correctamente");

        return paymentResponseDTO;
    }


    //#region MakePayment Validations

    String validatePaymentDTO(MakePaymentDTO paymentDTO) {
        String field = "";
        boolean isClientTypeValid = false;

        if ((paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.BANK_CLIENT.getValue()) || (paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.EVENTUAL_CLIENT.getValue())) {
            isClientTypeValid = true;
        }

        if (!isClientTypeValid) {
            field += "Tipo Cliente";
            return field;
        }

        if (paymentDTO.getCod_cliente() == null || paymentDTO.getCod_cliente() <= 0) {
            field += "Código Cliente, ";
        }
        if (paymentDTO.getNro_documento() == null || paymentDTO.getNro_documento().isEmpty()) {
            field += "Número de Documento, ";
        }
        if (paymentDTO.getTipo_documento() == null || paymentDTO.getTipo_documento() == 0) {
            field += "Tipo Documento, ";
        }
        if (paymentDTO.getPrimer_nombre() == null || paymentDTO.getPrimer_nombre().isEmpty()) {
            field += "Primer Nombre, ";
        }
        if ((paymentDTO.getApellido_paterno() == null || paymentDTO.getApellido_paterno().isEmpty()) &&
                (paymentDTO.getApellido_materno() == null || paymentDTO.getApellido_materno().isEmpty()) &&
                (paymentDTO.getApellido_casada() == null || paymentDTO.getApellido_casada().isEmpty())) {
            field += "Ningún Apellido, ";
        }

        if (paymentDTO.getTelefono_movil() == null || paymentDTO.getTelefono_movil() == 0) {
            field += "Teléfono Movil, ";
        }

        //#region Sólo para clientes del banco
        if (paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.BANK_CLIENT.getValue()) {

            if (paymentDTO.getEstado_civil() == null || paymentDTO.getEstado_civil() == 0) {
                field += "Estado Civil, ";
            }
            if (paymentDTO.getGenero() == null || paymentDTO.getGenero() == 0) {
                field += "Género, ";
            }
            if (paymentDTO.getFecha_nacimiento() == null) {
                field += "Fecha de Nacimiento, ";
            }
        }
        //#endregion

        if (paymentDTO.getPlan_id() == null || paymentDTO.getPlan_id() == 0) {
            field += "Id Plan, ";
        }
        if (paymentDTO.getMonto_pago() == null || paymentDTO.getMonto_pago() == 0) {
            field += "Monto de Pago, ";
        }
        if (paymentDTO.getNro_comprobante() == null || paymentDTO.getNro_comprobante().isEmpty()) {
            field += "Número de Comprobante, ";
        }
        if (paymentDTO.getFecha_pago() == null) {
            field += "Fecha de Pago, ";
        }
        if (paymentDTO.getId_usuario() == null || paymentDTO.getId_usuario() == 0) {
            field += "Id Usuario, ";
        }
        if (paymentDTO.getNombre_usuario() == null || paymentDTO.getNombre_usuario().isEmpty()) {
            field += "Nombre Usuario, ";
        }
        if (paymentDTO.getId_agencia() == null || paymentDTO.getId_agencia() == 0) {
            field += "Id Agencia, ";
        }
        if (paymentDTO.getNombre_agencia() == null || paymentDTO.getNombre_agencia().isEmpty()) {
            field += "Nombre Agencia, ";
        }
        if (paymentDTO.getTipo_moneda() == null) {
            field += "Tipo Moneda, ";
        }

        if (paymentDTO.getLugar_venta() == null || paymentDTO.getLugar_venta().isEmpty()) {
            field += "Lugar de Venta, ";
        }

        if (!field.isEmpty()) {
            field = field.substring(0, field.length() - 2);
        }

        return field;
    }

    public Classifier existsCountry(String searchCountry) {
        List<Classifier> countryList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Country.getReferenceId());

        Classifier country = countryList.stream()
                .filter(e -> e.getDescription().equals(searchCountry))
                .findFirst()
                .orElse(null);

        return country;
    }

    public Classifier existsCIExt(String searchCIExt) {
        List<Classifier> ciExtList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());

        Classifier ciExt = ciExtList.stream()
                .filter(e -> e.getDescription().equals(searchCIExt))
                .findFirst()
                .orElse(null);

        return ciExt;
    }

    public Boolean existsDocumentType(Integer searchDocumentType) {
        List<Classifier> documentTypeList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.DocumentType.getReferenceId());

        Classifier documentType = documentTypeList.stream()
                .filter(e -> e.getReferenceId().intValue() == searchDocumentType)
                .findFirst()
                .orElse(null);

        return documentType != null;
    }

    public Boolean existsMaritalStatus(Integer searchMaritalStatus) {
        List<Classifier> maritalStatusList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.MaritalStatus.getReferenceId());

        Classifier maritalStatus = maritalStatusList.stream()
                .filter(e -> e.getReferenceId().intValue() == searchMaritalStatus)
                .findFirst()
                .orElse(null);

        return maritalStatus != null;
    }

    public Boolean existsGender(Integer searchGender) {
        List<Classifier> genderList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Gender.getReferenceId());

        Classifier gender = genderList.stream()
                .filter(e -> e.getReferenceId().intValue() == searchGender)
                .findFirst()
                .orElse(null);

        return gender != null;
    }

    public Boolean existsCurrencyType(Integer searchCurrencyType) {
        List<Classifier> currencyTypeList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Currency.getReferenceId());

        Classifier currencyType = currencyTypeList.stream()
                .filter(e -> e.getReferenceId().intValue() == searchCurrencyType)
                .findFirst()
                .orElse(null);

        return currencyType != null;
    }

    //#endregion

    //#region MakePayment Helpers

    PaymentResponseDTO setPaymentResponse(Integer responseCode, String activationCode, String message) {
        return PaymentResponseDTO.builder()
                .codigo_respuesta(responseCode)
                .codigo_activacion(activationCode)
                .mensaje(message)
                .build();
    }

    void SendPaymentAlertEmail(MakePaymentDTO paymentDTO, String errorMessage) {
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        String dateFormat = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String timeFormat = today.format(DateTimeFormatter.ofPattern("hh:mm a"));
        String paymentDate = dateFormat + " a Hrs. " + timeFormat;

        SendMessageDTO messageDTO = SendMessageDTO.builder()
                .email("")
                .messageTypeEnum(AlertEnum.SMVS_PAYMENT_ALERT)
                .build();
        smvsCommonService.sendEmail(messageDTO, paymentDate,
                errorMessage, paymentDTO.getLugar_venta(),
                paymentDTO.getId_agencia().toString(), paymentDTO.getNombre_agencia(),
                paymentDTO.getId_usuario().toString(), paymentDTO.getNombre_usuario());
    }

    String getCompleteName(MakePaymentDTO paymentDTO) {
        return paymentDTO.getPrimer_nombre().trim() + " " + paymentDTO.getSegundo_nombre().trim() + " " +
                paymentDTO.getApellido_paterno().trim() + " " + paymentDTO.getApellido_materno().trim() + " " +
                paymentDTO.getApellido_casada().trim();
    }

    //#region Mappers

    private Person mapToDomainPerson(MakePaymentDTO paymentDTO, Integer countryIdc, Integer extIdc) {

        Person person = Person.builder()
                .id(0L)
                .nationalityIdc(countryIdc > 0 ? countryIdc : null)
                .residenceIdc(countryIdc > 0 ? countryIdc : null)
                .activityIdc(paymentDTO.getCodigo_actividad() > 0 ? paymentDTO.getCodigo_actividad() : null) //Si el código de actividad no existe, crear un nuevo clasificador con ese codigo, traer la referencia de su tipo clasificador
                .telephone(paymentDTO.getTelefono_movil() > 0 ? paymentDTO.getTelefono_movil().toString() : null)
                .email(paymentDTO.getCorreo())
                .holder(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .insured(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .naturalPerson(mapToDomainNaturalPerson(paymentDTO, extIdc))
                .build();

        return person;
    }

    private NaturalPerson mapToDomainNaturalPerson(MakePaymentDTO paymentDTO, Integer extIdc) {

        Integer clientEventual = PersistenceStatusEnum.DELETED.getValue();
        if (paymentDTO.getTipo_cliente() == SMVSClientTypeEnum.EVENTUAL_CLIENT.getValue()) {
            clientEventual = PersistenceStatusEnum.CREATED_OR_UPDATED.getValue();
        }


        String nameAux = "";
        if (paymentDTO.getPrimer_nombre() != null) nameAux += paymentDTO.getPrimer_nombre().trim();
        if (paymentDTO.getSegundo_nombre() != null) nameAux += " " + paymentDTO.getSegundo_nombre().trim();
        String lastName = paymentDTO.getApellido_paterno() != null ? paymentDTO.getApellido_paterno().trim() : paymentDTO.getApellido_paterno();
        String motherLastName = paymentDTO.getApellido_materno() != null ? paymentDTO.getApellido_materno().trim() : paymentDTO.getApellido_materno();
        String marriedLastName = paymentDTO.getApellido_casada() != null ? paymentDTO.getApellido_casada().trim() : paymentDTO.getApellido_casada();

        NaturalPerson naturalPerson = NaturalPerson.builder()
                .id(0L)
                .clientCode(paymentDTO.getCod_cliente())
                .clientEventual(clientEventual)
                .clientType(paymentDTO.getTipo_cliente())
                .name(nameAux)
                .lastName(lastName)
                .motherLastName(motherLastName)
                .marriedLastName(marriedLastName)
                .maritalStatusIdc(paymentDTO.getEstado_civil() > 0 ? paymentDTO.getEstado_civil() : null)
                .documentType(paymentDTO.getTipo_documento())
                .identificationNumber(paymentDTO.getNro_documento())
                .complement(paymentDTO.getComplemento())
                .extIdc(extIdc > 0 ? extIdc : null)
                .birthDate(paymentDTO.getFecha_nacimiento())
                .genderIdc(paymentDTO.getGenero() > 0 ? paymentDTO.getGenero() : null)
                .profession(paymentDTO.getProfesion())
                .build();

        return naturalPerson;
    }

    private Direction mapToDomainDirection(MakePaymentDTO paymentDTO, long personId) {
        Direction direction = Direction.builder()
                .description(paymentDTO.getDomicilio())
                .directionTypeIdc(DirectionTypeEnum.PERSONAL.getValue())
                .personId(personId)
                .build();

        return direction;
    }

    private GeneralRequest mapToDomainGeneralRequest(MakePaymentDTO paymentDTO, long personId) {
        String activationCode = HelpersMethods.generateCodeWithCapitalLettersAndNumbers(8);

        if (generalRequestPort.existsActivationCode(activationCode)) {
            return mapToDomainGeneralRequest(paymentDTO, personId);
        }

        String requestDescription = "Solicitud del cliente: " + paymentDTO.getPrimer_nombre() + " " + paymentDTO.getSegundo_nombre() + " " + paymentDTO.getApellido_paterno() + " " + paymentDTO.getApellido_materno();

        GeneralRequest generalRequest = GeneralRequest.builder()
                .id(0L)
                .requestDate(paymentDTO.getFecha_pago())
                .description(requestDescription)
                .activationCode(activationCode)
                .requestStatusIdc(RequestStatusEnum.PENDING.getValue())
                .planId(paymentDTO.getPlan_id())
                .personId(personId)
                .build();

        return generalRequest;
    }

    private Payment mapToDomainPayment(MakePaymentDTO paymentDTO, long requestId) {

        Payment payment = Payment.builder()
                .id(0L)
                .paymentTypeIdc(PaymentTypeEnum.Cash.getValue())
                .paymentPeriod(PeriodicityEnum.None.getValue())
                .total(paymentDTO.getMonto_pago())
                .surcharge(0.00)
                .totalSurcharge(0.00)
                .totalPaid(paymentDTO.getMonto_pago())
                .currencyTypeIdc(paymentDTO.getTipo_moneda())
                .generalRequestId(requestId)
                .build();

        return payment;
    }

    private PaymentPlan mapToDomainPaymentPlan(MakePaymentDTO paymentDTO, long paymentId) {

        PaymentPlan paymentPlan = PaymentPlan.builder()
                .id(0L)
                .quoteNumber(1)
                .amount(paymentDTO.getMonto_pago())
                .amountPaid(paymentDTO.getMonto_pago())
                .residue(0.00)
                .percentage(100)
                .expirationDate(paymentDTO.getFecha_pago())
                .datePaid(paymentDTO.getFecha_pago())
                .paymentId(paymentId)
                .build();

        return paymentPlan;
    }

    private Transaction mapToDomainTransaction(MakePaymentDTO paymentDTO, long paymentPlanId) {

        Transaction transaction = Transaction.builder()
                .id(0L)
                .amount(paymentDTO.getMonto_pago())
                .remainAmount(0.00)
                .datePaid(paymentDTO.getFecha_pago())
                .currencyTypeIdc(paymentDTO.getTipo_moneda())
                .paymentChannelIdc(PaymentChannelEnum.Cash.getValue())
                .transactionType(TransactionTypeEnum.PremiumPayment.getValue())
                .paymentPlanId(paymentPlanId)
                .build();

        return transaction;
    }

    private Receipt mapToDomainReceipt(MakePaymentDTO paymentDTO, long transactionId) {

        String numberToLiteral = HelpersMethods.convertNumberToLiteral(paymentDTO.getMonto_pago() + "", true);

        Receipt receipt = Receipt.builder()
                .id(0L)
                .voucherNumber(paymentDTO.getNro_comprobante())
                .paymentDate(paymentDTO.getFecha_pago())
                .concept("Pago de Prima")
                .totalAmount(paymentDTO.getMonto_pago())
                .literalAmount(numberToLiteral)
                .sellerId(paymentDTO.getId_usuario())
                .sellerName(paymentDTO.getNombre_usuario())
                .salePlace(paymentDTO.getLugar_venta())
                .agencyId(paymentDTO.getId_agencia())
                .agencyName(paymentDTO.getNombre_agencia())
                .receiptStatusIdc(ReceiptStatusEnum.PaidOut.getValue())
                .transactionId(transactionId)
                .build();

        return receipt;
    }

    //#endregion

    //#endregion


    //#region Revertir pago (Revert Payment)
    /*cosas a validar
    de acuerdo a la fecha, solo debe realizar la reversion aquello pagos realizado en el dia.
    realizar la validaciones necesarias */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public ReversalPaymentResponseDTO reversalPayment(ReversalPaymentDTO reversalPaymentDTO) {

        ReversalPaymentResponseDTO reversalPaymentResponseDTO = new ReversalPaymentResponseDTO();
        String notFoundMessage = "Operaciòn no completada, no se encontró ";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        // validamos los campos del dto
        String fields = ValidateReversalPaymentDTO(reversalPaymentDTO);
        String message = "";
        if (!fields.isEmpty()) {
            message = notFoundMessage + "los siguientes campos obligatorios: " + fields;
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }
        //0.1 que el comprobante exista -->el comprobante no existe, por lo tanto nose puede realizar la reversion del pago
        Receipt receipt = this.receiptPort.gReceiptForNumberReceipt(reversalPaymentDTO.getNro_comprobante());
        if (receipt == null) {
            message = "El comprobante " + String.valueOf(reversalPaymentDTO.getNro_comprobante()) + " no existe, por lo tanto no se puede realizar la reversión del pago";
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }

        GeneralRequest generalRequest = generalRequestPort.findGeneralByReceiptById(receipt.getId());
        //0.2 ,que la reversion seas en el dia --> No puede se realizar reversion pago, prosiga el conducto regular de la reversion manueles
        if (!validateReversalParymentDay(generalRequest)) {
            String dateReceipt = receipt.getPaymentDate().format(format);
            message = "No se puede realizar la reversión del pago, la fecha de compra del seguro fue el " + dateReceipt + ". La reversión se puede realizar únicamente el mismo día de la compra del seguro.";
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }
        //0.3 que estes pendiente de activacion -> no se puede realizar la reversion, por que ya cuenta el certificado de cobertura
        if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue()) {
            message = "No se puede realizar la reversión, por que ya cuenta el certificado de cobertura";
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }
        //0.4 que no se puede revertir mas dos veces --> el comprobante ingreasado ya se encuentra revertido.
        if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.CANCELLED.getValue()) {
            message = "El comprobante ingresado ya se encuentra revertido";
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }
        //0.5 no puede realizar la reversion, comniquese con el core de seguros.
        if (generalRequest.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
            //.- sacar el recibo correspoondiente al comprabante introducido

            LocalDateTime dateTime = LocalDateTime.now();
            String observation = "Revertiendo en fecha " + dateTime.format(format);
            //.- inserta in regsitros en transacionses con monto megativo
            Transaction transaction = transactionPort.findById(receipt.getTransactionId());
            transaction.setId(0L);
            transaction.setAmount(-transaction.getAmount());
            transaction.setObservation(observation);
            transaction.setAnnulmentReason(observation);
            transaction.setAnnulmentDate(LocalDateTime.now());
            transaction.setCreatedAt(new Date());
            transaction.setLastModifiedAt(new Date());
            Transaction transaction1 = new Transaction();
            transaction1 = transaction;
            long tr = transactionPort.saveOrUpdate(transaction);
            //.- actulizar los cambios quien estas revertiendo
            receipt.setSellerId(reversalPaymentDTO.getId_usuario());
            receipt.setSellerName(reversalPaymentDTO.getNombre_usuario());
            receipt.setAgencyId(reversalPaymentDTO.getId_agencia());
            receipt.setAgencyName(reversalPaymentDTO.getNombre_agencia());
            receipt.setObservation(observation);
            receipt.setLastModifiedAt(new Date());
            long recep = receiptPort.saveOrUpdate(receipt);
            //.- actulizar el estado de la solicitud
            generalRequest.setRequestStatusIdc(RequestStatusEnum.CANCELLED.getValue());
            long val = generalRequestPort.saveOrUpdate(generalRequest);
            if (recep > 0 && val > 0 && tr > 0) {
                message = "Operación completada correctamente";
                reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.OK.getValue(), message);
            } else {
                message = "No puede realizar la reversión de pago, comuníquese con el core de seguros";
                reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            }
            return reversalPaymentResponseDTO;
        } else {
            message = "No puede realizar la reversión de pago del siguiente comprobante: " + reversalPaymentDTO.getNro_comprobante() + ", comuníquese con el área técnica de Seguros.";
            reversalPaymentResponseDTO = setPaymentResponse(SMVSResponseEnum.ERROR.getValue(), message);
            return reversalPaymentResponseDTO;
        }
    }

    private boolean validateReversalParymentDay(GeneralRequest generalRequest) {
        LocalDateTime localDate = generalRequest.getRequestDate();
        LocalDateTime day = LocalDateTime.now();
        return localDate.getYear() == day.getYear() && localDate.getMonth() == day.getMonth() && localDate.getDayOfMonth() == day.getDayOfMonth();
    }

    private String ValidateReversalPaymentDTO(ReversalPaymentDTO reversalPaymentDTO) {
        String fields = "";
        if (reversalPaymentDTO.getNro_comprobante() == null || reversalPaymentDTO.getNro_comprobante().isEmpty())
            fields += "Nro comprobante, ";
        if (reversalPaymentDTO.getId_usuario() == null || reversalPaymentDTO.getId_usuario() == 0)
            fields += "Id usuario, ";
        if (reversalPaymentDTO.getNombre_agencia() == null || reversalPaymentDTO.getNombre_usuario().isEmpty())
            fields += "Nombre Usuario, ";
        if (reversalPaymentDTO.getId_agencia() == null || reversalPaymentDTO.getId_agencia() == 0)
            fields += "Id agencia, ";
        if (reversalPaymentDTO.getNombre_agencia() == null || reversalPaymentDTO.getNombre_agencia().isEmpty())
            fields += "Nombre Agencia";

        return fields;
    }

    ReversalPaymentResponseDTO setPaymentResponse(Integer responseCode, String message) {
        return ReversalPaymentResponseDTO.builder()
                .codigo_respuesta(responseCode)
                .mensaje(message)
                .build();
    }

    //#endregion
}
