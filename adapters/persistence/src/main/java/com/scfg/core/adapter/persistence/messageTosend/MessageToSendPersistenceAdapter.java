package com.scfg.core.adapter.persistence.messageTosend;

import com.scfg.core.application.port.out.MessageToSendPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MessageToSendPersistenceAdapter implements MessageToSendPort {
    private final MessageToSendRepository messageToSendRepository;

    @Override
    public MessageDTO findById(long id) {
        MessageToSendJpaEntity messageToSendJpaEntity = messageToSendRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Mensaje: " + id + " No encontrado"));
        return mapToDomain(messageToSendJpaEntity);
    }

    @Override
    public MessageDTO saveOrUpdate(MessageDTO messageToSent) {
        MessageToSendJpaEntity messageToSendJpaEntity = mapToJpaEntity(messageToSent);
        messageToSendJpaEntity = messageToSendRepository.save(messageToSendJpaEntity);
        return mapToDomain(messageToSendJpaEntity);
    }

    @Override
    public boolean delete(long id) {
        MessageToSendJpaEntity messageToSendJpaEntity = messageToSendRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Mensaje: " + id + " No encontrado"));
        messageToSendRepository.delete(messageToSendJpaEntity);
        return true;
    }

    @Override
    public MessageDTO findByReferenceIdAndTableReferenceIdc(Long referenceId, Integer referenceTableIdc) {
        List<MessageToSendJpaEntity> messageToSendJpaEntity = messageToSendRepository
                                                        .findByReferenceIdAndReferenceTableIdc(referenceId,
                                                                referenceTableIdc,
                                                                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        MessageDTO messageDTO = null;
        if (!messageToSendJpaEntity.isEmpty()) {
            messageDTO = new ModelMapper().map(messageToSendJpaEntity.get(0),MessageDTO.class);
            messageDTO.setLastNumberOfAttempt(messageDTO.getNumberOfAttempt());
        }
        return messageDTO;
    }

    //#region Mappers
    public static MessageToSendJpaEntity mapToJpaEntity(MessageDTO messageToSend) {
        return MessageToSendJpaEntity.builder()
                .id(messageToSend.getId())
                .messageTypeIdc(messageToSend.getMessageTypeIdc())
                .referenceId(messageToSend.getReferenceId())
                .message(messageToSend.getMessage())
                .subject(messageToSend.getSubject())
                .to(messageToSend.getTo())
                .cc(messageToSend.getCc())
                .referenceTableIdc(messageToSend.getReferenceTableIdc())
                .observation(messageToSend.getObservation())
                .numberOfAttempt(messageToSend.getNumberOfAttempt())
//                .createdAt(messageToSend.getCreatedAt())
//                .lastModifiedAt(messageToSend.getLastModifiedAt())
                .build();
    }

    public static MessageDTO mapToDomain(MessageToSendJpaEntity messageToSend) {
        return MessageDTO.builder()
                .id(messageToSend.getId())
                .messageTypeIdc(messageToSend.getMessageTypeIdc())
                .referenceId(messageToSend.getReferenceId())
                .message(messageToSend.getMessage())
                .subject(messageToSend.getSubject())
                .to(messageToSend.getTo())
                .cc(messageToSend.getCc())
                .referenceTableIdc(messageToSend.getReferenceTableIdc())
                .observation(messageToSend.getObservation())
                .numberOfAttempt(messageToSend.getNumberOfAttempt())
                .createdAt(messageToSend.getCreatedAt())
                .lastModifiedAt(messageToSend.getLastModifiedAt())
                .build();
    }
    //#endregion
}
