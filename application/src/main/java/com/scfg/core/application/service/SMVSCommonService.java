package com.scfg.core.application.service;

import com.scfg.core.application.port.in.SMVSCommonUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.enums.MessageTypeEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.enums.SMVSResponseEnum;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.common.util.ReCaptcha;
import com.scfg.core.common.credentials.SMVSReCaptchaConfig;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.Emailbody;
import com.scfg.core.domain.SendSmsDTO;
import com.scfg.core.domain.dto.MessageDTO;
import com.scfg.core.domain.person.Person;
import com.scfg.core.domain.smvs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMVSCommonService implements SMVSCommonUseCase {

    private final PersonPort personPort;
    private final DirectionPort directionPort;
    private final GeneralRequestPort generalRequestPort;
    private final MessageToSendPort messageToSendPort;
    private final MessageSentPort messageSentPort;

    private final EmailService emailService;
    private final SendMessageService messageService;

    private final SMVSReCaptchaConfig reCaptchaConfig;
    private final MyProperties path;

    private final Environment environment;

    private final String defaultEmail = "gaguilar@santacruzfg.com";

    private final AlertPort alertPort;

    @Override
    public SMVSResponseDTO verifyActivationCode(IngressFormDTO ingressForm) {

        SMVSResponseDTO smvsResponseDTO = new SMVSResponseDTO();
        smvsResponseDTO.setCodigo_respuesta(SMVSResponseEnum.ERROR.getValue());
        smvsResponseDTO.setMensaje("El código de activación o el número de identificación no es valido");

        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            if (!ReCaptcha.isCaptchaValid(reCaptchaConfig.getSecretKey(), ingressForm.getReCaptchaResponse())) {
                log.error("El reCaptcha no es valido commonService: " + ingressForm.getActivationCode() + " - " + ingressForm.getReCaptchaResponse() + " - " + ingressForm.getIdentificationNumber(), ingressForm);
                smvsResponseDTO.setMensaje("El reCaptcha no es valido");
                return smvsResponseDTO;
            }
        }

        VerifyActivationCodeDTO existsActivationCode = generalRequestPort.existsActivationCode(ingressForm.getActivationCode(), ingressForm.getIdentificationNumber());
        if (existsActivationCode != null) {

            if (existsActivationCode.getRequestStatusIdc() == RequestStatusEnum.CANCELLED.getValue()) {
                smvsResponseDTO.setMensaje("La solicitud fue revertida");
            }

            if (existsActivationCode.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue()) {
                smvsResponseDTO.setMensaje("La solicitud ya fue activada");
            }

            if (existsActivationCode.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
                smvsResponseDTO.setCodigo_respuesta(SMVSResponseEnum.OK.getValue());
                smvsResponseDTO.setMensaje("Operación realizada correctamente");
                smvsResponseDTO.setData(existsActivationCode);
            }
        }

        return smvsResponseDTO;
    }

    @Transactional
    @Override
    public Boolean updatePerson(Person person) {
        long personId = personPort.saveOrUpdate(person);
        long directionId = directionPort.saveOrUpdate(person.getDirection());
        return (personId > 0 && directionId > 0);
    }

    public MessageResponseDTO sendMessages(SendMessageDTO messageDTO) {

        Integer minimumNumberOfDigits = 5;
        String msgContent = "";
        long idAux = 0;

        MessageResponseDTO messageResponseDTO = MessageResponseDTO.builder()
                .emailSent(false)
                .smsSent(false)
                .whatsAppSent(false)
                .build();

        MessageDTO msgAux = setMessageDTO(messageDTO);

        if (messageDTO.getEmail() != null) {
            if (!messageDTO.getEmail().isEmpty()) {
                msgContent = getMsgContent(messageDTO, MessageTypeEnum.EMAIL);
                msgAux.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
                msgAux.setMessage(msgContent);

                MessageDTO msgEmail = messageToSendPort.saveOrUpdate(msgAux);

                boolean emailSent = sendEmail(messageDTO);
                if (emailSent) {
                    idAux = msgEmail.getId();
                    msgEmail.setId(0L);
                    messageSentPort.saveOrUpdate(msgEmail);
                    messageToSendPort.delete(idAux);
                } else {
                    msgEmail.setNumberOfAttempt(msgEmail.getNumberOfAttempt() + 1);
                    msgEmail.setObservation("Error al enviar el correo electrónico");
                    messageToSendPort.saveOrUpdate(msgEmail);
                }
                messageResponseDTO.setEmailSent(emailSent);
            }
        }

        if (messageDTO.getPhoneNumber() != null && Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            if (messageDTO.getPhoneNumber().length() > minimumNumberOfDigits) {

                SendSmsDTO sms = new SendSmsDTO();
                sms.setNumber(messageDTO.getPhoneNumber());

                //#region Enviar SMS
                msgContent = getMsgContent(messageDTO, MessageTypeEnum.SMS);
                sms.setMessage(msgContent);
                sms.setSendToWhatsApp(false);

                msgAux.setMessageTypeIdc(MessageTypeEnum.SMS.getValue());
                msgAux.setMessage(msgContent);

                MessageDTO msgSMS = messageToSendPort.saveOrUpdate(msgAux);

                boolean smsSent = messageService.sendSMS(sms);
                if (smsSent) {
                    idAux = msgSMS.getId();
                    msgSMS.setId(0L);
                    messageSentPort.saveOrUpdate(msgSMS);
                    messageToSendPort.delete(idAux);
                } else {
                    msgSMS.setNumberOfAttempt(msgSMS.getNumberOfAttempt() + 1);
                    msgSMS.setObservation("Error al enviar el mensaje de SMS");
                    messageToSendPort.saveOrUpdate(msgSMS);
                }
                messageResponseDTO.setSmsSent(smsSent);
                //#endregion

            }
        }

        return messageResponseDTO;
    }

    public Boolean sendContactCenterMessage(SendMessageDTO messageDTO) {
        Boolean emailSent = false;
        if (messageDTO.getEmail() != null || messageDTO.getEmails() != null) {
            emailSent = sendEmail(messageDTO);
        }
        return emailSent;
    }


    //#region Deprecated
    public Boolean sendEmail(SendMessageDTO messageDTO, String msgContent) {
        boolean emailSent = false;

        String[] emails = new String[]{messageDTO.getEmail()};
        if (messageDTO.getEmail() == null && messageDTO.getEmails().length > 0) {
            emails = messageDTO.getEmails();
        }

        if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
            emails = new String[]{defaultEmail};
        }

        Emailbody emailbody = Emailbody.builder()
                .content(msgContent)
                .email(emails)
                .emailcopy(messageDTO.getCcEmails())
                .name_attachment(messageDTO.getAttachmentName())
                .bytes(messageDTO.getAttachmentFile())
                .build();

        switch (messageDTO.getMessageTypeEnum()) {
            case WELCOME:
                emailbody.setSubject("Mensaje de Bienvenida");
                break;
            case ACTIVATION:
                emailbody.setSubject("Mensaje de Activación del Seguro");
                break;
            case SCHEDULED_CLIENT_MESSAGE:
                emailbody.setSubject("Recordatorio de Activación del Seguro");
                break;
            case SCHEDULED_CONTACT_CENTER_MESSAGE:
                emailbody.setSubject("Reporte de Clientes con solicitudes pendientes");
                break;
            case MAKE_PAYMENT_ALERT:
                emailbody.setSubject("Notificación en la compra del seguro de sepelio vida segura");
                break;
        }

        try {
            emailSent = emailService.sendEmail(emailbody);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return emailSent;
    }
    //#endregion

    public Emailbody getEmailBody(SendMessageDTO messageDTO) {

        String[] emails = new String[]{messageDTO.getEmail()};

        if (messageDTO.getEmail() == null && messageDTO.getEmails().length > 0) {
            emails = messageDTO.getEmails();
        }

        if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
            emails = new String[]{defaultEmail};
        }

        Emailbody emailbody = Emailbody.builder()
                .email(emails)
                .emailcopy(messageDTO.getCcEmails())
                .name_attachment(messageDTO.getAttachmentName())
                .bytes(messageDTO.getAttachmentFile())
                .build();

        Alert alert = alertPort.findByAlert(messageDTO.getMessageTypeEnum().getValue());
        emailbody.setSubject(alert.getMail_subject());

        switch (messageDTO.getMessageTypeEnum()) {
            case SMVS_WELCOME_MESSAGE:
                emailbody.setContent(getWelcomeEmailTemplate(messageDTO.getName(), messageDTO.getActivationCode(),
                        alert.getMail_body()));
                break;
            case SMVS_ACTIVATION:
                emailbody.setContent(getActivationEmailTemplate(messageDTO.getName(), messageDTO.getPolicyNumber(),
                        alert.getMail_body()));
                break;
            case SMVS_RECORDATORY:
                emailbody.setContent(getReminderActivationEmailTemplate(messageDTO.getName(),
                        messageDTO.getActivationCode(), alert.getMail_body()));
                break;
            case SMVS_PAYMENT_ALERT:
                emailbody.setEmail(new String[]{alert.getMail_to()});
                emailbody.setContent(alert.getMail_body());
                break;
            case SMVS_SCHEDULED_CLIENT_MESSAGE:
                emailbody.setContent(alert.getMail_body());
                emailbody.setEmail(alert.getMail_to().split(";"));
                emailbody.setEmailcopy(alert.getMail_cc().split(";"));
                break;
        }
        return emailbody;
    }

    public Boolean sendEmail(SendMessageDTO messageDTO) {
        boolean emailSent = false;
        try {
            emailSent = emailService.sendEmail(this.getEmailBody(messageDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return emailSent;
    }

    public Boolean sendEmail(SendMessageDTO messageDTO, String paymentDate, String errorMessage, String office,
                             String agencyId, String agencyName, String sellerId, String sellerName) {

        boolean emailSent = false;

        Emailbody emailbody = this.getEmailBody(messageDTO);
        emailbody.setContent(getPaymentAlertEmailTemplate(paymentDate, errorMessage, office, agencyId, agencyName,
                sellerId, sellerName, emailbody.getContent()));

        try {
            emailSent = emailService.sendEmail(emailbody);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return emailSent;
    }

    //#region Email Templates

    private String getWelcomeEmailTemplate(String name, String activationCode, String content) {
        String emailTemplate = content;

        emailTemplate = emailTemplate.replace("{urlRedirection}", "https://www.santacruzvidaysalud.com.bo/sepelio/");
        emailTemplate = emailTemplate.replace("{name}", name);
        emailTemplate = emailTemplate.replace("{activationCode}", activationCode);

        return emailTemplate;
    }

    private String getActivationEmailTemplate(String name, String policyNumber, String content) {
        String emailTemplate = content;

        emailTemplate = emailTemplate.replace("{name}", name);
        return emailTemplate;
    }

    private String getReminderActivationEmailTemplate(String name, String activationCode, String content) {
        String emailTemplate = content;

        emailTemplate = emailTemplate.replace("{urlRedirection}", "https://www.santacruzvidaysalud.com.bo/sepelio/");
        emailTemplate = emailTemplate.replace("{name}", name);
        emailTemplate = emailTemplate.replace("{activationCode}", activationCode);
        return emailTemplate;
    }

    public String getPaymentAlertEmailTemplate(String paymentDate, String errorMessage, String office, String agencyId, String agencyName, String sellerId, String sellerName, String content) {
        String emailTemplate = content;

        emailTemplate = emailTemplate.replace("{paymentDate}", paymentDate);
        emailTemplate = emailTemplate.replace("{errorMessage}", errorMessage);
        emailTemplate = emailTemplate.replace("{office}", office);
        emailTemplate = emailTemplate.replace("{agencyId}", agencyId);
        emailTemplate = emailTemplate.replace("{agencyName}", agencyName);
        emailTemplate = emailTemplate.replace("{sellerId}", sellerId);
        emailTemplate = emailTemplate.replace("{sellerName}", sellerName);

        return emailTemplate;
    }

    //#endregion

    public String getCompleteName(String name, String lastName, String motherLastName, String marriedLastName) {
        String completeName = "";
        if (name != null && !name.isEmpty()) completeName += name.trim();
        if (lastName != null && !lastName.isEmpty()) completeName += " " + lastName.trim();
        if (motherLastName != null && !motherLastName.isEmpty()) completeName += " " + motherLastName.trim();
        if (marriedLastName != null && !marriedLastName.isEmpty()) {
            if (!marriedLastName.toUpperCase().trim().contains("DE ")) {
                String d = marriedLastName.equals(marriedLastName.toUpperCase()) ? " DE " : " de ";
                completeName += d + marriedLastName.trim();
            }else{
                completeName += " " + marriedLastName.trim();
            }
        }
        return completeName;
    }

    private String getMsgContent(SendMessageDTO messageDTO, MessageTypeEnum messageTypeEnum) {
        String msg = "";

        if (messageTypeEnum.getValue() == MessageTypeEnum.WHATSAPP.getValue()) {
            switch (messageDTO.getMessageTypeEnum()) {
                case SMVS_WELCOME_MESSAGE:
                    msg = "Bienvenido(a), " + messageDTO.getName() +
                            "\nGracias por adquirir nuestro Seguro de *Sepelio Vida Segura* y por la confianza depositada en Santa Cruz Vida y Salud.\n" +
                            "Te damos la bienvenida y esperamos poder apoyarte a vos y a tu familia cuando más nos necesiten.\n" +
                            "\nIngresá con el siguiente código \n*" + messageDTO.getActivationCode() + "*\n\n " +
                            "al siguiente enlace: https://www.santacruzvidaysalud.com.bo/sepelio/ \n\n" +
                            "para la activación de tu seguro.";
                    break;
                case SMVS_ACTIVATION:
                    msg = "Hola, " + messageDTO.getName() +
                            "\nGracias por confiar en nuestra compañía. " +
                            "\nAquí podrás encontrar tu póliza de Sepelio Vida Segura con todas las condiciones de cobertura, esperamos que sea de tu total agrado.";
                    break;
                case SMVS_RECORDATORY:
                    msg = "Hola, " + messageDTO.getName() +
                            "\nTenés pendiente la activación de tu seguro de *Sepelio Vida Segura*, " +
                            "\nIngresá con el siguiente código \n*" + messageDTO.getActivationCode() + "*\n\n " +
                            "al siguiente enlace: https://www.santacruzvidaysalud.com.bo/sepelio/ \n\n" +
                            "para la activación de tu seguro.";
                    break;
            }
        }

        if (messageTypeEnum.getValue() == MessageTypeEnum.SMS.getValue()) {
            switch (messageDTO.getMessageTypeEnum()) {
                case SMVS_WELCOME_MESSAGE:
                    msg = "Ingresá a www.santacruzseguros.com.bo tu código de activación " + messageDTO.getActivationCode();
                    break;
                case SMVS_ACTIVATION:
                    msg = "Revisá tu correo electrónico donde encontrarás tu póliza activada";
                    break;
                case SMVS_RECORDATORY:
                    msg = "Recordatorio, activá tu seguro de Sepelio Vida Segura";
                    break;
            }
        }

        return msg;
    }

    private MessageDTO setMessageDTO(SendMessageDTO message) {
        MessageDTO msg = new MessageDTO();
        msg.setId(0L);
//        msg.setProduct("Sepelio Vida Segura");
        msg.setReferenceId(message.getRequestId());
        if (message.getMessageTypeEnum().equals(MessageTypeEnum.EMAIL.getValue())) {
            msg.setTo(message.getEmail());
        } else {
            msg.setTo(message.getPhoneNumber());
        }
        msg.setNumberOfAttempt(0);

        if (message.getAttachmentName() != null && message.getAttachmentFile() != null) {
//            msg.setFilename(message.getAttachmentName());
//            msg.setUrlFile(path.getPathCertificateCoverage() + message.getAttachmentName());
        }

        return msg;
    }

}
