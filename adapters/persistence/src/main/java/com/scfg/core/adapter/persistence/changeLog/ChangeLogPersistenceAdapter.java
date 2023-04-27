package com.scfg.core.adapter.persistence.changeLog;

import com.scfg.core.application.port.out.ChangeLogPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.ChangeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
class ChangeLogPersistenceAdapter implements ChangeLogPort {

    private final ChangeLogRepository changeLogRepository;

    @Override
    public Object findAllByPage(int page, int size) {
        Pageable newPage = PageRequest.of(page, size);
        return changeLogRepository.findAll(newPage);
    }

    @Override
    public Boolean save(ChangeLog changeLog) {
        ChangeLogJpaEntity changeLogJpaEntity = mapToJpaEntity(changeLog);
        changeLogJpaEntity = changeLogRepository.save(changeLogJpaEntity);
        return changeLogJpaEntity != null;
    }

    //#region Mapper
    private ChangeLogJpaEntity mapToJpaEntity(ChangeLog changeLog) {
        return ChangeLogJpaEntity.builder()
                .id(changeLog.getId())
                .action(changeLog.getAction())
                .table(changeLog.getTable())
                .referenceId(changeLog.getReferenceId())
                .referenceObject(changeLog.getReferenceObject())
                .createdAt(changeLog.getCreatedAt())
                .browser(changeLog.getBrowser())
                .ip(changeLog.getIp())
                .userId(changeLog.getUserId())
                .build();
    }
    //#endregion
}
