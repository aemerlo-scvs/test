package com.scfg.core.adapter.persistence.pep;

import com.scfg.core.adapter.persistence.role.RoleJpaEntity;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.application.port.out.PEPPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.common.Pep;
import com.scfg.core.domain.dto.SearchPepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PEPPersistenceAdapter implements PEPPort {

    private final PEPRepository pepRepository;
    private final EntityManager em;

    @Override
    public Pep findById(long pepId) {
        PEPJpaEntity pepJpaEntity = pepRepository.findById(pepId).orElseThrow(() -> new NotDataFoundException("Pep, no encontrado"));
        return mapToDomain(pepJpaEntity);
    }

    @Override
    public List<Pep> findAll() {
        List<PEPJpaEntity> list = pepRepository.findAll();
        return list.stream().map(PEPPersistenceAdapter::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public List<Pep> findAllByKeyWord(String keyWord) {
        return pepRepository.findByKeyWord(keyWord);
    }

    @Override
    public Object findAllByPage(int page, int size) {
        Pageable newPage = PageRequest.of(page, size);
        Page<PEPJpaEntity> list = pepRepository.findAll(newPage);
        return list;
    }

    @Override
    public Pep saveOrUpdate(Pep pep) {
        PEPJpaEntity pepJpaEntity = mapToJpaEntity(pep);
        pepJpaEntity = pepRepository.save(pepJpaEntity);
        return mapToDomain(pepJpaEntity);
    }

    @Override
    public Boolean saveOrUpdateAll(List<Pep> pep) {
        List<PEPJpaEntity> list = pep.stream().map(PEPPersistenceAdapter::mapToJpaEntity).collect(Collectors.toList());
        pepRepository.saveAll(list);
        return true;
    }

    @Override
    public Boolean delete(Pep pep) {
        // pepRepository.delete(mapToJpaEntity(pep));

        PEPJpaEntity pepJpaEntity = mapToJpaEntity(pep);
        pepJpaEntity.setStatus(PersistenceStatusEnum.DELETED.getValue());
        pepRepository.save(pepJpaEntity);

        return true;
    }

    @Override
    public List<Pep> existsByIdentificationNumber(String filters) {
        String query = pepRepository.getFindAllByFiltersBaseQuery() + filters;

        List<PEPJpaEntity> pepJpaEntityList = em.createQuery(query).getResultList();
        em.close();

        List<Pep> list = pepJpaEntityList.stream().map(PEPPersistenceAdapter::mapToDomain).collect(Collectors.toList());

        return list;
    }

    @Override
    public Boolean existsByIdentificationNumberOrName(SearchPepDTO pepDTO) {
        String query = pepRepository.getFindAllByFiltersBaseQuery() + this.getSearchPepDTOByIdentificationNumberOrNameFilters(pepDTO);

        List<PEPJpaEntity> pepJpaEntityList = em.createQuery(query).getResultList();

        em.close();
        return !pepJpaEntityList.isEmpty();
    }

    @Override
    public Boolean existsIdentificationNumber(String filters) {
        String query = pepRepository.getFindAllByFiltersBaseQuery() + filters;

        List<PEPJpaEntity> pepJpaEntityList = em.createQuery(query).getResultList();

        em.close();
        return !pepJpaEntityList.isEmpty();
    }

    //#region Mappers
    public static PEPJpaEntity mapToJpaEntity(Pep pep) {
        return PEPJpaEntity.builder()
                .id(pep.getId())
                .name(pep.getName())
                .lastName(pep.getLastName())
                .motherLastName(pep.getMotherLastName())
                .identificationNumber(pep.getIdentificationNumber())
                .issuancePlace(pep.getIssuancePlace())
                .charge(pep.getCharge())
                .countryCharge(pep.getCountryCharge())
                .pepType(pep.getPepType())
                .createdAt(pep.getCreatedAt())
                .lastModifiedAt(pep.getLastModifiedAt())
                .createdBy(pep.getCreatedBy())
                .lastModifiedBy(pep.getLastModifiedBy())
                .build();
    }

    public static Pep mapToDomain(PEPJpaEntity pepJpaEntity) {
        return Pep.builder()
                .id(pepJpaEntity.getId())
                .name(pepJpaEntity.getName())
                .lastName(pepJpaEntity.getLastName())
                .motherLastName(pepJpaEntity.getMotherLastName())
                .identificationNumber(pepJpaEntity.getIdentificationNumber())
                .issuancePlace(pepJpaEntity.getIssuancePlace())
                .charge(pepJpaEntity.getCharge())
                .countryCharge(pepJpaEntity.getCountryCharge())
                .pepType(pepJpaEntity.getPepType())
                .createdAt(pepJpaEntity.getCreatedAt())
                .lastModifiedAt(pepJpaEntity.getLastModifiedAt())
                .createdBy(pepJpaEntity.getCreatedBy())
                .lastModifiedBy(pepJpaEntity.getLastModifiedBy())
                .build();
    }

    //#endregion

    //#region Auxiliary Methods
    private String getSearchPepDTOByIdentificationNumberOrNameFilters(SearchPepDTO pepDTO) {
        String queryFilter = "";

        if (!pepDTO.getIdentificationNumber().isEmpty()) {
            queryFilter += " p.identificationNumber = " + "'" + pepDTO.getIdentificationNumber().trim() + "' ";
        }
        if (!pepDTO.getName().isEmpty()) {
            queryFilter += "OR (TRIM(p.name) LIKE " + "'%" + pepDTO.getName().trim().toUpperCase() + "%' AND";
            queryFilter += " TRIM(p.lastName) LIKE " + "'%" + pepDTO.getLastName().trim().toUpperCase() + "%' AND";
            queryFilter += " TRIM(p.motherLastName) LIKE " + "'%" + pepDTO.getMotherLastName().trim().toUpperCase() + "%')";
        }
        return queryFilter;
    }

    //#endregion

}
