package com.scfg.core.adapter.persistence.action;

import com.scfg.core.adapter.persistence.changeLog.ChangeLogRepository;
import com.scfg.core.adapter.persistence.role.RoleJpaEntity;
import com.scfg.core.application.port.out.ActionPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.common.Action;
import com.scfg.core.domain.common.Role;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ActionPersistenceAdapter implements ActionPort {

    private final ActionRepository actionRepository;

    @Override
    public List<Action> findAll() {
        Object actions = actionRepository.findAll();
        return (List<Action>) actions;
    }

    @Override
    public Action findById(long id) {
        ActionJpaEntity actionJpaEntity = actionRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Acción: " + id + " No encontrado"));
        return mapToDomain(actionJpaEntity);
    }

    //#region Mappers
    public static Action mapToDomain(ActionJpaEntity actionJpaEntity) {

        Action action = Action.builder()
                .id(actionJpaEntity.getId())
                .name(actionJpaEntity.getName())
                .description(actionJpaEntity.getDescription())
                .createdAt(actionJpaEntity.getCreatedAt())
                .lastModifiedAt(actionJpaEntity.getLastModifiedAt())
                .build();

        return action;
    }
    //#endregion
}
