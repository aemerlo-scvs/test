package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;

import java.util.List;

public interface SenderUseCase {
    boolean sendMessage(MessageDTO messageDTO);
    boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList);
}
