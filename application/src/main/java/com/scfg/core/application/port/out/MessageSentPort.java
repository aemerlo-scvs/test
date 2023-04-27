package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.MessageDTO;

import java.util.List;

public interface MessageSentPort {

    long saveOrUpdate(MessageDTO messageSent);

    List<MessageDTO> findAllByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc);

    MessageDTO findByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc);
}
