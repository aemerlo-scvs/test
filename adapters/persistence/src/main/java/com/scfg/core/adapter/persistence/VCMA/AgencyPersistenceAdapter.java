package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.AgencyRepository;
import com.scfg.core.application.port.out.AgencyPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.managers.Agency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersMethods.isNull;
import static com.scfg.core.common.util.HelpersMethods.mergeValues;

@PersistenceAdapter
@RequiredArgsConstructor
public class AgencyPersistenceAdapter implements AgencyPort {
    @Autowired
    private AgencyRepository agencyRepository;

    public AgencyRepository getAgencyRepository() {
        return agencyRepository;
    }

    public void setAgencyRepository(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }


    @Override
    public List<Agency> getAllAgency() {
        try {
            Object listAgency = agencyRepository.findAll();
            return (List<Agency>) listAgency;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Agency getAgencyByDescription(String agencyDescription) {
        Page<AgencyJpaEntity> pages = agencyRepository.findByDESCRIPTION(
                agencyDescription,
                PageRequest.of(0, 1));
        AgencyJpaEntity agencyJpaEntity = pages.stream()
                .findFirst()
                .orElseThrow(() ->
                        new NotDataFoundException("Agency Name: " + agencyDescription + " Not found")
                );
        return mapToDomain(agencyJpaEntity, false);
    }

    public synchronized AgencyJpaEntity insertAgency(AgencyJpaEntity mg) {
        try {
            return agencyRepository.save(mg);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Map<String, Object> findOrUpsert(Agency agencyDomain) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Page<AgencyJpaEntity> pages = agencyRepository.findByDESCRIPTION(
                agencyDomain.getDESCRIPTION(),
                PageRequest.of(0, 1));
        Optional<AgencyJpaEntity> agencyJpaEntityOptional = pages.stream().findFirst();
        AgencyJpaEntity agencyJpaEntity = agencyJpaEntityOptional.isPresent()
                ? agencyJpaEntityOptional.get()
                : null;
        if (isNull(agencyJpaEntity)) {
            agencyJpaEntity = new AgencyJpaEntity();
            result.put(KEY_ACTION_ENTITY, CREATE_ACTION);
            // code temp
            agencyJpaEntity.setAGENCY_ID(BigInteger.valueOf(agencyRepository.findMaxAgencyId() + 1));
        } else {
            result.put(KEY_ACTION_ENTITY, UPDATE_ACTION);
        }
        // assign RequestStatusId for default
        mergeValues(agencyJpaEntity, agencyDomain);
        agencyJpaEntity = agencyRepository.saveAndFlush(agencyJpaEntity);
        result.put(KEY_CONTENT_ENTITY, mapToDomain(agencyJpaEntity, false));

        return result;
        /*return mapToDomain(insuranceRequestRepository.findByRequestNumber(insuranceRequest.getRequestNumber())
                .orElseGet(() -> insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest))));
        return mapToDomain(insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest)));*/
    }


    //#region Mapper
    private AgencyJpaEntity mapToJpaEntity(Agency agency) {
        return new AgencyJpaEntity(
                agency.getAGENCY_ID() == null ? null : agency.getAGENCY_ID(),
                agency.getDESCRIPTION(),
                agency.getUSER_CREATE(),
                agency.getUSER_UPDATE(),
                agency.getDATE_CREATE(),
                agency.getDATE_UPDATE(),
                agency.getSTATUS(),
                agency.getBRANCH_OFFICE_ID(),
                agency.getZONES_ID());
    }

    private Agency mapToDomain(AgencyJpaEntity agencyJpaEntity, boolean withRelations) {
        return new Agency(
                agencyJpaEntity.getAGENCY_ID() == null ? null : agencyJpaEntity.getAGENCY_ID(),
                agencyJpaEntity.getDESCRIPTION(),
                agencyJpaEntity.getUSER_CREATE(),
                agencyJpaEntity.getUSER_UPDATE(),
                agencyJpaEntity.getDATE_CREATE(),
                agencyJpaEntity.getDATE_UPDATE(),
                agencyJpaEntity.getSTATUS(),
                agencyJpaEntity.getBRANCH_OFFICE_ID(),
                agencyJpaEntity.getZONES_ID()
        );
    }
//#endregion
}
