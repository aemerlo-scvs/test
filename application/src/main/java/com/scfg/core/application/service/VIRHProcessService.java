package com.scfg.core.application.service;

import com.scfg.core.application.port.in.VIRHUseCase;
import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.service.sender.SenderService;
import com.scfg.core.application.service.sender.WhatsAppSenderService;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.MessageTypeEnum;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VIRHProcessService implements VIRHUseCase {

    @PersistenceContext
    private EntityManager entityManager;

    private final FileDocumentPort fileDocumentPort;
    private final SenderService senderService;
    private final WhatsAppSenderService whatsAppSenderService;
    @Override
    public String getDataInformationPolicy(String param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_view_data_policy_propose");
        query.registerStoredProcedureParameter("param", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);
        query.setParameter("param", param);
        query.execute();
        String result = (String) query.getOutputParameterValue("result");

        return result;
    }

    @Override
    public FileDocument getDocument(Long id) {
        try {
            FileDocument fileDocument = this.fileDocumentPort.findById(id);
            return fileDocument;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean sendWhatsApp(String number, String message, Long requestId) {
        this.senderService.setStrategy(this.whatsAppSenderService);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(number);
        messageDTO.setMessage(message);
        messageDTO.setSubject("Envío notificación por WhatsApp");
        messageDTO.setMessageTypeIdc(MessageTypeEnum.WHATSAPP.getValue());
        messageDTO.setReferenceId(requestId);
        messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);
        return this.senderService.sendMessage(messageDTO);
    }

    @Override
    public Boolean sendWhatsAppWithAttachment(String number, String message, Long requestId, Long docId) {
        this.senderService.setStrategy(this.whatsAppSenderService);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(number);
        messageDTO.setMessage(message);
        messageDTO.setSubject("Envío notificación por WhatsApp con adjunto");
        messageDTO.setMessageTypeIdc(MessageTypeEnum.WHATSAPP.getValue());
        messageDTO.setReferenceId(requestId);
        messageDTO.setReferenceTableIdc((int) ClassifierEnum.REFERENCE_TABLE_GENERALREQUEST.getReferenceCode());
        messageDTO.setNumberOfAttempt(0);
        messageDTO.setLastNumberOfAttempt(0);

        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(docId+"");

        List<AttachmentDTO> list = new ArrayList<>();
        list.add(attachmentDTO);

        return this.senderService.sendMessageWithAttachment(messageDTO, list);
    }
}
