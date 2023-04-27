package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.ManagerJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.ManagerRepository;
import com.scfg.core.application.port.out.ManagerPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.managers.Manager;
import lombok.RequiredArgsConstructor;
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
public class ManagerPersistenceAdapter implements ManagerPort {

    private final ManagerRepository managerRepository;

    /*public void setManagerRepository(ManagerRepositoryV2 managerRepository) {
        this.managerRepository = managerRepository;
    }*/

    @Override
    public List<Manager> getAllManager() {
        try {
            Object managerList = managerRepository.findAll();
            return (List<Manager>) managerList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Manager getManagerByFullname(String fullname) {
        Page<ManagerJpaEntity> pages = managerRepository.findByNAMES(
                fullname,
                PageRequest.of(0, 1));
        ManagerJpaEntity manager = pages.stream()
                .findFirst()
                .orElseThrow(() ->
                        new NotDataFoundException("Manager Name: " + fullname + " Not found")
                );
        return mapToDomain(manager, false);
    }

    public synchronized ManagerJpaEntity insertManager(ManagerJpaEntity mg) {
        try {
            return managerRepository.save(mg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Map<String, Object> findOrUpsert(Manager managerDomain) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Page<ManagerJpaEntity> pages = managerRepository.findByNAMES(
                managerDomain.getNAMES(),
                PageRequest.of(0, 1));
        Optional<ManagerJpaEntity> managerJpaEntityOptional = pages.stream().findFirst();
        ManagerJpaEntity managerJpaEntity = managerJpaEntityOptional.isPresent()
                ? managerJpaEntityOptional.get()
                : null;
        if (isNull(managerJpaEntity)) {
            managerJpaEntity = new ManagerJpaEntity();
            result.put(KEY_ACTION_ENTITY, CREATE_ACTION);
            // code temp
            managerJpaEntity.setMANAGER_ID(BigInteger.valueOf(managerRepository.findMaxManagerId()+1));
        } else {
            result.put(KEY_ACTION_ENTITY, UPDATE_ACTION);
        }
        // assign RequestStatusId for default
        mergeValues(managerJpaEntity, managerDomain);
        managerJpaEntity = managerRepository.saveAndFlush(managerJpaEntity);
        result.put(KEY_CONTENT_ENTITY, mapToDomain(managerJpaEntity, false));

        return result;
        /*return mapToDomain(insuranceRequestRepository.findByRequestNumber(insuranceRequest.getRequestNumber())
                .orElseGet(() -> insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest))));
        return mapToDomain(insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest)));*/
    }

    //#region Mapper
    private ManagerJpaEntity mapToJpaEntity(Manager manager) {
        return new ManagerJpaEntity(
                manager.getMANAGER_ID() == null ? null : manager.getMANAGER_ID(),
                manager.getNAMES(),
                manager.getUSER_CREATE(),
                manager.getUSER_UPDATE(),
                manager.getDATE_CREATE(),
                manager.getDATE_UPDATE(),
                manager.getSTATUS(),
                manager.getCARGO()
        );
    }

    private Manager mapToDomain(ManagerJpaEntity managerJpaEntity, boolean withRelations) {
        return new Manager(
                managerJpaEntity.getMANAGER_ID() == null ? null : managerJpaEntity.getMANAGER_ID(),
                managerJpaEntity.getNAMES(),
                managerJpaEntity.getUSER_CREATE(),
                managerJpaEntity.getUSER_UPDATE(),
                managerJpaEntity.getDATE_CREATE(),
                managerJpaEntity.getDATE_UPDATE(),
                managerJpaEntity.getSTATUS(),
                managerJpaEntity.getCARGO()
        );
    }
//#endregion
}
