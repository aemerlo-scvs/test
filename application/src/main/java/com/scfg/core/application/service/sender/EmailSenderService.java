package com.scfg.core.application.service.sender;

import com.scfg.core.application.port.in.SenderUseCase;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Service("EmailSenderService")
@RequiredArgsConstructor
public class EmailSenderService implements SenderUseCase {
    private final JavaMailSenderImpl sender;

    private MimeMessage setHelper(String[] to, String[] cc, String subject, String message) {
        MimeMessage messageAux = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(messageAux, true, "UTF-8");
            helper.setFrom(this.sender.getUsername());
            helper.setTo(to);
            helper.setCc(cc);
            helper.setText(message, true);
            helper.setSubject(subject);

        } catch (Exception e) {
            messageAux = null;
            log.error("Ocurrió un error al enviar el mail: {}", e.getMessage());
        }
        return messageAux;
    }

    private MimeMessage setHelperWithAttachment(String[] to, String[] cc, String subject, String message, List<AttachmentDTO> attachmentList) {
        MimeMessage messageAux = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(messageAux, true, "UTF-8");
            helper.setFrom(this.sender.getUsername());
            helper.setTo(to);
            helper.setCc(cc);
            helper.setText(message, true);
            helper.setSubject(subject);

            if (!attachmentList.isEmpty()) {
                for (AttachmentDTO attachment: attachmentList) {
                    ByteArrayResource resource = new ByteArrayResource(attachment.getContent());
                    String fileName = attachment.getFileName();
                    if (resource != null) {
                        helper.addAttachment(fileName, resource);
                    }
                }
            }

        } catch (Exception e) {
            messageAux = null;
            log.error("Ocurrió un error al enviar el mail: {}", e.getMessage());
        }
        return messageAux;
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        boolean sent = false;
        try {
            MimeMessage messageAux = this.setHelper(messageDTO.getSendTo(), messageDTO.getSendCc(),
                    messageDTO.getSubject(), messageDTO.getMessage());
            if (messageAux == null) {
                return false;
            }
            sender.send(messageAux);
            sent = true;
        } catch (Exception e) {
            log.error("Ocurrió un error al enviar el mail: {}", e.getMessage());
        }
        return sent;
    }

    @Override
    public boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList) {
        boolean sent = false;
        try {
            MimeMessage messageAux = this.setHelperWithAttachment(messageDTO.getSendTo(), messageDTO.getSendCc(),
                    messageDTO.getSubject(), messageDTO.getMessage(), attachmentList);
            if (messageAux == null) {
                return false;
            }
            sender.send(messageAux);
            sent = true;
        } catch (Exception e) {
            log.error("Ocurrió un error al enviar el mail: {}", e.getMessage());
        }
        return sent;
    }

    public boolean sendMessageWithCopy(String[] to, String[] cc, String subject, String message) {
        MimeMessage messageAux = this.setHelper(to, cc, subject, message);
        if (messageAux == null) {
            return false;
        }
        sender.send(messageAux);
        return true;
    }

}
