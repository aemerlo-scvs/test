package com.scfg.core.adapter.persistence.commecialManagement;

import com.scfg.core.application.port.out.CommercialManagementPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
public class CommercialManagementPersistenceAdapter implements CommercialManagementPort {

    private final CommercialManagementRepository repository;

    @Override
    public PersistenceResponse save(CommercialManagement obj, boolean returnEntity) {
        CommercialManagementJpaEntity entity = mapToJpaEntity(obj);
        entity=repository.save(entity);
        obj=mapToDomain(entity);
        return new PersistenceResponse(
                CommercialManagementPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                obj
        );
    }

    @Override
    public PersistenceResponse update(CommercialManagement obj) {
        CommercialManagementJpaEntity entity = mapToJpaEntity(obj);
        entity = repository.save(entity);
        obj = mapToDomain(entity);
        return new PersistenceResponse(
                CommercialManagementPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                obj
        );
    }

    @Override
    public CommercialManagement findById(Long id) {
        CommercialManagementJpaEntity cms = this.repository.findById(id, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomain(cms);
    }

    @Override
    public Boolean saveAll(List<CommercialManagement> commercialManagementList) {
        List<CommercialManagementJpaEntity> commercialManagementJpaEntities = new ArrayList<>();
        commercialManagementList.forEach(e -> {
            CommercialManagementJpaEntity cm = mapToJpaEntity(e);
            commercialManagementJpaEntities.add(cm);
        });
        repository.saveAll(commercialManagementJpaEntities);
        return true;
    }

    private CommercialManagementJpaEntity mapToJpaEntity(CommercialManagement obj) {
        return new ModelMapperConfig().getStrictModelMapper().map(obj, CommercialManagementJpaEntity.class);
    }
    public static CommercialManagement mapToDomain(CommercialManagementJpaEntity obj) {
        return new ModelMapper().map(obj, CommercialManagement.class);
    }
}
