package com.scfg.core.adapter.persistence.telephone;

import com.scfg.core.application.port.out.TelephonePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.Telephone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class TelephonePersistenceAdapter implements TelephonePort {
    private final TelephoneRepository telephoneRepository;

    @Override
    public Telephone saveOrUpdate(Telephone telephone) {
        TelephoneJpaEntity telephoneJpaEntity = mapToJpaEntity(telephone);
        telephoneJpaEntity = telephoneRepository.save(telephoneJpaEntity);
        return mapToDomain(telephoneJpaEntity);
    }

    @Override
    public boolean saveOrUpdateAll(List<Telephone> telephoneList) {
        List<TelephoneJpaEntity> telephoneJpaEntityList = new ArrayList<>();
        telephoneList.forEach(e -> {
            TelephoneJpaEntity telephone = mapToJpaEntity(e);
            telephoneJpaEntityList.add(telephone);
        });
        telephoneRepository.saveAll(telephoneJpaEntityList);
        return true;
    }

    @Override
    public List<Telephone> getAllByPersonId(Long personId) {
        List<TelephoneJpaEntity> telephoneJpaEntityList = telephoneRepository
                                                          .findAllByPersonId(personId,
                                                                  PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<Telephone> telephoneList = new ArrayList<>();
        telephoneJpaEntityList.forEach(x -> {
            Telephone t = mapToDomain(x);
            telephoneList.add(t);
        });
        return telephoneList;
    }

    @Override
    public List<Telephone> getAllByNewPersonId(Long newPersonId) {
        List<TelephoneJpaEntity> telephoneJpaEntityList = telephoneRepository
                .findAllByNewPersonId(newPersonId,
                        PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<Telephone> telephoneList = new ArrayList<>();
        telephoneJpaEntityList.forEach(x -> {
            Telephone t = mapToDomain(x);
            telephoneList.add(t);
        });
        return telephoneList;
    }

    //#region Mappers
    public static TelephoneJpaEntity mapToJpaEntity(Telephone telephone) {
        return new ModelMapper().map(telephone,TelephoneJpaEntity.class);
    }

    public static Telephone mapToDomain(TelephoneJpaEntity telephoneJpaEntity) {
        return new ModelMapper().map(telephoneJpaEntity, Telephone.class);
    }
    //#endregion
}
