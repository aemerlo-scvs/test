package com.scfg.core.adapter.persistence.CommercialManagementLog;

import com.scfg.core.application.port.out.CommercialManagementLogPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagementLog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommercialManagementLogPersistenceAdapter implements CommercialManagementLogPort {
    private  final  CommercialManagementLogRepository repository;
    @Override
    public PersistenceResponse save(CommercialManagementLog obj, boolean returnEntity) {
        CommercialManagementLogJpaEntity entity = mapToJpaEntity(obj);
        entity=repository.save(entity);
        obj=mapToDomain(entity);
        return new PersistenceResponse(
                CommercialManagementLogJpaEntity.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                obj
        );    }

    @Override
    public List<CommercialManagementLog> getAllByCommercialManagementId(Long idCommercialManagement) {
        List<CommercialManagementLogJpaEntity> cmlList = repository.getAllByCommercialManagementId(idCommercialManagement,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return cmlList.size()>0 ?  ObjectMapperUtils.mapAll(cmlList, CommercialManagementLog.class) : new ArrayList<>();
    }
    private CommercialManagementLogJpaEntity mapToJpaEntity(CommercialManagementLog obj) {
        return new ModelMapperConfig().getStrictModelMapper().map(obj, CommercialManagementLogJpaEntity.class);
    }
    public static CommercialManagementLog mapToDomain(CommercialManagementLogJpaEntity obj) {
        return new ModelMapper().map(obj, CommercialManagementLog.class);
    }
}
