package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.MessageDTO;

public interface MessageToSendPort {

    MessageDTO findById(long id);

    MessageDTO saveOrUpdate(MessageDTO messageToSent);

    boolean delete(long id);

    MessageDTO findByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc);
}
