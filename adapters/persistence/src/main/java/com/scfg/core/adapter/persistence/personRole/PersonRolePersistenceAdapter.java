package com.scfg.core.adapter.persistence.personRole;
import com.scfg.core.application.port.out.PersonRolePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.person.PersonRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PersonRolePersistenceAdapter implements PersonRolePort {
    private final PersonRoleRepository personRoleRepository;

    @Override
    public PersonRole saveOrUpdate(PersonRole personRole){
        PersonRoleJpaEntity personRoleJpaEntity = mapToJpaEntity(personRole);
        personRoleJpaEntity = personRoleRepository.save(personRoleJpaEntity);
        return mapToDomain(personRoleJpaEntity);
    }

    @Override
    public boolean saveOrUpdateAll(List<PersonRole> personRoleList){
        List<PersonRoleJpaEntity> personRoleJpaEntityList = new ArrayList<>();
        personRoleList.forEach(e -> {
            PersonRoleJpaEntity personRol = mapToJpaEntity(e);
            personRoleJpaEntityList.add(personRol);
        });
        personRoleRepository.saveAll(personRoleJpaEntityList);
        return true;
    }

    //#region Mappers
    public static PersonRoleJpaEntity mapToJpaEntity(PersonRole personRole) {
        return new ModelMapper().map(personRole, PersonRoleJpaEntity.class);
    }

    public static PersonRole mapToDomain(PersonRoleJpaEntity personRoleJpaEntity) {
        return new ModelMapper().map(personRoleJpaEntity, PersonRole.class);
    }
    //#endregion

}
