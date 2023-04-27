package com.scfg.core.application.service;

import com.scfg.core.application.port.out.DirectionPort;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.application.port.out.PersonPort;
import com.scfg.core.application.port.out.ReceiptPort;
import com.scfg.core.common.enums.TypesDocumentPersonEnum;
import com.scfg.core.common.exception.NotFileWriteReadException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Receipt;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.person.Person;
import com.scfg.core.domain.smvs.PendingActivateErrorDTO;
import com.scfg.core.domain.smvs.SavePolicyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SMVSAutomaticGeneratePolicy {
    @Resource
    SMVSGeneratePolicy generatePolicy;
    private final GeneralRequestPort generalRequestPort;
    private final PersonPort personPort;
    private final DirectionPort directionPort;
    private final ReceiptPort receiptPort;

    public List<PendingActivateErrorDTO> generatePendingFromPeriod(String fromDate, String toDate) {
        List<GeneralRequest> generalRequestList = generalRequestPort.getAllByPendingAndActivationCode(fromDate,toDate);
        List<Long> personIds = generalRequestList.stream().map(GeneralRequest::getPersonId).collect(Collectors.toList());
        List<Long> requestIds = generalRequestList.stream().map(GeneralRequest::getId).collect(Collectors.toList());
        List<Person> personList = personPort.findAllByListOfPersonId(personIds);
        List<Direction> directionList = directionPort.findAllByListOfPersonId(personList.stream().map(Person::getId).collect(Collectors.toList()));
        List<PendingActivateErrorDTO> pendingActivateErrorDTOS = new ArrayList<>();

        for (GeneralRequest request: generalRequestList) {
            Receipt receipt = receiptPort.getReceiptForGeneralRequestById(request.getId());

            Person person = personList.stream().filter(x -> x.getId().equals(request.getPersonId())).findFirst().orElse(new Person());
            person.setDirection(directionList.stream().filter(x -> x.getPersonId().equals(person.getId())).findFirst().orElse(new Direction()));

            Beneficiary beneficiary = new Beneficiary();
            beneficiary.setName("HEREDEROS");
            beneficiary.setLastName("LEGALES");
            beneficiary.setMotherLastName("");
            beneficiary.setMarriedLastName("");
            beneficiary.setPercentage(100);
            beneficiary.setRelationshipIdc(7);
            beneficiary.setIsUnderAge(0);
            List<Beneficiary> beneficiaryList = new ArrayList<>();
            beneficiaryList.add(beneficiary);

            SavePolicyDTO savePolicyDTO = new SavePolicyDTO();
            savePolicyDTO.setRequestId(request.getId());
            savePolicyDTO.setPerson(person);
            savePolicyDTO.setBeneficiaryList(beneficiaryList);
            savePolicyDTO.setDocumentList(new ArrayList<>());

            try {
                BufferedImage systemFirm = textToImage("Activado automáticamente,\nsegún comprobante No. " + receipt.getVoucherNumber(),64);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(systemFirm,"png",baos);
                FileDocumentDTO fileFirm = new FileDocumentDTO();
                fileFirm.setContent(Base64.getEncoder().encodeToString(baos.toByteArray()));
                fileFirm.setMime("image/png");
                fileFirm.setTypeId(TypesDocumentPersonEnum.DIGITALFIRM.getValue());
                savePolicyDTO.setDocumentFirm(fileFirm);
                String test = this.generatePolicy.generatePolicyPendingToActivate(savePolicyDTO, request);
                if (test != null) {
                    PendingActivateErrorDTO scsfh = PendingActivateErrorDTO.builder()
                            .name(request.getDescription())
                            .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                            .message("Póliza generada exitósamente --> " + test)
                            .activationCode(request.getActivationCode())
                            .requestDate(receipt.getCreatedAt())
                            .statusMessage("Exitoso")
                            .build();
                    pendingActivateErrorDTOS.add(scsfh);
                } else {
                    PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                            .name(request.getDescription())
                            .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                            .message("Error al querer generar la póliza --> " + test)
                            .activationCode(request.getActivationCode())
                            .requestDate(receipt.getCreatedAt())
                            .statusMessage("Falló")
                            .build();
                    pendingActivateErrorDTOS.add(error);
                }
            }  catch (NotFileWriteReadException ex) {
                PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                        .name(request.getDescription())
                        .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                        .message("Error al querer generar la póliza --> " + ex.getMessage())
                        .activationCode(request.getActivationCode())
                        .requestDate(receipt.getCreatedAt())
                        .statusMessage("Falló")
                        .build();
                pendingActivateErrorDTOS.add(error);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("Error al generar la póliza automatica en el periodo seleccionado", ex.getMessage());
            }   catch (Exception e) {
                PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                        .name(request.getDescription())
                        .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                        .message("Error al querer generar la póliza --> " + e.getMessage())
                        .activationCode(request.getActivationCode())
                        .requestDate(receipt.getCreatedAt())
                        .statusMessage("Falló")
                        .build();
                pendingActivateErrorDTOS.add(error);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("Error al generar la póliza automatica en el periodo seleccionado", e.getMessage());
            }
        }
        generatePolicy.sendMailWithExcel(pendingActivateErrorDTOS);
        return pendingActivateErrorDTOS;
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "America/La_Paz")
    public List<PendingActivateErrorDTO> generatePendingAutomaticDiary() {
        Calendar toDay = DateUtils.getDateNowByGregorianCalendar();
        toDay.add(Calendar.DAY_OF_MONTH, -32);
        toDay.setTime(DateUtils.changeHourInDateMorningAndNight(toDay.getTime(),true,false));
        List<GeneralRequest> generalRequestList = generalRequestPort.getAllByPendingAndActivationCodeLastThirtyOneDay(LocalDateTime.ofInstant(toDay.toInstant(),toDay.getTimeZone().toZoneId()));
        List<Long> personIds = generalRequestList.stream().map(GeneralRequest::getPersonId).collect(Collectors.toList());
        List<Person> personList = personPort.findAllByListOfPersonId(personIds);
        List<Direction> directionList = directionPort.findAllByListOfPersonId(personList.stream().map(Person::getId).collect(Collectors.toList()));
        List<PendingActivateErrorDTO> pendingActivateErrorDTOS = new ArrayList<>();

        for (GeneralRequest request: generalRequestList) {
            Receipt receipt = receiptPort.getReceiptForGeneralRequestById(request.getId());

            Person person = personList.stream().filter(x -> x.getId().equals(request.getPersonId())).findFirst().orElse(new Person());
            person.setDirection(directionList.stream().filter(x -> x.getPersonId().equals(person.getId())).findFirst().orElse(new Direction()));

            Beneficiary beneficiary = new Beneficiary();
            beneficiary.setName("HEREDEROS");
            beneficiary.setLastName("LEGALES");
            beneficiary.setMotherLastName("");
            beneficiary.setMarriedLastName("");
            beneficiary.setPercentage(100);
            beneficiary.setRelationshipIdc(7);
            beneficiary.setIsUnderAge(0);
            List<Beneficiary> beneficiaryList = new ArrayList<>();
            beneficiaryList.add(beneficiary);

            SavePolicyDTO savePolicyDTO = new SavePolicyDTO();
            savePolicyDTO.setRequestId(request.getId());
            savePolicyDTO.setPerson(person);
            savePolicyDTO.setBeneficiaryList(beneficiaryList);
            savePolicyDTO.setDocumentList(new ArrayList<>());

            try {
                BufferedImage systemFirm = textToImage("Activado automáticamente,\nsegún comprobante No. " + receipt.getVoucherNumber(),64);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(systemFirm,"png",baos);
                FileDocumentDTO fileFirm = new FileDocumentDTO();
                fileFirm.setContent(Base64.getEncoder().encodeToString(baos.toByteArray()));
                fileFirm.setMime("image/png");
                fileFirm.setTypeId(TypesDocumentPersonEnum.DIGITALFIRM.getValue());
                savePolicyDTO.setDocumentFirm(fileFirm);
                String test = this.generatePolicy.generatePolicyPendingToActivate(savePolicyDTO, request);
                if (test != null) {
                    PendingActivateErrorDTO scsfh = PendingActivateErrorDTO.builder()
                            .name(request.getDescription())
                            .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                            .message("Póliza generada exitósamente --> " + test)
                            .activationCode(request.getActivationCode())
                            .requestDate(receipt.getCreatedAt())
                            .statusMessage("Exitoso")
                            .build();
                    pendingActivateErrorDTOS.add(scsfh);
                } else {
                    PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                            .name(request.getDescription())
                            .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                            .message("Error al querer generar la póliza --> " + test)
                            .activationCode(request.getActivationCode())
                            .requestDate(receipt.getCreatedAt())
                            .statusMessage("Falló")
                            .build();
                    pendingActivateErrorDTOS.add(error);
                }
            }  catch (NotFileWriteReadException ex) {
                PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                        .name(request.getDescription())
                        .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                        .message("Error al querer generar la póliza --> " + ex.getMessage())
                        .activationCode(request.getActivationCode())
                        .requestDate(receipt.getCreatedAt())
                        .statusMessage("Falló")
                        .build();
                pendingActivateErrorDTOS.add(error);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("Error al generar la póliza automatica en el periodo seleccionado", ex.getMessage());
            }   catch (Exception e) {
                PendingActivateErrorDTO error = PendingActivateErrorDTO.builder()
                        .name(request.getDescription())
                        .documentNumber(person.getNaturalPerson().getIdentificationNumber())
                        .message("Error al querer generar la póliza --> " + e.getMessage())
                        .activationCode(request.getActivationCode())
                        .requestDate(receipt.getCreatedAt())
                        .statusMessage("Falló")
                        .build();
                pendingActivateErrorDTOS.add(error);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("Error al generar la póliza automatica en el día correspondiente", e.getMessage());
            }
        }
        generatePolicy.sendMailWithExcel(pendingActivateErrorDTOS);
        return pendingActivateErrorDTOS;
    }

    public static BufferedImage textToImage(String text, float size) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        String[] lines = text.split("\n");
        int lineHeight = g2d.getFontMetrics().getHeight();

        int width = 600;
        int height = 230;
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < lines.length; i++) {
            String texto = lines[i];
            int yPos = (fm.getAscent() + 150) + i * lineHeight;
            g2d.drawString(texto,0, yPos);
        }
        g2d.dispose();

        return img;
    }
}
