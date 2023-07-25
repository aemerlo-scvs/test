package com.scfg.core.adapter.persistence.referencePerson;
import com.scfg.core.application.port.out.ReferencePersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.person.ReferencePerson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReferencePersonPersistenceAdapter implements ReferencePersonPort {
    private final ReferencePersonRepository referencePersonRepository;
    @Override
    public ReferencePerson saveOrUpdate(ReferencePerson referencePerson) {
        ReferencePersonJpaEntity referencePersonJpaEntity = mapToJpaEntity(referencePerson);
        referencePersonJpaEntity = referencePersonRepository.save(referencePersonJpaEntity);
        return mapToDomain(referencePersonJpaEntity);
    }
    @Override
    public boolean saveOrUpdateAll(List<ReferencePerson> referencePersonList){
        List<ReferencePersonJpaEntity> referencePersonJpaEntityList= new ArrayList<>();
        referencePersonList.forEach(e ->{
            ReferencePersonJpaEntity reference = mapToJpaEntity(e);
            referencePersonJpaEntityList.add(reference);
        });
        referencePersonRepository.saveAll(referencePersonJpaEntityList);
        return true;
    }

    //#region Mappers
    public static ReferencePersonJpaEntity mapToJpaEntity(ReferencePerson referencePerson) {
        return new ModelMapper().map(referencePerson,ReferencePersonJpaEntity.class);
    }

    public static ReferencePerson mapToDomain(ReferencePersonJpaEntity referencePersonJpaEntity) {
        return new ModelMapper().map(referencePersonJpaEntity, ReferencePerson.class);
    }
    //#endregion
}
