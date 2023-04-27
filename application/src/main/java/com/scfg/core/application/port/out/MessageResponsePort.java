package com.scfg.core.application.port.out;

import com.scfg.core.domain.MessageResponse;

import java.util.List;

public interface MessageResponsePort {
    long saveOrUpdate(MessageResponse messageResponse);

    MessageResponse getMessageByMessageSentIds(List<Long> messageSentIds);
    MessageResponse getMessageByMessageSentId(Long messageSentId);
}
