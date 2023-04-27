package com.scfg.core.adapter.persistence.notification;

import com.scfg.core.adapter.persistence.notificationUser.NotificationUserJpaEntity;
import com.scfg.core.adapter.persistence.notificationUser.NotificationUserKey;
import com.scfg.core.adapter.persistence.notificationUser.NotificationUserRepository;
import com.scfg.core.application.port.out.NotificationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.NotificationDTO;
import com.scfg.core.domain._NotificationDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationUserRepository notificationUserRepository;

    @Override
    public List<NotificationDTO> findAll() {
        List<NotificationDTO> list = notificationRepository.customFindAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return list;
    }

    @Override
    public List<NotificationDTO> findByToUserId(long toUserId) {
        List<NotificationDTO> list = notificationRepository.customFindAllToUserId(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(), toUserId);
        return list;
    }

    @Override
    public Boolean updateToRead(NotificationDTO notificationDTO) {
        Boolean result = false;
        NotificationUserJpaEntity notificationUserJpaEntity = notificationUserRepository.findById_NotificationIdAndId_ToUserId(notificationDTO.getId(),notificationDTO.getToUserId());
        if(notificationUserJpaEntity != null){
            notificationUserJpaEntity.setRead(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
            notificationUserRepository.save(notificationUserJpaEntity);
            result = true;
        }
        return result;
    }

    @Override
    public Boolean updateNote(NotificationDTO notificationDTO) {
        Boolean result = false;
        NotificationUserJpaEntity notificationUserJpaEntity = notificationUserRepository.findById_NotificationIdAndId_ToUserId(notificationDTO.getId(),notificationDTO.getToUserId());
        if(notificationUserJpaEntity != null){
            notificationUserJpaEntity.setNote(notificationDTO.getNote());
            notificationUserRepository.save(notificationUserJpaEntity);
            result = true;
        }
        return result;
    }

    @Override
    public List<NotificationDTO> save(_NotificationDTO notificationDTO) {

        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity();
        List<NotificationDTO> resultList = new ArrayList<>();

       Long notificationIdAux = notificationDTO.getId();

        if(notificationDTO.getId() <= 0){
            notificationJpaEntity = mapToJpaEntity(notificationDTO);
            notificationJpaEntity = notificationRepository.save(notificationJpaEntity);
            notificationIdAux = notificationJpaEntity.getId();
        }


        List<NotificationUserJpaEntity> notificationUserJpaEntityList = new ArrayList<>();
        Long finalNotificationIdAux = notificationIdAux;
        notificationDTO.getToUserId().forEach(item -> {
            NotificationUserKey notificationUserKey = new NotificationUserKey();
            notificationUserKey.notificationId = finalNotificationIdAux;
            notificationUserKey.toUserId = item;
            NotificationUserJpaEntity notificationUserJpaEntity = NotificationUserJpaEntity.builder()
                    .id(notificationUserKey)
                    .build();
            notificationUserJpaEntityList.add(notificationUserJpaEntity);
        });

        List<NotificationUserJpaEntity> auxList = notificationUserRepository.saveAll(notificationUserJpaEntityList);
        NotificationJpaEntity finalNotificationJpaEntity = notificationJpaEntity;
        auxList.forEach(item -> {
            NotificationDTO notificationDTOAux = mapToDomain(finalNotificationJpaEntity, item);
            resultList.add(notificationDTOAux);
        });

        return resultList;
    }

    //#region Mappers
    public static NotificationJpaEntity mapToJpaEntity(_NotificationDTO notificationDTO) {

        return NotificationJpaEntity.builder()
                .id(notificationDTO.getId())
                .fromUserId(notificationDTO.getFromUserId())
                .name(notificationDTO.getName())
                .description(notificationDTO.getDescription())
                .redirectionUrl(notificationDTO.getRedirectionUrl())
                .sentToGroup(notificationDTO.getSendToGroup())
                .roleId(notificationDTO.getRoleId())
                .build();
    }

    public static NotificationDTO mapToDomain(NotificationJpaEntity notificationJpaEntity, NotificationUserJpaEntity notificationUserJpaEntity) {

        return NotificationDTO.builder()
                .id(notificationJpaEntity.getId())
                .fromUserId(notificationJpaEntity.getFromUserId())
                .toUserId(notificationUserJpaEntity.getId().toUserId)
                .name(notificationJpaEntity.getName())
                .description(notificationJpaEntity.getDescription())
                .redirectionUrl(notificationJpaEntity.getRedirectionUrl())
                .read(notificationUserJpaEntity.getRead())
                .createdAt(notificationUserJpaEntity.getCreatedAt())
                .lastModifiedAt(notificationUserJpaEntity.getLastModifiedAt())
                .build();
    }
    //#endregion

}
