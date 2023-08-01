package com.scfg.core.adapter.persistence.newPerson;

import com.scfg.core.adapter.persistence.clause.ClauseJpaEntity;
import com.scfg.core.adapter.persistence.clause.ClausePersistenceAdapter;
import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.adapter.persistence.personRole.PersonRoleJpaEntity;
import com.scfg.core.application.port.out.NewPersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
import com.scfg.core.domain.person.NewPerson;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.NTextType;
import org.modelmapper.ModelMapper;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mssql.googlecode.concurrentlinkedhashmap.Weighers.map;

@PersistenceAdapter
@RequiredArgsConstructor
public class NewPersonPersistenceAdapter implements NewPersonPort {

    private final NewPersonRepository newPersonRepository;

    private final EntityManager em;

    private static String GET_SEARCH_PERSONS = "exec proc_search_persons :documentType,:documentNumber,:personName";

    @Override
    public long saveOrUpdate(NewPerson newPerson) {
        NewPersonJpaEntity newPersonJpaEntity = ObjectMapperUtils.map(newPerson, NewPersonJpaEntity.class);
        newPersonJpaEntity = newPersonRepository.save(newPersonJpaEntity);
        return newPersonJpaEntity.getId();
    }

    @Override
    public boolean saveOrUpdateAll(List<NewPerson> newPersonList){
        List<NewPersonJpaEntity> newPersonJpaEntityList = new ArrayList<>();
        newPersonList.forEach(e -> {
            NewPersonJpaEntity newPerson = mapToJpaEntity(e);
            newPersonJpaEntityList.add(newPerson);
        });
        newPersonRepository.saveAll(newPersonJpaEntityList);
        return true;
    }

    @Override
    public Object searchPerson(Long documentTypeIdc, String identificationNumber, String name) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("proc_search_persons")
                .registerStoredProcedureParameter(
                        "documentTypeIdc",
                        Integer.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "identificationNumber",
                        String.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "name",
                        String.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "result",
                        NTextType.class,
                        ParameterMode.OUT
                )
                .setParameter("documentTypeIdc", documentTypeIdc.intValue())
                .setParameter("identificationNumber", identificationNumber)
                .setParameter("name", name);
        query.execute();

        Object list = (Object) query.getOutputParameterValue("result");
        em.close();

        return list;
    }

    @Override
    public boolean findByIdentificationNumber(String identificationNumber){
        return this.newPersonRepository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public NewPerson findById(long newPersonId) {
        Optional<NewPersonJpaEntity> newPerson = Optional.ofNullable(newPersonRepository.customFindById(newPersonId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()));
        return newPerson.isPresent() ? ObjectMapperUtils.map(newPerson.get(), NewPerson.class): null;
    }


    //#region Mappers
    public static NewPersonJpaEntity mapToJpaEntity(NewPerson newPerson) {
        return new ModelMapper().map(newPerson,NewPersonJpaEntity.class);
    }

    public static NewPerson mapToDomain(NewPersonJpaEntity newPersonJpaEntity) {
        return new ModelMapper().map(newPersonJpaEntity, NewPerson.class);
    }
    //#endregion
}
