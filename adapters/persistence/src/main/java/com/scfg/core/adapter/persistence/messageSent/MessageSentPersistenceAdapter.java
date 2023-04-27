package com.scfg.core.adapter.persistence.messageSent;

import com.scfg.core.adapter.persistence.messageTosend.MessageToSendJpaEntity;
import com.scfg.core.application.port.out.MessageSentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MessageSentPersistenceAdapter implements MessageSentPort {
    private final MessageSentRepository messageSentRepository;

    @Override
    public long saveOrUpdate(MessageDTO messageSent) {
        MessageSentJpaEntity messageSentJpaEntity = mapToJpaEntity(messageSent);
        messageSentJpaEntity = messageSentRepository.save(messageSentJpaEntity);
        return messageSentJpaEntity.getId();
    }

    @Override
    public List<MessageDTO> findAllByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc) {
        List<MessageSentJpaEntity> list = messageSentRepository
                .findByReferenceIdAndReferenceTableIdc(referenceId,
                        referenceTableIdc, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream()
                .map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO findByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc) {
        List<MessageSentJpaEntity> messageSentJpaEntity = messageSentRepository
                .findByReferenceIdAndReferenceTableIdc(referenceId,
                        referenceTableIdc, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        MessageDTO messageDTO = null;

        if (messageSentJpaEntity == null) {
            return null;
        }

        if (!messageSentJpaEntity.isEmpty()) {
            messageDTO = new ModelMapper().map(messageSentJpaEntity.get(0),MessageDTO.class);
            messageDTO.setLastNumberOfAttempt(messageDTO.getNumberOfAttempt());
        }
        return messageDTO;
    }


    //#region Mappers
    public static MessageSentJpaEntity mapToJpaEntity(MessageDTO messageToSend) {
        return MessageSentJpaEntity.builder()
                .id(messageToSend.getId())
                .messageTypeIdc(messageToSend.getMessageTypeIdc())
                .referenceId(messageToSend.getReferenceId())
                .to(messageToSend.getTo())
                .cc(messageToSend.getCc())
                .referenceTableIdc(messageToSend.getReferenceTableIdc())
                .message(messageToSend.getMessage())
                .subject(messageToSend.getSubject())
                .observation(messageToSend.getObservation())
                .numberOfAttempt(messageToSend.getNumberOfAttempt())
//                .createdAt(messageToSend.getLastModifiedAt())
                .build();
    }
    //#endregion
}
