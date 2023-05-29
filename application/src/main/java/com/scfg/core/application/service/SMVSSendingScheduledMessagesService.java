package com.scfg.core.application.service;

import com.scfg.core.application.port.out.AlertPort;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.common.enums.AlertEnum;
import com.scfg.core.common.enums.SMVSMessageTypeEnum;
import com.scfg.core.common.util.ConvertBase64ToPdf;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.Emailbody;
import com.scfg.core.domain.dto.FileDocumentDTOInf;
import com.scfg.core.domain.dto.ReportRequestPolicyDTO;
import com.scfg.core.domain.smvs.ContactCenterRequestDTO;
import com.scfg.core.domain.smvs.SendMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMVSSendingScheduledMessagesService {

    private final SMVSCommonService smvsCommonService;
    private final GeneralRequestPort generalRequestPort;
    private final GenerateReportsService generateReportsService;
    private final SMVSReportService smvsReportService;
    private final EmailService emailService;
    private final AlertPort alertPort;

    // Tiempos expresados en Dias
    private final int dayAfterPurchase = 1;
    private final int minTime = 4;
    private final int maxTime = 10;
    private final Integer productAgreementCode = 746;

    @Profile(value="prod")
    @Scheduled(cron = "0 0 6 * * *", zone = "America/La_Paz")
    public void sendMessages() {

        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        today.with(LocalTime.MIN);

        List<ContactCenterRequestDTO> requestList = generalRequestPort.getAllPendingGeneralRequestByProductAgreementCode(productAgreementCode);
        List<ContactCenterRequestDTO> contactCenterRequestList = new ArrayList<>();

        requestList.forEach(r -> {
            long t = ChronoUnit.DAYS.between(r.getRequestDate().with(LocalTime.MIN), today);

            SendMessageDTO messageDTO = this.getMessageDTO(r);
            if (t == minTime) { // Primera notificación
                smvsCommonService.sendMessages(messageDTO);
            }
            if (t > minTime && t <= maxTime) { // Segunda notificación
                smvsCommonService.sendMessages(messageDTO);
            }
            if (t >= dayAfterPurchase) {
                contactCenterRequestList.add(r);
            }
        });

        //#region Tercera notificación (Contact Center)

        if (contactCenterRequestList.size() > 0) {
            List<String> formattedHeaderList = new ArrayList<>();
            formattedHeaderList.add("Nombre Completo Asegurado");
            formattedHeaderList.add("Código de activación");
            formattedHeaderList.add("Telf. Domicilio");
            formattedHeaderList.add("Celular");
            formattedHeaderList.add("Correo Electrónico");
            formattedHeaderList.add("Fecha de Venta");
            formattedHeaderList.add("Punto de Venta");
            formattedHeaderList.add("Nro. Comprobante de pago");
            formattedHeaderList.add("Estado de Solicitud");

            byte[] file = generateReportsService.generateExcelByteArray(formattedHeaderList, new ArrayList<>(contactCenterRequestList));
            if (file != null) {

                String[] to = new String[]{""};
                String[] cc = new String[]{""};

                SendMessageDTO messageDTOAux = SendMessageDTO.builder()
                        .emails(to)
                        .ccEmails(cc)
                        .messageTypeEnum(AlertEnum.SMVS_SCHEDULED_CLIENT_MESSAGE)
                        .attachmentName("SMVS - Reporte Clientes.xlsx")
                        .attachmentFile(file)
                        .build();
                smvsCommonService.sendContactCenterMessage(messageDTOAux);
            }
        }

        //#endregion

    }
    @Profile(value="prod")
    private SendMessageDTO getMessageDTO(ContactCenterRequestDTO requestDTO) {
        return SendMessageDTO.builder()
                .name(requestDTO.getCompleteName())
                .email((requestDTO.getEmail() == null) ? "" : requestDTO.getEmail())
                .activationCode(requestDTO.getActivationCode())
                .phoneNumber((requestDTO.getTelephone() == null) ? "" : requestDTO.getTelephone().toString())
                .messageTypeEnum(AlertEnum.SMVS_RECORDATORY)
                .build();
    }
    @Profile(value="prod")
    @Scheduled(cron = "0 30 23 * * *", zone = "America/La_Paz")
    public void sendReportCommercials() {
        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        Emailbody emailbody = new Emailbody();
        ReportRequestPolicyDTO parameter = new ReportRequestPolicyDTO();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Locale spanishLocale = new Locale("es", "ES");
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", spanishLocale);
        DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("dd MMMM", spanishLocale);
        Calendar c = Calendar.getInstance();
        Date date = DateUtils.asDate(today);
        c.setTime(date);
        Calendar actual = c;
//restamos do meses
        c.add(Calendar.MONTH, -2);
        //primer dia mes anterior
        c.set(Calendar.DAY_OF_MONTH, 1);
        // establecemos fechas
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Calendar cc = c;
        try {
            parameter.setDateto(date);

            // calendar.set(Calendar.MONTH, -1);
            parameter.setDatefrom(format.parse(format.format(cc.getTime())));

            FileDocumentDTOInf fileDocumentDTOInf = smvsReportService.ReportSUMSReportCommercialsNew(parameter);
            Alert alert = alertPort.findByAlert(SMVSMessageTypeEnum.SCHEDULED_REPORT_COMMERCIALS.getValue());

            String message = alert.getMail_body();
            LocalDate dateto = DateUtils.asDateToLocalDate(parameter.getDateto());
            LocalDate datefrom = DateUtils.asDateToLocalDate(parameter.getDatefrom());
//Get formatted String
            String p1 = FOMATTER2.format(datefrom);
            String p2 = FOMATTER.format(dateto);
            message = message.replaceAll("<P1>", p1);
            //remplazar la etiquetas <P1>
            message = message.replaceAll("<P2>", p2);
            String subject = alert.getMail_subject();
            emailbody.setContent(message);
            emailbody.setSubject(subject);
            if (!alert.getMail_to().isEmpty()) {
                emailbody.setEmail(alert.getMail_to().split(";"));
            }
            if (!alert.getMail_cc().isEmpty()) {
                emailbody.setEmailcopy(alert.getMail_cc().split(";"));
            }
            if (fileDocumentDTOInf != null && fileDocumentDTOInf.getContent() != null) {
                byte file[] = Base64.getDecoder().decode(fileDocumentDTOInf.getContent().getBytes(StandardCharsets.UTF_8));
                emailbody.setBytes(file);
                emailbody.setName_attachment(fileDocumentDTOInf.getName() + ".xlsx");
            }
            boolean b = emailService.sendEmail(emailbody);

        } catch (ParseException e) {
            log.error(e.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}
