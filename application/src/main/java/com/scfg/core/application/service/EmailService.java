package com.scfg.core.application.service;

import com.scfg.core.application.port.in.EmailPortIn;
import com.scfg.core.application.port.out.MessageSentPort;
import com.scfg.core.application.port.out.MessageToSendPort;
import com.scfg.core.common.enums.MessageTypeEnum;
import com.scfg.core.domain.Emailbody;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailPortIn {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSenderImpl sender;

    private final MessageToSendPort messageToSendPort;
    private final MessageSentPort messageSentPort;

    @Override
    public boolean sendEmail(Emailbody emailbody) throws MessagingException {

        boolean sent = false;

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        try {
            helper.setFrom(this.sender.getUsername());
            helper.setTo(emailbody.getEmail());
            if (emailbody.getEmailcopy() != null){
                helper.setCc(emailbody.getEmailcopy());
            }
            helper.setText(emailbody.getContent(), true);
            helper.setSubject(emailbody.getSubject());

            if (emailbody.getBytes() != null) {
                ByteArrayResource resource = new ByteArrayResource(emailbody.getBytes());
                String filename = emailbody.getName_attachment();
                if (resource != null) {
                    helper.addAttachment(filename, resource);
                }
            }

            sender.send(message);
            sent = true;
        } catch (javax.mail.MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: ", e);

        }
        return sent;
    }

    @Override
    public boolean sendNewEmail(Emailbody emailbody) {
        boolean sent = false;

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(this.sender.getUsername());
            helper.setTo(emailbody.getEmail());
            if (emailbody.getEmailcopy() != null) helper.setCc(emailbody.getEmailcopy());
            helper.setText(emailbody.getContent(), true);
            helper.setSubject(emailbody.getSubject());

            if (emailbody.getBytes() != null) {
                ByteArrayResource resource = new ByteArrayResource(emailbody.getBytes());
                String filename = emailbody.getName_attachment();
                if (resource != null) {
                    helper.addAttachment(filename, resource);
                }
            }

            sender.send(message);
            sent = true;
        } catch (Exception e) {
            log.error("Ocurrió un error al enviar el mail: {}", e.getMessage());
        }
        return sent;
    }

    @Override
    public void saveEmail(Emailbody emailbody) {
        String msgContent = "";
        long idAux = 0;

        MessageDTO msg = new MessageDTO();
        msg.setId(0L);
        msg.setReferenceId(emailbody.getReferenceId());
        msg.setTo(emailbody.getEmail().toString());
        msg.setNumberOfAttempt(0);

        if (emailbody.getName_attachment() != null && emailbody.getBytes() != null) {
//            msg.setFilename(emailbody.getName_attachment());
        }

        if (emailbody.getEmail() != null) {
            if (emailbody.getEmail().length > 0) {
                msg.setMessageTypeIdc(MessageTypeEnum.EMAIL.getValue());
                msg.setMessage(emailbody.getContent());

                MessageDTO msgEmail = messageToSendPort.saveOrUpdate(msg);

                boolean emailSent = this.sendNewEmail(emailbody);
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
            }
        }
    }
}
