package com.scfg.core.adapter.persistence.messageResponse;

import com.scfg.core.application.port.out.MessageResponsePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MessageResponseAdapter implements MessageResponsePort {

    private final MessageResponseRepository messageResponseRepository;

    @Override
    public long saveOrUpdate(MessageResponse messageResponse) {
        MessageResponseJpaEntity messageResponseJpaEntity = new ModelMapper().map(messageResponse,MessageResponseJpaEntity.class);
        messageResponseJpaEntity = messageResponseRepository.save(messageResponseJpaEntity);
        return messageResponseJpaEntity.getId();
    }

    @Override
    public MessageResponse getMessageByMessageSentIds(List<Long> messageSentIds) {
        List<MessageResponseJpaEntity> list = messageResponseRepository.findAllByMessageSentIds(messageSentIds,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ModelMapper().map(list.get(0), MessageResponse.class);
    }

    @Override
    public MessageResponse getMessageByMessageSentId(Long messageSentId) {
        MessageResponseJpaEntity messageResponseJpaEntity = messageResponseRepository.findByMessageSentId(messageSentId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if (messageResponseJpaEntity == null) {
            return null;
        }
        return new ModelMapper().map(messageResponseJpaEntity,MessageResponse.class);
    }
}
