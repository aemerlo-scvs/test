package com.scfg.core.application.service.sender;

import com.scfg.core.application.port.in.SenderUseCase;
import com.scfg.core.application.port.out.MessageSentPort;
import com.scfg.core.application.port.out.MessageToSendPort;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderService implements SenderUseCase {

    private SenderUseCase strategy;
    private final MessageToSendPort messageToSendPort;
    private final MessageSentPort messageSentPort;

    public void setStrategy(SenderUseCase strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        long idAux = 0l;
        MessageDTO msgEmail = messageToSendPort.saveOrUpdate(messageDTO);
        boolean status = strategy.sendMessage(messageDTO);
        if (status) {
            idAux = msgEmail.getId();
            msgEmail.setId(0l);
            messageSentPort.saveOrUpdate(msgEmail);
            messageToSendPort.delete(idAux);
        } else {
            if (msgEmail.getLastNumberOfAttempt() == null) {
                msgEmail.setLastNumberOfAttempt(msgEmail.getNumberOfAttempt());
            }
            if ((msgEmail.getLastNumberOfAttempt() - msgEmail.getNumberOfAttempt()) <= 0) {
                msgEmail.setNumberOfAttempt(msgEmail.getNumberOfAttempt() + 1);
            }
            msgEmail.setObservation("Error al enviar el mensaje");
            messageToSendPort.saveOrUpdate(msgEmail);
        }
        return status;
    }

    @Override
    public boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList) {
        long idAux = 0l;
        MessageDTO msgEmail = messageToSendPort.saveOrUpdate(messageDTO);
        boolean status = strategy.sendMessageWithAttachment(messageDTO,attachmentList);
        if (status) {
            idAux = msgEmail.getId();
            msgEmail.setId(0l);
            messageSentPort.saveOrUpdate(msgEmail);
            messageToSendPort.delete(idAux);
        } else {
            if (msgEmail.getLastNumberOfAttempt() == null) {
                msgEmail.setLastNumberOfAttempt(msgEmail.getNumberOfAttempt());
            }
            if ((msgEmail.getLastNumberOfAttempt() - msgEmail.getNumberOfAttempt()) <= 0) {
                msgEmail.setNumberOfAttempt(msgEmail.getNumberOfAttempt() + 1);
            }
            msgEmail.setObservation("Error al enviar el mensaje");
            messageToSendPort.saveOrUpdate(msgEmail);
        }
        return status;
    }
}
