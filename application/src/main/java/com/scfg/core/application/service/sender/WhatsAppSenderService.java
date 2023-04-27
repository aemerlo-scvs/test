package com.scfg.core.application.service.sender;

import com.scfg.core.application.port.in.SenderUseCase;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("WhatsAppSenderService")
@RequiredArgsConstructor
public class WhatsAppSenderService implements SenderUseCase {

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        return false;
    }

    @Override
    public boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList) {
        return false;
    }
}
